package me.voleia.volands.Listeners;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import me.voleia.volands.CustomGUISystem.CustomGUI;
import me.voleia.volands.Enums.ChatMode;
import me.voleia.volands.Enums.Race;
import me.voleia.volands.Enums.invType;
import me.voleia.volands.GUIs.ModernizedRaceSelection;
import me.voleia.volands.GUIs.OCSelectionGUI;
import me.voleia.volands.GUIs.RaceSelectionGUI;
import me.voleia.volands.OC;
import me.voleia.volands.Races.Neko;
import me.voleia.volands.Volands;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RaceListener implements Listener {

    ArrayList<Material> ores = new ArrayList<>();
    ArrayList<Material> fish = new ArrayList<>();
    ArrayList<EntityType> nethm = new ArrayList<>();

    public RaceListener() {
        ores.add(Material.COAL_ORE);
        ores.add(Material.COPPER_ORE);
        ores.add(Material.DIAMOND_ORE);
        ores.add(Material.IRON_ORE);
        ores.add(Material.EMERALD_ORE);
        ores.add(Material.GOLD_ORE);
        ores.add(Material.LAPIS_ORE);
        ores.add(Material.REDSTONE_ORE);
        ores.add(Material.DEEPSLATE_COAL_ORE);
        ores.add(Material.DEEPSLATE_COPPER_ORE);
        ores.add(Material.DEEPSLATE_DIAMOND_ORE);
        ores.add(Material.DEEPSLATE_IRON_ORE);
        ores.add(Material.DEEPSLATE_EMERALD_ORE);
        ores.add(Material.DEEPSLATE_GOLD_ORE);
        ores.add(Material.DEEPSLATE_LAPIS_ORE);
        ores.add(Material.DEEPSLATE_REDSTONE_ORE);

        fish.add(Material.SALMON);
        fish.add(Material.TROPICAL_FISH);
        fish.add(Material.COOKED_SALMON);
        fish.add(Material.COD);
        fish.add(Material.PUFFERFISH);
        fish.add(Material.COOKED_COD);

        nethm.add(EntityType.BLAZE);
        nethm.add(EntityType.MAGMA_CUBE);
        nethm.add(EntityType.GHAST);
        nethm.add(EntityType.PIGLIN);
        nethm.add(EntityType.PIGLIN_BRUTE);
        nethm.add(EntityType.ZOMBIFIED_PIGLIN);
        nethm.add(EntityType.ZOGLIN);
        nethm.add(EntityType.HOGLIN);
    }

    Volands system = Volands.getSystem();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void offhandEvent(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        OC oc = system.getOC(p);
        if (oc!=null) {
            if (e.getPlayer().isSneaking()) {
                oc.abilityMenuEvent(e);
                e.setCancelled(true);
            } else if (oc.getRace().equals(Race.NEKO) && !p.isOnGround() && oc.getRaceClass().getFlag("doubleJump").equals(true) && oc.getLevel()>=3){
                oc.getRaceClass().setFlag("doubleJump",false);
                p.getWorld().spawnParticle(Particle.CRIT, new Location(p.getLocation().getWorld(),p.getLocation().getX(),p.getLocation().getY()-1,p.getLocation().getZ()),100,0.1,0.1,0.1);
                p.getWorld().playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1f, 1f);
                p.setVelocity(p.getLocation().getDirection().normalize().multiply(0.9)); //neko boost
                e.setCancelled(true);
            }
        }
    }


    public List<CustomGUI> allGUIS = new ArrayList<>();

    public void removeGUI(CustomGUI gui) {
        allGUIS.remove(gui);
    }

    public void addGUI(CustomGUI gui) {
        allGUIS.add(gui);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void invClickEvent(InventoryClickEvent e) {
        for (CustomGUI gui : allGUIS) {
            if (gui.viewport==e.getInventory()) {
                gui.InventoryClicked(e.getSlot(), e.isLeftClick());
                e.setCancelled(true);
                return;
            }
        }
        if (e.getCurrentItem()!=null && e.getCurrentItem().getType().equals(Material.ELYTRA)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void switchHandEvent(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        ItemStack item=player.getInventory().getItem(e.getNewSlot());
        Material m;
        if (item!=null) {
            m=item.getType();
        } else {
            m=Material.AIR;
        }
        OC oc = system.getOC(e.getPlayer());
        if (oc!=null) {
            if(!oc.scrollEvent(e)) { //scroll event
                if (oc.getRace().equals(Race.HUMAN) && oc.getLevel()>=2) {
                    if (m.equals(Material.FISHING_ROD)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, Integer.MAX_VALUE, 0, false, false, true));
                    } else {
                        removeArtificialPotion(player,PotionEffectType.LUCK);
                    } //human luck
                    if (oc.getLevel()>=3) {
                        if (m.equals(Material.DIAMOND_AXE) || m.equals(Material.GOLDEN_AXE) || m.equals(Material.IRON_AXE) || m.equals(Material.STONE_AXE) || m.equals(Material.WOODEN_AXE) || m.equals(Material.NETHERITE_AXE)) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0, false, false, true));
                        } else {
                            removeArtificialPotion(player,PotionEffectType.FAST_DIGGING);
                        } //human digspeed
                    }
                } //human luck and digspeed
                else if (oc.getRace().equals(Race.DWARF) && oc.getLevel()>=1) {
                    if (m.equals(Material.DIAMOND_PICKAXE) || m.equals(Material.GOLDEN_PICKAXE) || m.equals(Material.IRON_PICKAXE) || m.equals(Material.STONE_PICKAXE) || m.equals(Material.WOODEN_PICKAXE) || m.equals(Material.NETHERITE_PICKAXE)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0, false, false, true));
                    } else {
                        removeArtificialPotion(player,PotionEffectType.FAST_DIGGING);
                    } //dwarven digspeed
                } //dwarven digspeed
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void punchEvent(PlayerInteractEvent e) {
        if (e.getPlayer().isSneaking() && e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            OC oc = system.getOC(e.getPlayer());
            if (oc!=null) {
                oc.activateAbility();
            }
        }
    }

    //TEMPORARY EVENT
    @EventHandler
    public void pje(PlayerJoinEvent e) {
        String banned = system.getRaceAdminCommand().isPlayerBanned(e.getPlayer());
        if (banned==null) {
            ocSelectionMenu(e.getPlayer());
            system.getChat().setChatMode(e.getPlayer(), ChatMode.GLOBAL);
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().sendMessage(ChatColor.RED + "Remember, you can use /chat <local/global> to switch chat modes!");
                    cancel();
                }
            }.runTaskTimer(system,60,20); //repeat every second;
        } else {
            e.getPlayer().kickPlayer(banned);
        }
    }

    public void ocSelectionMenu(Player player) {
        File OCfile = new File(system.getDataFolder(), "OCs");
        if (!OCfile.exists()) {
            OCfile.mkdir();
        }
        File playerFile = new File(system.getDataFolder(),"OCs/"+player.getUniqueId().toString());
        if (!playerFile.exists()) {
            playerFile.mkdir();
        }
        File playerHandler = new File(system.getDataFolder(),"OCs/"+player.getUniqueId().toString()+"/handler.yml");
        if (!playerHandler.exists()) {
            try {playerHandler.createNewFile();}catch(Exception ignored){}
        }

        ArrayList<OC> OCs = system.handler.loadOCs(player);
        if (OCs == null || OCs.isEmpty()) { //empty OCs
            //open race selection gui
            new BukkitRunnable() {
                @Override
                public void run() {
                    ModernizedRaceSelection rsgui = new ModernizedRaceSelection();
                    player.openInventory(rsgui.raceSelectionSmall());
                    system.addCreating(player, invType.RACE);
                    cancel();
                }
            }.runTaskTimer(system,4,20);
        } else if (OCs.size()==1) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    system.putToMap(player, OCs.get(0));
                    cancel();
                }
            }.runTaskTimer(system,4,20);
        } else {
            //open OC selection gui
            new BukkitRunnable() {
                @Override
                public void run() {
                    OCSelectionGUI ocgui = new OCSelectionGUI();
                    player.openInventory(ocgui.OCGUICreator(player));
                    system.addCreating(player,invType.OCC);
                    cancel();
                }
            }.runTaskTimer(system,4,20);
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void deathEvent (PlayerDeathEvent e) { //wraith death and elytra removal
        int sz = e.getDrops().size();
        for (int i = 0; i < sz; i+=1) { //remove dragon wings
            if (e.getDrops().get(i).getType().equals(Material.ELYTRA)) {
                e.getDrops().remove(i);
            }
        }
        Player player = e.getPlayer();
        OC oc = system.getOC(e.getPlayer());
        if (oc!=null && oc.getRace().equals(Race.WRAITH)) {
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_SCULK_SHRIEKER_SHRIEK, 2f, 2f);
        }
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onDurabilityDec(PlayerItemDamageEvent e) {
        if (e.getItem().getType().equals(Material.ELYTRA)) {
            e.setCancelled(true);
        } else if (e.getItem().getType().equals(Material.BOW)) {
            OC oc = system.getOC(e.getPlayer());
            if (oc!=null && oc.getRace().equals(Race.ELF)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void respawnEvent(PlayerRespawnEvent e) {
        Player player = e.getPlayer();;
        if (system.getOC(player)==null) {
            ocSelectionMenu(e.getPlayer());
        } else if (system.getOC(player).getRace().equals(Race.DRAGONBORN)) { //dragon wings
            system.giveElytra(player);
        } else {
            system.removeElytra(player);
        }
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void leaveEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (system.hasArmorStack(player)) {
            system.loadArmor(player);
            system.removeArmor(player);
        }
        OC oc = system.getOC(player);
        system.removeFromMap(player);
        system.removeFromCreation(player);
        system.removeSelecting(player);
        if (oc!=null) {
            system.getHandler().saveOC(oc);
        }
        system.getChat().removeMode(player);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void inventoryClose(InventoryCloseEvent e) {
        int allguisize = allGUIS.size();
        for (int i = 0; i < allguisize; i++) {
            if (allGUIS.get(i).viewport==e.getInventory()) {
                removeGUI(allGUIS.get(i));
                break;
            }
        }
        if (e.getReason()!=InventoryCloseEvent.Reason.OPEN_NEW && e.getReason()!=InventoryCloseEvent.Reason.CANT_USE && e.getReason()!=InventoryCloseEvent.Reason.DEATH && e.getReason()!=InventoryCloseEvent.Reason.UNLOADED && e.getReason()!=InventoryCloseEvent.Reason.UNKNOWN && e.getReason()!=InventoryCloseEvent.Reason.DISCONNECT) {
            Player p = (Player) e.getPlayer();
            if (system.creatingRace(p) && p.isOnline()) {
                system.removeFromCreation(p);
                p.kickPlayer(ChatColor.WHITE+"READ THIS WHOLE MESSAGE\n\n"+ChatColor.BOLD+"You can rejoin immediately!"+ChatColor.WHITE+"\n\nYou just have to select a race after joining the server!\n\nClick one of the faces in the menu to select it. It CAN be changed later!");
            }
        }
    }

    boolean removeArtificialPotion(Player p, PotionEffectType pot) {
        if (p.hasPotionEffect(pot) && p.getPotionEffect(pot).getDuration()>500000) {
            p.removePotionEffect(pot);
            return true;
        }
        return false;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void blockBreakEvent(BlockBreakEvent e) {
        if (!e.isCancelled()) {
            OC oc = system.getOC(e.getPlayer());
            if (oc!=null) {
                if (e.getBlock().getBlockData() instanceof org.bukkit.block.data.Ageable) {
                    if (oc.getRace().equals(Race.HUMAN) && oc.getLevel()>=1) {
                        World playerWorld = e.getPlayer().getWorld();
                        Location location = e.getPlayer().getLocation();
                        for (ItemStack i : e.getBlock().getDrops()) {
                            playerWorld.dropItem(location,i);
                        }
                    }
                } //increased human harvesting (passive)
                if (oc.getRace().equals(Race.DWARF) && (e.getPlayer().getItemInHand()==null || !e.getPlayer().getItemInHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH))) {
                    if (ores.contains(e.getBlock().getType())) {
                        int blessed=0;
                        for (ItemStack drop : e.getBlock().getDrops()) {
                            Random r = new Random();
                            World playerWorld = e.getPlayer().getWorld();
                            Location location = e.getPlayer().getLocation();
                            if (r.nextInt(10) < 5) {
                                playerWorld.dropItem(location,drop);
                                blessed+=1;
                            }
                        }
                        if (blessed>0) {
                            oc.sendRaceMessage(ChatColor.GOLD + "Your dwarven prowess has helped you find "+blessed+" extra ore!");
                        }
                    }
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void PlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            OC oc = system.getOC(player);
            if (oc!=null) {
                if (system.creatingRace(player) || system.getSelectingName().containsKey(player)) {
                    e.setCancelled(true);
                }
                else if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                    if (oc.getRace().equals(Race.ELF) && oc.getLevel()>=2) {
                        e.setDamage(e.getDamage()/2);
                        oc.sendRaceMessage(ChatColor.GOLD + "Your elven magic has lessened your fall");
                    }
                } else if (oc.getRace().equals(Race.DARKELF)) {
                    oc.getRaceClass().setFlag("sneakingTime",0);
                    if (removeArtificialPotion(player,PotionEffectType.INVISIBILITY)) {
                        player.sendMessage(ChatColor.RED + "You have taken damage and lost your invisibility");
                    }
                } else if (oc.getRace().equals(Race.WRAITH)) {
                    if (removeArtificialPotion(player,PotionEffectType.INVISIBILITY)) {
                        player.removePotionEffect(PotionEffectType.SPEED);
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_SCULK_SHRIEKER_SHRIEK, 1f, 1f);
                        player.sendMessage(ChatColor.RED + "You have taken damage and lost your invisibility");
                    }
                } else if (oc.getRace().equals(Race.DRAGONBORN)) {
                    //dragon lose flight if damaged
                    if (player.isGliding()) {
                        player.setGliding(false);
                        oc.getRaceClass().setFlag("stunTime",3);
                        player.sendMessage(ChatColor.RED + "You have been hurt mid-air! You have lost your flight for 3 seconds.");
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 3f, 3f);
                    }
                }
            } else {
                e.setCancelled(true);
            }

            if (system.hasArmorStack(player)) {
                 system.loadArmor(player);
                 system.removeArmor(player);
                 double points = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                 double toughness = player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getValue();
                 e.setDamage(e.getDamage()*(1 - Math.min(20, Math.max(points / 5, points - e.getDamage() / (2 + toughness / 4))) / 25));
            }
        }
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void invisEnd(EntityPotionEffectEvent e) {
        if (e.getCause().equals(EntityPotionEffectEvent.Cause.EXPIRATION)) {
            if (e.getModifiedType().equals(PotionEffectType.INVISIBILITY)) {
                if (e.getEntity() instanceof Player) {
                    Player player = (Player) e.getEntity();
                    if (system.hasArmorStack(player)) {
                        system.loadArmor(player);
                        system.removeArmor(player);
                    }
                }
            }
        } else {
            if (e.getModifiedType().equals(PotionEffectType.INVISIBILITY)) {
                if (e.getEntity() instanceof Player) {
                    Player player = (Player) e.getEntity();
                    if (!system.hasArmorStack(player)) {
                        system.saveArmor(player);
                    }
                }
            }
        }
    }


    @EventHandler (priority = EventPriority.HIGH)
    public void dragonGlide(EntityToggleGlideEvent e) {
        if (e.getEntity()instanceof Player) {
            Player player = (Player)e.getEntity();
            OC oc = system.getOC(player);
            if (e.isGliding()) {
                if (!oc.getRace().equals(Race.DRAGONBORN)) {
                    while (player.getInventory().contains(Material.ELYTRA)) {
                        player.getInventory().remove(Material.ELYTRA);
                    }
                    try {
                        while (player.getInventory().contains(Material.LEGACY_ELYTRA)) {
                            player.getInventory().remove(Material.LEGACY_ELYTRA);
                        }
                    } catch (Exception exc) {
                        System.out.println("Could not remove depracated item LEGACY_ELYTRA: " + exc.toString());
                    }
                    e.setCancelled(true);
                } else {
                    if (((int)oc.getRaceClass().getFlag("stunTime"))>0) {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You cannot fly for " + oc.getRaceClass().getFlag("stunTime") + " more seconds!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void PlayerToggleSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        OC oc = system.getOC(player);
        if (oc!=null) {
            if (oc.getRace().equals(Race.DARKELF)) {
                oc.getRaceClass().setFlag("sneakingTime",0);
                removeArtificialPotion(player,PotionEffectType.INVISIBILITY);
            } else if (oc.getRace().equals(Race.WRAITH) && oc.getLevel()>=2) {
                if (e.isSneaking()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE,0,false,false,true));
                } else {
                    removeArtificialPotion(player,PotionEffectType.SLOW_FALLING);
                }
            } else if (oc.getRace().equals(Race.NEKO)) {
                if (e.isSneaking() && system.blockAround(player.getLocation()) && ((Neko)oc.getRaceClass()).getCanClimb()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,Integer.MAX_VALUE,0,false,false,false));
                } else {
                    removeArtificialPotion(player,PotionEffectType.LEVITATION);
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void FireBow(EntityShootBowEvent e) {
        if (!e.isCancelled()) {
            if (e.getEntity() instanceof Player) {
                Player player = (Player)e.getEntity();
                OC oc = system.getOC(player);
                if (oc.getRace().equals(Race.ELF) && oc.getLevel()>=3) {
                    e.setConsumeItem(false);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void changeTarget(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player) {
            if (e.getEntity() instanceof Spider || e.getEntity() instanceof CaveSpider) {
                OC oc = system.getOC((Player) e.getTarget());
                if (oc.getRace().equals(Race.DARKELF) && oc.getLevel()>=1) {
                    e.setCancelled(true);
                }
            } else if (e.getEntity() instanceof Zombie || e.getEntity() instanceof Skeleton || e.getEntity() instanceof Drowned) {
                OC oc = system.getOC((Player) e.getTarget());
                if (oc.getRace().equals(Race.WRAITH) && oc.getLevel()>=2) {
                    e.setCancelled(true);
                }
            } else if (e.getEntity() instanceof Creeper) {
                OC oc = system.getOC((Player) e.getTarget());
                if (oc.getRace().equals(Race.NEKO) && oc.getLevel()>=3) {
                    e.setCancelled(true);
                }
            } else if (nethm.contains(e.getEntity().getType())) {
                OC oc = system.getOC((Player) e.getTarget());
                if (oc.getRace().equals(Race.DEMON) && oc.getLevel() >= 2) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void rocketBoost(PlayerElytraBoostEvent e) {
        e.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.LOW)
    public void DragonDrownEvent(EntityAirChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player)e.getEntity();
            OC oc = system.getOC(player);
            if (oc.getRace().equals(Race.DRAGONBORN)) {
                if (oc.getRaceClass().getFlag("savebreath").equals(true)) {
                    e.setCancelled(true);
                }
                oc.getRaceClass().setFlag("savebreath", "");
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void dragonRightClickEvent(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            e.setCancelled(true);
            Player mount = (Player) e.getRightClicked();
            Player rider = e.getPlayer();
            OC oc = system.getOC(mount);
            if (oc!=null) {
                if (oc.getRace().equals(Race.DRAGONBORN)) {
                    if (oc.getRaceClass().getFlag("rideable").equals(true)) {
                        mount.addPassenger(rider);
                        rider.getWorld().playSound(rider.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 1f, 1f);
                    } else {
                        rider.sendMessage(ChatColor.RED + "That player has mounting disabled!");
                    }
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void damageEntityEvent(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player=(Player)e.getDamager();
            OC oc = system.getOC(player);
            if (oc!=null && oc.getRace().equals(Race.WRAITH)) {
                if (removeArtificialPotion(player,PotionEffectType.INVISIBILITY)) {
                    player.sendMessage(ChatColor.RED + "You have dealt damage and lost your invisibility!");
                }
            }
            if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                OC damagerOC = system.getOC((Player)e.getDamager());
                if (damagerOC!=null && damagerOC.getRace().equals(Race.DRAGONBORN)) {
                    e.setDamage(e.getDamage()+(damagerOC.getSpeed()/7.2));
                }
            }

            if (e.getEntity() instanceof Player) {
                if (player.getPassengers().contains(e.getEntity())) {
                    e.setCancelled(true);
                    if (player.isSneaking()) {
                        if (oc!=null) {
                            oc.activateAbility();
                        }
                    }
                } else if (e.getEntity().getPassengers().contains(player)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void nekoEat(PlayerItemConsumeEvent e) {
        if (fish.contains(e.getItem().getType())) {
            Player player = e.getPlayer();
            OC oc = system.getOC(player);
            if (oc.getRace().equals(Race.NEKO) && oc.getLevel()>=2) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0,false,false,true));
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void nekoJump(PlayerJumpEvent e) {
        OC oc = system.getOC(e.getPlayer());
        if (oc.getRace().equals(Race.NEKO)) {
            oc.getRaceClass().setFlag("doubleJump",true);
        }
    }
}
