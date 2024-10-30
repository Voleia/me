package me.voleia.volands.Listeners;

import me.angeschossen.lands.api.LandsIntegration;
import me.clip.placeholderapi.PlaceholderAPI;
import me.voleia.volands.Commands.RaceAdminCommand;
import me.voleia.volands.Enums.ChatMode;
import me.voleia.volands.Enums.Race;
import me.voleia.volands.OC;
import me.voleia.volands.Volands;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import net.md_5.bungee.api.chat.*;

import java.util.*;

public class ChatListener implements Listener {

    Volands system = Volands.getSystem();


    public HashMap<Player, Boolean> prefixMap = new HashMap<Player, Boolean>();
    HashMap<Player, ChatMode> chatMode = new HashMap<>();
    HashMap<Player, Boolean> globalMuted = new HashMap<>();

    boolean adminMute = false;

    public boolean toggleMute() {
        adminMute=!adminMute;
        return adminMute;
    }

    public void setChatMode(Player p, ChatMode m) {
        chatMode.put(p,m);
        p.sendMessage(ChatColor.GREEN + "You are now in " + ChatColor.YELLOW + m.toString() + ChatColor.GREEN + " chat mode");
    }
    public void removeMode(Player p) {
        chatMode.remove(p);
    }

    public boolean isGlobalMuted(Player p) {
        if (!globalMuted.containsKey(p)) {
            return false;
        }
        return globalMuted.get(p);
    }

    public void toggleGlobalMute(Player p) {
        if (!globalMuted.containsKey(p) || !globalMuted.get(p)) {
            globalMuted.put(p,true);
            p.sendMessage(ChatColor.GREEN + "Muted your Global Chat!");
        } else {
            globalMuted.put(p,false);
            p.sendMessage(ChatColor.GREEN + "Unmuted your Global Chat!");
        }
    }

    int whisperDist=8;
    int localDist=48;

    LandsIntegration lapi = system.getIntegration();

    public net.md_5.bungee.api.ChatColor getGenColor(Player p) {
        OC oc = system.getOC(p);
        if (system.getChat().prefixMap.containsKey(p) && system.getChat().prefixMap.get(p)) {
            return system.getChat().getColor(PlaceholderAPI.setPlaceholders(p, "%luckperms_prefix%"));
        } else {
            return system.getChatColor(oc.getRace());
        }
    }

    List<String> bannedTerms = Arrays.asList("pussy", "nigga", "nig", "nigger",
            "shithead", "fag", "faggot", "paki", "p word", "ez", "kys",
            "keep yourself safe", "kill yourself", "keep your self safe",
            "kill your self", "hail hitler", "sieg hail", "heil hitler", "sieg heil", "whore");

