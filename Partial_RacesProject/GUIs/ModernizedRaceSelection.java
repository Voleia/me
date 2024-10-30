package me.voleia.volands.GUIs;

import jdk.tools.jlink.internal.DirArchive;
import me.voleia.volands.Enums.ExpType;
import me.voleia.volands.Enums.Race;
import me.voleia.volands.OC;
import me.voleia.volands.Races.*;
import me.voleia.volands.Volands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ModernizedRaceSelection implements Listener {

    ArrayList<Race> raceOrder = new ArrayList<>();
    public ModernizedRaceSelection() {
        raceOrder.add(Race.NEKO);
        raceOrder.add(Race.DRAGONBORN);
        raceOrder.add(Race.AMBYSTOMA);
        raceOrder.add(Race.ELF);
        raceOrder.add(Race.DEMON);
        raceOrder.add(Race.DARKELF);
        raceOrder.add(Race.WRAITH);
        raceOrder.add(Race.DWARF);
        raceOrder.add(Race.HUMAN);
    }

    public Inventory raceSelectionSmall() {
        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE376");
        for (int i = 0; i < 9; i+=1) {
            ItemStack slot1 = new ItemStack(Material.BARRIER);
            ItemMeta slot1meta = slot1.getItemMeta();
            slot1meta.setDisplayName(ChatColor.WHITE + system.getRaceIcon(raceOrder.get(i)));
            slot1meta.setCustomModelData(1);
            slot1meta.setLore(getRaceInfo(getInstance(raceOrder.get(i)),raceOrder.get(i)));
            slot1.setItemMeta(slot1meta);

            gui.setItem(i,slot1);
        }
        return gui;
    }

    public void SelectRace(Race race, Player p)
    {
        system.removeFromCreation(p);
        system.addSelecting(p, race);
        p.sendMessage(ChatColor.YELLOW + "You have selected your race! Now, type a name for your character in chat");
        p.sendMessage(ChatColor.GREEN + "This name can be anything you want between 3 and 16 characters, and it can be changed later. You can also just type your username.");
        system.sendSelectingMessages();
        p.closeInventory();
    }

    Volands system = Volands.getSystem();
    
    List<String> getRaceInfo(RaceClass cur, Race r) {
        List<String> ret = new ArrayList<>();
        ret.add(ChatColor.RED + "CLICK TO SELECT");
        ret.add(ChatColor.YELLOW+system.translate("==[ " + r.toString() + " Race Information ]=="));
        for (String str : cur.getInfo()) {
            ret.add(ChatColor.GRAY+str);
        }
        ret.add(ChatColor.GOLD+"Press 'Shift+F' to use active abilities");
        return ret;
    }

    RaceClass getInstance(Race race) {
        switch (race) {
            case AMBYSTOMA:
                return new Ambystoma();
            case DEMON:
                return new Demon();
            case DRAGONBORN:
                return new Dragonborn();
            case DWARF:
                return new Dwarf();
            case ELF:
                return new Elf();
            case HUMAN:
                return new Human();
            case NEKO:
                return new Neko();
            case DARKELF:
                return new Shadowkin();
            case WRAITH:
                return new Wraith();
        }
        return null;
    }

    @EventHandler
    public void invint(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE376")) {
            e.setCancelled(true);
            if (e.getSlot()<=8) {
                SelectRace(raceOrder.get(e.getSlot()),(Player) e.getWhoClicked());
            }
        }
    }


}
