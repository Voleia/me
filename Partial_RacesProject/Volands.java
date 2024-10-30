package me.voleia.volands;

import me.angeschossen.lands.api.LandsIntegration;
import me.voleia.volands.Abilities.*;
import me.voleia.volands.Commands.*;
import me.voleia.volands.Config.ConfigHandler;
import me.voleia.volands.Config.PHExpansion;
import me.voleia.volands.Enums.Race;
import me.voleia.volands.Enums.invType;
import me.voleia.volands.GUIs.*;
import me.voleia.volands.Listeners.ChatListener;
import me.voleia.volands.Listeners.RaceListener;
import me.voleia.volands.Races.ArmorStack;
import me.voleia.volands.SkillTree.InvIcon;
import me.voleia.volands.SkillTree.Skill;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Volands extends JavaPlugin {

    public static Volands system;
    public static HashMap<Player, OC> OCMap = new HashMap<>();
    HashMap<Player, Race> selectingName = new HashMap<>();
    HashMap<Player, invType> raceCreationScreen = new HashMap<>();

    int tickNum=0;
    public static ConfigHandler handler;

    public void addSelecting(Player player, Race race) {
        if (!selectingName.containsKey(player)) {
            selectingName.put(player, race);
        }
    }


    public static Ability[] abilities = new Ability[10];

    HashMap<Character,Character> cc = new HashMap<>();

    ArrayList<Player> leaving = new ArrayList<>();
    HashMap<Player, ArmorStack> armors = new HashMap<>();

    public void removeArmor(Player player) {
        armors.remove(player);
    }

    public void loadArmor(Player player) {
        armors.get(player).loadInventory(player);
    }

    public void saveArmor(Player player) {
        armors.put(player,new ArmorStack(player));
    }

    public boolean hasArmorStack(Player player) {
        return armors.containsKey(player);
    }

    public void addLeaving(Player player) {
        leaving.add(player);
    }

    public void removeLeaving(Player player) {
        leaving.remove(player);
    }

    public boolean isLeaving(Player player) {
        return leaving.contains(player);
    }

    public ChatListener getChat() {
        return chatListener;
    }

    ChatListener chatListener;

    boolean landsEnabled=false;

    LandsIntegration integration;

    public LandsIntegration getIntegration() {
        return integration;
    }

    RaceAdminCommand radmin;

    public static String ptToggleable;
    public static String ptActive;
    public static String ptPassive;
    public static String ptLocked;
    public static String ptUnlocked;
    public static String ptAvailible;
    public static String ptSkillTree;
    public static String ptExpensive;

    public RaceAdminCommand getRaceAdminCommand() {
        return radmin;
    }

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        integration = LandsIntegration.of(this);
        system = this;

        handler = new ConfigHandler();

        cc.put('a','ᴀ');
        cc.put('b','ʙ');
        cc.put('c','ᴄ');
        cc.put('d','ᴅ');
        cc.put('e','ᴇ');
        cc.put('f','ғ');
        cc.put('g','ɢ');
        cc.put('h','ʜ');
        cc.put('i','ɪ');
        cc.put('j','ᴊ');
        cc.put('k','ᴋ');
        cc.put('l','ʟ');
        cc.put('m','ᴍ');
        cc.put('n','ɴ');
        cc.put('o','ᴏ');
        cc.put('p','ᴘ');
        cc.put('q','ǫ');
        cc.put('r','ʀ');
        cc.put('s','s');
        cc.put('t','ᴛ');
        cc.put('u','ᴜ');
        cc.put('v','ᴠ');
        cc.put('w','ᴡ');
        cc.put('x','x');
        cc.put('y','ʏ');
        cc.put('z','z');

        abilities[0]=new ThreeByThree(); //dwarf ability
        abilities[1]=new ElvenDash(); //elf ability
        abilities[2]=new ForwardTeleport(); //shadowkin ability
        abilities[3]=new DraconicBoost(); //dragon ability
        abilities[4]=new DragonMount(); //dragon ability
        abilities[5]=new WraithInvisibility(); //wraith ability
        abilities[6]=new NightVisionToggle(); //wraith + shadowkin ability
        abilities[7]=new NightVisionToggleLevelTwo(); //ambystoma ability
        abilities[8]=new NekoNightVisionToggle();
        abilities[9]=new TriggerClimb();

        radmin = new RaceAdminCommand();
        this.getCommand("raceadmin").setExecutor(radmin);
        this.getCommand("ocadmin").setExecutor(radmin);
        this.getCommand("oca").setExecutor(radmin);
        this.getCommand("race").setExecutor(new RaceCommand());
        this.getCommand("oc").setExecutor(new RaceCommand());
        this.getCommand("race").setTabCompleter(new RaceCommandTabCompletion());
        this.getCommand("oc").setTabCompleter(new RaceCommandTabCompletion());
        this.getCommand("diceroll").setExecutor(new diceroll());

        this.getCommand("chat").setExecutor(new ChatCommand());
        this.getCommand("mode").setExecutor(new ChatCommand());
        this.getCommand("g").setExecutor(new ChatCommand());
        this.getCommand("global").setExecutor(new ChatCommand());
        this.getCommand("ooc").setExecutor(new ChatCommand());
        this.getCommand("local").setExecutor(new ChatCommand());
        this.getCommand("races").setExecutor(new ChatCommand());
        this.getCommand("globalmute").setExecutor(new ChatCommand());
        this.getCommand("muteglobal").setExecutor(new ChatCommand());

        this.getCommand("chat").setTabCompleter(new ChatCommandTabCompletion());
        this.getCommand("mode").setTabCompleter(new ChatCommandTabCompletion());
        this.getCommand("raceadmin").setTabCompleter(radmin);
        this.getCommand("ocadmin").setTabCompleter(radmin);
        this.getCommand("oca").setTabCompleter(radmin);

        raceListener = new RaceListener();
        Bukkit.getPluginManager().registerEvents(raceListener, this);
        //Bukkit.getPluginManager().registerEvents(new RaceSelectionGUI(), this);
        Bukkit.getPluginManager().registerEvents(new ModernizedRaceSelection(), this);
        Bukkit.getPluginManager().registerEvents(new OCSelectionGUI(), this);
        Bukkit.getPluginManager().registerEvents(new XPListener(), this);
        Bukkit.getPluginManager().registerEvents(new TitleGUIHandler(), this);

        chatListener = new ChatListener();
        Bukkit.getPluginManager().registerEvents(chatListener, this);

        //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mutechat 48h");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null) {
            new PHExpansion(this).register();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (OC c : OCMap.values()) {
                    c.onTick();
                }
                sendSelectingMessages();
            }
        }.runTaskTimer(this,20,20); //repeat every second;

        ptAvailible = "&a"+translate("Available");
        ptActive = "&6"+translate("Active");
        ptExpensive = "&e"+translate("Too Expensive");
        ptLocked = "&c"+translate("Locked");
        ptPassive = "&b"+translate("Passive");
        ptToggleable = "&7" + translate("Toggleable");
        ptUnlocked = "&9" + translate("Unlocked");
        ptSkillTree = translate("Skill Tree");
    }

    HashMap<InvIcon, Integer> iconModelData = new HashMap<InvIcon, Integer>() {{
        put(InvIcon.BACKGROUND, 17);
        put(InvIcon.DOWN_STRAIGHT, 20);
        put(InvIcon.DOWN_FROM_RIGHT, 21);
        put(InvIcon.DOWN_FROM_LEFT, 22);
        put(InvIcon.STRAIGHT_ACROSS, 23);
        put(InvIcon.T, 24);
        put(InvIcon.INCOMP_STRAIGHT_ACROSS, 25);
        put(InvIcon.ACTIVE_UNLOCKED, 30);
        put(InvIcon.ACTIVE_AVAILIBLE, 31);
        put(InvIcon.ACTIVE_EXPENSIVE, 32);
        put(InvIcon.ACTIVE_LOCKED, 33);
        put(InvIcon.PASSIVE_UNLOCKED, 40);
        put(InvIcon.PASSIVE_AVAILIBLE, 41);
        put(InvIcon.PASSIVE_EXPENSIVE, 42);
        put(InvIcon.PASSIVE_LOCKED, 43);
        put(InvIcon.TOGGLE_UNLOCKED, 50);
        put(InvIcon.TOGGLE_AVAILIBLE, 51);
        put(InvIcon.TOGGLE_EXPENSIVE, 52);
        put(InvIcon.TOGGLE_LOCKED, 53);
        put(InvIcon.ARROW_UP, 60);
        put(InvIcon.ARROW_DOWN, 61);
        put(InvIcon.ARROW_RIGHT, 62);
        put(InvIcon.ARROW_LEFT, 63);
    }};

    public ItemStack getSkillItemStack(InvIcon icon, String name, List<String> lore) {
        name = ChatColor.translateAlternateColorCodes('&', "&7"+name);
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, ChatColor.translateAlternateColorCodes('&', "&7"+lore.get(i)));
        }
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.setCustomModelData(iconModelData.get(icon));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getSkillItemStack(InvIcon icon) {
        return getSkillItemStack(icon, "-", new ArrayList<String>());
    }

    public void removeFromCreation(Player p) {
        if (raceCreationScreen.containsKey(p)) {
            raceCreationScreen.remove(p);
        }
    }

    RaceListener raceListener;

    public RaceListener getRaceListener() {
        return raceListener;
    }

    public boolean creatingRace(Player p) {
        return raceCreationScreen.containsKey(p);
    }

    public invType getCreatingRaceInventory(Player p) {
        return raceCreationScreen.get(p);
    }

    public void addCreating(Player p, invType i) {
        raceCreationScreen.put(p,i);
    }

    public String getRaceIcon(Race race) {
        switch (race) {
            case DEMON:
                return "\uE003";
            case WRAITH:
                return "\uE002";
            case NEKO:
                return "\uE00C";
            case HUMAN:
                return "\uE00B";
            case ELF:
                return "\uE00D";
            case AMBYSTOMA:
                return "\uE005";
            case DWARF:
                return "\uE001";
            case DARKELF:
                return "\uE00A";
            case DRAGONBORN:
                return "\uE00E";
        }
        return null;
    }

    public net.md_5.bungee.api.ChatColor getChatColor(Race race) {
        switch (race) {
            case DEMON:
                return net.md_5.bungee.api.ChatColor.of("#AF3D00");
            case WRAITH:
                return net.md_5.bungee.api.ChatColor.of("#1E4096");
            case NEKO:
                return net.md_5.bungee.api.ChatColor.of("#9A1C37");
            case HUMAN:
                return net.md_5.bungee.api.ChatColor.of("#397A37");
            case ELF:
                return net.md_5.bungee.api.ChatColor.of("#CDA416");
            case AMBYSTOMA:
                return net.md_5.bungee.api.ChatColor.of("#22907A");
            case DWARF:
                return net.md_5.bungee.api.ChatColor.of("#634E4F");
            case DARKELF:
                return net.md_5.bungee.api.ChatColor.of("#4E2C82");
            case DRAGONBORN:
                return net.md_5.bungee.api.ChatColor.of("#6D1A1D");
        }
        return net.md_5.bungee.api.ChatColor.WHITE;
    }

    public void sendSelectingMessages() {
        PotionEffect blndns = new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, false, false, true);
        for (Player player : selectingName.keySet()) {
            player.addPotionEffect(blndns);
            player.sendTitle(ChatColor.YELLOW + "Type a name!", ChatColor.WHITE + "This will be your character name / nickname in Ashea.",0,22,5);
        }
    }

    public HashMap<Player, Race> getSelectingName() {
        return selectingName;
    }

    public void setName(String name, Player p) {
        OCMap.put(p, new OC(selectingName.get(p), p, 1, 5000, name, 0,-1,"Blank","Blank", false, new ArrayList<String>()));
        p.removePotionEffect(PotionEffectType.BLINDNESS);
        selectingName.remove(p);
    }

    public void removeSelecting(Player p) {
        if (selectingName.containsKey(p)) {
            selectingName.remove(p);
        }
    }

    public ConfigHandler getHandler() {
        return handler;
    }

    @Override
    public void onDisable() {
        for (OC c : OCMap.values()) {
            handler.saveOC(c);
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (hasArmorStack(p)) {
                loadArmor(p);
                removeArmor(p);
            }
        }
    }

    HashMap<Player, TitleGUI> titles = new HashMap<>();

    public void putTitle(Player p, TitleGUI title) {
        titles.put(p,title);
    }

    public TitleGUI getTitle(Player p) {
        return titles.get(p);
    }

    public void removeTitle(Player p) {
        titles.remove(p);
    }

    public static Volands getSystem() {
        return system;
    }

    public Ability[] getAbilities() {
        return abilities;
    }

    public OC getOC(Player p) {
        return OCMap.get(p);
    }

    public HashMap<Player, OC> getOCMap() {
        return OCMap;
    }

    public void putToMap(Player p, OC oc) {
        if (OCMap.containsKey(p)) {
            handler.saveOC(OCMap.get(p));
        }
        OCMap.put(p, oc);
        //p.getInventory().setContents(handler.GrabOCContents(oc));
        p.sendMessage(ChatColor.GREEN + "Loaded OC: " + oc.getName() + ", " + oc.getRace().toString());
        if (oc.getRace().equals(Race.DRAGONBORN)) {
            giveElytra(p);
        } else {
            removeElytra(p);
        }
    }

    public boolean giveElytra(Player player) {
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        elytra.addEnchantment(Enchantment.BINDING_CURSE,1);
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate==null || chestplate.getType().equals(Material.ELYTRA)) {
            player.getInventory().setChestplate(elytra);
            return true;
        } else { //if chestplate is exists and ISNT elytra
            player.getWorld().dropItem(player.getLocation(), chestplate);
            player.getInventory().setChestplate(elytra);
            player.sendMessage(ChatColor.RED + "Your chestplate has been dropped!");
            return true;
        }
    }

    public boolean removeElytra(Player player) {
        ItemStack i = player.getInventory().getChestplate();
        if (i==null) {
            return true;
        } else if (i.getType()==Material.ELYTRA) {
            player.getInventory().setChestplate(null);
            return true;
        }
        return false;
    }

    public void removeFromMap(Player p) {
        OCMap.remove(p);
    }

    public boolean blockAround(Location location) {
        Block[] blocks = new Block[4];
        blocks[0]=location.getWorld().getBlockAt(location.getBlockX()-1,location.getBlockY(),location.getBlockZ());
        blocks[1]=location.getWorld().getBlockAt(location.getBlockX()+1,location.getBlockY(),location.getBlockZ());
        blocks[2]=location.getWorld().getBlockAt(location.getBlockX(),location.getBlockY(),location.getBlockZ()-1);
        blocks[3]=location.getWorld().getBlockAt(location.getBlockX(),location.getBlockY(),location.getBlockZ()+1);
        for (Block block : blocks) {
            if (!block.getType().equals(Material.AIR) && block.getType().isCollidable()) {
                return true;
            }
        }
        return false;
    }

    public String translate(String str) {
        String newString = "";
        for (char c : str.toCharArray()) {
            c=Character.toLowerCase(c);
            if (cc.containsKey(c)) {
                newString+=String.valueOf(cc.get(c));
            } else {
                newString+=String.valueOf(c);
            }
        }
        return newString;
    }

    HashMap<Player,Long> timeAsOC = new HashMap<>();

    public boolean allowedToSwitch(Player player) {
        if (!timeAsOC.containsKey(player)) {
            return true;
        }
        return (System.currentTimeMillis()-timeAsOC.get(player)>1800000);
    }

    public int getTimeLeft(Player player) {
        long difference = System.currentTimeMillis()-timeAsOC.get(player);
        int dif = (int) difference;
        return (dif/1800000);
    }

    public void putSwitch(Player player) {
        timeAsOC.put(player,System.currentTimeMillis());
    }
}