    @EventHandler (priority = EventPriority.LOWEST)
    public void PlayerChat(PlayerChatEvent e) {
        if (system.getSelectingName().containsKey(e.getPlayer())) {
            e.setCancelled(true);
            String message = e.getMessage();
            Player player = e.getPlayer();
            if (message.contains(" ")) {
                String msg = message.substring(0,message.indexOf(" "));
                if (msg.length()>=3 && msg.length()<=16) {
                    if (alphanumeric(msg)) {
                        system.setName(msg,player);
                        //valid message
                    } else {
                        player.sendMessage(ChatColor.RED + "Your character name can only consist of letters!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Your name must be a single word between 3 and 16 letters!");
                }
            } else if (message.length()>=3 && message.length()<=16) {
                if (alphanumeric(message)) {
                    system.setName(message, player);
                    //valid message
                } else {
                    player.sendMessage(ChatColor.RED + "Your character name can only consist of letters!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Your name must be between 3 and 16 letters!");
            }
        } //if the player is selecting their name
        else if (lapi.getLandPlayer(e.getPlayer().getUniqueId()).getChatMode()==null) {
            Player player = e.getPlayer();
            String rank = PlaceholderAPI.setPlaceholders(player, "%luckperms_prefix%");
            ChatMode mode = chatMode.get(player);
            e.setCancelled(true);
            String muted = system.getRaceAdminCommand().isPlayerMuted(player);
            if (muted==null) {
                switch (mode) {
                    case GLOBAL: {
                        if (!adminMute) {
                            //ooc chat
                            if (isGlobalMuted(player)) {
                                globalMuted.put(player,false);
                                player.sendMessage(ChatColor.RED + "You automatically unmuted global chat due to talking in global!");
                            }
                            OC oc = system.getOC(player);
                            String ocname = oc.getName();
                            if (!oc.getPrefix().equals("Blank")) {
                                ocname = oc.getPrefix() + " " + ocname;
                            }

                            TextComponent message = createChatMessage(player,oc," "+player.getName() + " (" + ocname + ") ", e.getMessage(), rank);

                            //String message = ChatColor.DARK_GRAY + "[G] " + ChatColor.WHITE+rank + " " + getColor(rank) + player.getName() + " (" + ocname + ")" + ChatColor.DARK_GRAY + " >> " + ChatColor.translateAlternateColorCodes('&',"&f"+e.getMessage());
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (!isGlobalMuted(p)) {
                                    p.spigot().sendMessage(message);
                                }
                            }
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "discord broadcast **"+ oc.getRace().toString()+"** " + player.getName()+" ("+ocname+") » "+e.getMessage());
                            break;
                        } else {
                            player.sendMessage(ChatColor.RED + "Public chat is currently muted for the whole server! do '/local' to switch to local chat.");
                        }
                    }
                    case RACE: {
                        if (isGlobalMuted(player)) {
                            globalMuted.put(player,false);
                            player.sendMessage(ChatColor.RED + "You automatically unmuted global chat due to talking in race chat!");
                        }
                        OC oc = system.getOC(player);
                        String ocname = oc.getName();
                        if (!oc.getPrefix().equals("Blank")) {
                            ocname = oc.getPrefix() + " " + ocname;
                        }

                        String message;
                        if (!oc.getRace().equals(Race.ELF)) {
                            message = ChatColor.DARK_GRAY + "["+system.translate(oc.getRace().toString().substring(0,4))+"] "+ getColor(rank)+player.getName()+" ("+ocname+")" + ChatColor.DARK_GRAY + " >> " + ChatColor.translateAlternateColorCodes('&',"&f"+e.getMessage());
                        } else {
                            message = ChatColor.DARK_GRAY + "["+system.translate("Elf")+"] "+ getColor(rank)+player.getName()+" ("+ocname+")" + ChatColor.DARK_GRAY + " >> " + ChatColor.translateAlternateColorCodes('&',"&f"+e.getMessage());
                        }

                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (!isGlobalMuted(p) && system.getOC(p)!=null && system.getOC(p).getRace().equals(oc.getRace())) {
                                p.sendMessage(message);
                            }
                        }
                        break;
                    }
                    case LOCAL: {
                        OC oc = system.getOC(player);
                        String ocname = oc.getName();
                        if (!oc.getPrefix().equals("Blank")) {
                            ocname = oc.getPrefix() + " " + ocname;
                        }
                        if (!oc.getSuffix().equals("Blank")) {
                            ocname = ocname + " " + oc.getSuffix();
                        }
                        String curMsg = e.getMessage();
                        String message;
                        if (curMsg.contains("/")) {
                            if (curMsg.indexOf("/")==curMsg.length()-1) {
                                message=ChatColor.ITALIC+checkForOCName(curMsg.substring(0,curMsg.indexOf("/")),oc.getName());
                            } else {
                                message=checkForOCName(curMsg.substring(0,curMsg.indexOf("/")),oc.getName())+"\n"+getColor(rank) + ocname + ChatColor.DARK_GRAY + " >> " + ChatColor.WHITE + curMsg.substring(curMsg.indexOf("/")+1);
                            }
                        } else {
                            message = getGenColor(player) + ocname + ChatColor.DARK_GRAY + " >> " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&',"&f"+e.getMessage());
                        }
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            int distance = (int) p.getLocation().distance(player.getLocation());
                            if (distance <= localDist) {
                                p.sendMessage(ChatColor.GRAY + "[" + distance + "m] " + message);
                            }
                        }
                        break;
                    }
                    case WHISPER: {
                        OC oc = system.getOC(player);
                        String ocname = oc.getName();
                        if (!oc.getPrefix().equals("Blank")) {
                            ocname = oc.getPrefix() + " " + ocname;
                        }
                        if (!oc.getSuffix().equals("Blank")) {
                            ocname = ocname + " " + oc.getSuffix();
                        }
                        String curMsg = e.getMessage();
                        String message;
                        if (curMsg.contains("/")) {
                            if (curMsg.indexOf("/")==curMsg.length()-1) {
                                message=ChatColor.ITALIC+checkForOCName(curMsg.substring(0,curMsg.indexOf("/")),oc.getName());
                            } else {
                                message=checkForOCName(curMsg.substring(0,curMsg.indexOf("/")),oc.getName())+"\n"+getColor(rank) + ocname + ChatColor.DARK_GRAY + " >> " + ChatColor.WHITE + curMsg.substring(curMsg.indexOf("/")+1);
                            }
                        } else {
                            message = getGenColor(player) + ocname + ChatColor.DARK_GRAY + " >> " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&',"&f"+e.getMessage());
                        }
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            int distance = (int) p.getLocation().distance(player.getLocation());
                            if (distance <= whisperDist) {
                                p.sendMessage(ChatColor.GRAY + "[" + distance + "m] " + message);
                            }
                        }
                        break;
                    }
                }
            } else {
                player.sendMessage(muted);
            }
        }
    }

    static String secondsToTime(int seconds) {
        seconds/=60;
        int s = seconds;
        int r = s/60;
        s-=(r*60);
        if (s==0) {
            return r+"h";
        }
        return r+"h "+s+"m";
    }

    net.md_5.bungee.api.chat.TextComponent createChatMessage(Player player, OC oc, String name, String message, String rank) {
        TextComponent rankComp;
        TextComponent nameComp;

        boolean containsBannedTerm = false;
        for (String term : bannedTerms) {
            if (message.contains(" "+term+" ")) {
                containsBannedTerm=true;
                break;
            }
        }
        if (containsBannedTerm) {
            int i = new Random().nextInt(0,6);
            switch (i) {
                case 0:
                    message = "Please teach me how to be as good as you at this.";
                    break;
                case 1:
                    message = "Hail Koyuki, Ruler of the Crownlands!";
                    break;
                case 2:
                    message = "I surrender, I can't fight someone as good as you.";
                    break;
                case 3:
                    message = "You really are such a kind person :)";
                    break;
                case 4:
                    message = "GG! You're really great at this.";
                    break;
                default:
                    message = "Voleia is definitely the best admin";
                    break;
            }
            Volands.getSystem().getRaceAdminCommand().mute(player.getName(), Volands.getSystem().getRaceAdminCommand().parseStringToLong("5m"), "Toxic Chat", "Chat Filter");
        }

        if (prefixMap.containsKey(player) && prefixMap.get(player)) {
            rankComp = new net.md_5.bungee.api.chat.TextComponent(rank);
            rankComp.setColor(net.md_5.bungee.api.ChatColor.WHITE);
            nameComp = new net.md_5.bungee.api.chat.TextComponent(name);
            nameComp.setColor(getColor(rank));
        } else {
            rankComp = new net.md_5.bungee.api.chat.TextComponent(system.getRaceIcon(oc.getRace()));
            rankComp.setColor(net.md_5.bungee.api.ChatColor.WHITE);
            nameComp = new net.md_5.bungee.api.chat.TextComponent(name);
            nameComp.setColor(system.getChatColor(oc.getRace()));
        }
        TextComponent divider = new net.md_5.bungee.api.chat.TextComponent(">> ");
        divider.setColor(net.md_5.bungee.api.ChatColor.DARK_GRAY);
        TextComponent msg = new net.md_5.bungee.api.chat.TextComponent(net.md_5.bungee.api.ChatColor.WHITE + net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&',message));
        rankComp.addExtra(nameComp);
        rankComp.addExtra(divider);
        rankComp.addExtra(msg);
        String textToShow = net.md_5.bungee.api.ChatColor.DARK_GREEN + system.translate("Name: ") + net.md_5.bungee.api.ChatColor.GREEN + oc.getName();
        textToShow+="\n"+(net.md_5.bungee.api.ChatColor.DARK_GREEN + system.translate("Race: ") + net.md_5.bungee.api.ChatColor.GREEN + oc.getRace().toString());
        textToShow+="\n"+(net.md_5.bungee.api.ChatColor.DARK_GREEN + system.translate("Level: ") + net.md_5.bungee.api.ChatColor.GREEN + oc.getLevel());
        textToShow+="\n"+(net.md_5.bungee.api.ChatColor.DARK_GREEN + system.translate("XP: ") + net.md_5.bungee.api.ChatColor.GREEN + (int)oc.getXP());
        textToShow+="\n"+(net.md_5.bungee.api.ChatColor.DARK_GREEN + system.translate("Time Played: ") + net.md_5.bungee.api.ChatColor.GREEN + secondsToTime(oc.getTimePlayed()));
        rankComp.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(HoverEvent.Action.SHOW_TEXT, new net.md_5.bungee.api.chat.ComponentBuilder(textToShow).create()));
        return rankComp;
    }

    String checkForOCName(String msg, String name) {
        if (!msg.contains(" ")) {
            if (msg.equals(name)) {
                return (ChatColor.ITALIC + msg);
            }
            return (ChatColor.ITALIC+name + " " + ChatColor.ITALIC + msg);
        } else {
              if (!msg.substring(0,msg.indexOf(" ")).contains(name)) {
                    return (ChatColor.ITALIC+name + " " + ChatColor.ITALIC + msg);
              }
              return (ChatColor.ITALIC+msg);
        }
    }

    public net.md_5.bungee.api.ChatColor getColor(String rank) {
        net.md_5.bungee.api.ChatColor color;
        if (rank.contains("\uE800"))
        {

            //owner
            color = net.md_5.bungee.api.ChatColor.of("#42ff77");
        }
        else if (rank.contains("\uE700"))
        {
            //admin
            color = net.md_5.bungee.api.ChatColor.of("#36ffd0");
        }
        else if (rank.contains("\uE900"))
        {
            //manager
            color = net.md_5.bungee.api.ChatColor.of("#b92145");
        }
        else if (rank.contains("\uE600"))
        {
            //trial mod
            color = net.md_5.bungee.api.ChatColor.of("#4b9eff");
        }
        else if (rank.contains("\uE500"))
        {
            //mod
            color = net.md_5.bungee.api.ChatColor.of("#35f4ff");
        }
        else if (rank.contains("\uE400"))
        {
            //dev
            color = net.md_5.bungee.api.ChatColor.of("#ff7d21");
        }
        else if (rank.contains("\uE300"))
        {
            //builder
            color = net.md_5.bungee.api.ChatColor.of("#ffc726");
        }
        else if (rank.contains("\uE200"))
        {
            //media
            color = net.md_5.bungee.api.ChatColor.of("#ff1a1a");
        }
        else if (rank.contains("\uEA00"))
        {
            //premium
            color = net.md_5.bungee.api.ChatColor.of("#9c2fff");
        }
        else if (rank.contains("\uE100"))
        {
            //supporter
            color = net.md_5.bungee.api.ChatColor.of("#ff1a80");
        }
        else if (rank.contains("\uEB00"))
        {
            //traveler
            color = net.md_5.bungee.api.ChatColor.GRAY;
        }
        else
        {
            color = net.md_5.bungee.api.ChatColor.of("#ffffff");
        }
        return color;
    }

    net.md_5.bungee.api.ChatColor getColor(Race race) {
        switch (race) {
            case WRAITH:
                return net.md_5.bungee.api.ChatColor.DARK_BLUE;
            case NEKO:
                return net.md_5.bungee.api.ChatColor.DARK_GREEN;
            case HUMAN:
                return net.md_5.bungee.api.ChatColor.AQUA;
            case ELF:
                return net.md_5.bungee.api.ChatColor.GREEN;
            case AMBYSTOMA:
                return net.md_5.bungee.api.ChatColor.BLUE;
            case DEMON:
                return net.md_5.bungee.api.ChatColor.RED;
            case DWARF:
                return net.md_5.bungee.api.ChatColor.GRAY;
            case DARKELF:
                return net.md_5.bungee.api.ChatColor.LIGHT_PURPLE;
            case DRAGONBORN:
                return net.md_5.bungee.api.ChatColor.DARK_GRAY;
        }
        return net.md_5.bungee.api.ChatColor.GRAY;
    }

    public boolean alphanumeric(String s) {
        char[] ch = s.toCharArray();
        for (char c : ch) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    } //if alphanumeric

    @EventHandler
    public void PlayerMove(PlayerMoveEvent e) {
        if (system.getSelectingName().containsKey(e.getPlayer())) {
            e.getPlayer().sendMessage(ChatColor.RED + "Select an OC name first!");
            e.setCancelled(true);
        }
    } //if the player is moving

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
        if (!e.isCancelled()) {
            OC oc = system.getOC(e.getPlayer());
            if (oc!=null && oc.getRace().equals(Race.DWARF)) {
                if (oc.getRaceClass().getFlag("3by3").equals(true)) {
                    Location loc = e.getBlock().getLocation();
                    ItemStack netheritePickaxe=new ItemStack(Material.NETHERITE_PICKAXE);
                    for (int x = loc.getBlockX()-1; x<=loc.getBlockX()+1; x+=1) {
                        for (int y = loc.getBlockY()-1; y<=loc.getBlockY()+1; y+=1) {
                            for (int z = loc.getBlockZ()-1; z<=loc.getBlockZ()+1; z+=1) {
                                Block block = e.getPlayer().getWorld().getBlockAt(x,y,z);
                                if (block.getType()!=Material.AIR && block.getType()!=Material.BEDROCK && block.getType()!=Material.OBSIDIAN &&
                                        (lapi.getLandByChunk(e.getPlayer().getWorld(),e.getPlayer().getLocation().getChunk().getX(),e.getPlayer().getLocation().getChunk().getZ())==null || lapi.getLandByChunk(e.getPlayer().getWorld(),e.getPlayer().getLocation().getChunk().getX(),e.getPlayer().getLocation().getChunk().getZ())
                                                .getTrustedPlayers().contains(e.getPlayer().getUniqueId()))) {
                                    block.breakNaturally(netheritePickaxe);
                                    block.getWorld().spawnParticle(Particle.CRIT, block.getLocation(),100,0.1,0.1,0.1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
