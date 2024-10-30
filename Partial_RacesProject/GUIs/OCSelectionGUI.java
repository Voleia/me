package me.voleia.volands.GUIs;

import me.voleia.volands.Enums.Race;
import me.voleia.volands.Enums.invType;
import me.voleia.volands.OC;
import me.voleia.volands.Volands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class OCSelectionGUI implements Listener {

    Volands system = Volands.getSystem();

    public Inventory OCGUICreator(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Character Selection");
        int time = system.getHandler().getTimePlayed(player);

        //Owned OC's
        ArrayList<OC> ocs = Volands.handler.loadOCs(player);
        for (int i = 0; i < 3; i+=1) {
            if (i<ocs.size()) {
                OC oc = ocs.get(i);
                ItemStack slot;
                ItemMeta meta;
                List<String> lore = new ArrayList<>();

                switch (oc.getRace()) {
                    case ELF:
                        slot = new ItemStack(Material.NETHER_STAR);
                        meta = slot.getItemMeta();
                        meta.setDisplayName(ChatColor.GREEN + oc.getName());
                        lore.add(ChatColor.ITALIC + "Elf");
                        lore.add(" ");
                        break;
                    case NEKO:
                        slot = new ItemStack(Material.JUNGLE_SAPLING);
                        meta = slot.getItemMeta();
                        meta.setDisplayName(ChatColor.DARK_GREEN + oc.getName());
                        lore.add(ChatColor.ITALIC + "Neko");
                        lore.add(" ");
                        break;
                    case DEMON:
                        slot = new ItemStack(Material.FIRE_CHARGE);
                        meta = slot.getItemMeta();
                        meta.setDisplayName(ChatColor.RED + oc.getName());
                        lore.add(ChatColor.ITALIC + "Demon");
                        lore.add(" ");
                        break;
                    case DWARF:
                        slot = new ItemStack(Material.RAW_GOLD);
                        meta = slot.getItemMeta();
                        meta.setDisplayName(ChatColor.GRAY + oc.getName());
                        lore.add(ChatColor.ITALIC + "Dwarf");
                        lore.add(" ");
                        break;
                    case DRAGONBORN:
                        slot = new ItemStack(Material.ELYTRA);
                        meta = slot.getItemMeta();
                        meta.setDisplayName(ChatColor.YELLOW + oc.getName());
                        lore.add(ChatColor.ITALIC + "Dragonborn");
                        lore.add(" ");
                        break;
                    case HUMAN:
                        slot = new ItemStack(Material.WHEAT);
                        meta = slot.getItemMeta();
                        meta.setDisplayName(ChatColor.GOLD + oc.getName());
                        lore.add(ChatColor.ITALIC + "Human");
                        lore.add(" ");
                        break;
                    case WRAITH:
                        slot = new ItemStack(Material.ENDERMAN_SPAWN_EGG);
                        meta = slot.getItemMeta();
                        meta.setDisplayName(ChatColor.BLUE + oc.getName());
                        lore.add(ChatColor.ITALIC + "Wraith");
                        lore.add(" ");
                        break;
                    case AMBYSTOMA:
                        slot = new ItemStack(Material.HEART_OF_THE_SEA);
                        meta = slot.getItemMeta();
                        meta.setDisplayName(ChatColor.AQUA + oc.getName());
                        lore.add(ChatColor.ITALIC + "Ambystoma");
                        lore.add(" ");
                        break;
                    case DARKELF:
                        slot = new ItemStack(Material.ENDER_EYE);
                        meta = slot.getItemMeta();
                        meta.setDisplayName(ChatColor.LIGHT_PURPLE + oc.getName());
                        lore.add(ChatColor.ITALIC + "Shadowkin");
                        lore.add(" ");
                        break;
                    default:
                        slot = new ItemStack(Material.BLACK_CONCRETE);
                        meta = slot.getItemMeta();
                        meta.setDisplayName(ChatColor.WHITE + oc.getName());
                        lore.add(ChatColor.WHITE + "Could not detect race");
                        lore.add(" ");
                        break;
                }

                lore.add(ChatColor.GRAY + "Level: " + oc.getLevel());
                lore.add(ChatColor.GRAY + "XP: " + (int)oc.getXP());
                lore.add(ChatColor.GRAY + "Timed Played: " + secondsToTime(oc.getTimePlayed()));
                meta.setLore(lore);
                slot.setItemMeta(meta);
                gui.setItem(i,slot);
            } else {
                int timeNeeded=i*32400;
                if(time>=timeNeeded) {
                    ItemStack slot = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                    ItemMeta meta = slot.getItemMeta();
                    meta.setDisplayName(ChatColor.DARK_GREEN + "You can create a new OC!");
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GREEN + "Total Time Needed: " + secondsToTime(timeNeeded));
                    meta.setLore(lore);
                    slot.setItemMeta(meta);
                    gui.setItem(i,slot);
                } else {
                    ItemStack slot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                    ItemMeta meta = slot.getItemMeta();
                    meta.setDisplayName(ChatColor.DARK_RED + "This OC slot is locked!");
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + "Total Time Needed: " + secondsToTime(timeNeeded));
                    lore.add(ChatColor.RED + "Time Left: " + secondsToTime(timeNeeded-time));
                    meta.setLore(lore);
                    slot.setItemMeta(meta);
                    gui.setItem(i,slot);
                }
            }
        }

        //Create OC
        ItemStack slot8 = new ItemStack(Material.LIME_WOOL);
        ItemMeta slot8meta = slot8.getItemMeta();
        slot8meta.setDisplayName(ChatColor.GREEN + "Create new OC");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "Max of 3 OCs");
        lore.add(ChatColor.GRAY + "You may not create");
        lore.add(ChatColor.GRAY + "OCs of a specific race");
        lore.add(ChatColor.GRAY + "for a single battle");
        lore.add(" ");
        lore.add(ChatColor.GRAY + "To delete an OC, do");
        lore.add(ChatColor.GRAY + "/oc delete <OCName> <OCName>");
        lore.add(ChatColor.GRAY + "This is irreversible.");
        slot8meta.setLore(lore);
        slot8.setItemMeta(slot8meta);
        gui.setItem(8,slot8);

        return gui;
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void inventoryClick (InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.BLACK + "Character Selection")) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            int slot = e.getSlot();
            ArrayList<OC> OCs = system.getHandler().loadOCs(p);
            if (slot<OCs.size()) {
                OC oc = OCs.get(slot);

                if (oc.getRace()==Race.DRAGONBORN) {
                    if (!(p.getInventory().getChestplate()==null || p.getInventory().getChestplate().getType().equals(Material.ELYTRA))) {
                        p.sendMessage("Remove your chestplate before switching to dragonborn");
                        return;
                    }
                }

                if (system.getOCMap().containsKey(p)) {
                    system.getHandler().saveOC(system.getOC(p));
                    system.removeFromMap(p);
                }
                system.putToMap(p, oc);
                p.sendMessage(ChatColor.YELLOW + "You have selected a new OC!");
                system.removeFromCreation(p);
                p.closeInventory();
            } else if (slot==8) {
                int timeNeeded=OCs.size()*32400;
                int time = system.getHandler().getTimePlayed(p);
                if (time>=timeNeeded) {
                    if (OCs.size()<3) {
                        system.removeFromCreation(p);
                        p.closeInventory();
                        system.addCreating(p, invType.RACE);
                        //RaceSelectionGUI rsgui = new RaceSelectionGUI();
                        //p.openInventory(rsgui.NekoGUI);
                        ModernizedRaceSelection rsgui = new ModernizedRaceSelection();
                        p.openInventory(rsgui.raceSelectionSmall());
                    } else {
                        p.sendMessage(ChatColor.RED + "You can only have 3 OCs! ");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "You need a total playtime of " + (timeNeeded/3600) + " hours to make another OC!");
                }
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
}
