package me.voleia.volands.GUIs;

import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.Land;
import me.angeschossen.lands.api.nation.Nation;
import me.angeschossen.lands.api.player.LandPlayer;
import me.voleia.volands.Commands.ChatCommand;
import me.voleia.volands.OC;
import me.voleia.volands.Volands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TitleGUI {

    Volands system = Volands.getSystem();
    LandsIntegration lapi = system.getIntegration();
    ArrayList<String> ownedTitles = new ArrayList<>();
    ArrayList<String> ownedSuffixes = new ArrayList<>();
    LandPlayer player;
    Player p;
    boolean male=true;

    int pI=0;
    int sI=0;

    public TitleGUI(Player p_) {
        ownedTitles.add("Blank;Blank");
        ownedSuffixes.add("Blank");
        p = p_;
        player = lapi.getLandPlayer(p.getUniqueId());
        ArrayList<Land> lands = new ArrayList<>(player.getLands());
        for (Land land : lands) {
            if (land.getOwnerUID().equals(p.getUniqueId())) { //if the land is owned by the player
                ownedTitles.addAll(system.getConfig().getStringList("SmallLandTitles"));
                int size = land.getTrustedPlayers().size();
                if (size >= 3) {
                    ownedTitles.addAll(system.getConfig().getStringList("MedLandTitles"));
                    if (size >= 9) {
                        ownedTitles.addAll(system.getConfig().getStringList("LargeLandTitles"));
                    }
                }
            }
            ownedSuffixes.add("of " + land.getName());

            Nation nation = land.getNation();
            if (nation != null) {
                ownedSuffixes.add("of " + nation.getName());
                if (nation.getOwnerUID().equals(p.getUniqueId())) {
                    ownedTitles.addAll(system.getConfig().getStringList("SmallNationTitles"));
                    int size = nation.getLands().size();
                    if (size >= 3) {
                        ownedTitles.addAll(system.getConfig().getStringList("MedNationTitles"));
                        if (size >= 9) {
                            ownedTitles.addAll(system.getConfig().getStringList("LargeNationTitles"));
                        }
                    }
                }
            }
        }



        /*for (Land land : lands) {
            if (land.getOwnerUID().equals(p.getUniqueId())) { //if the land is owned by the player
                ownedTitles.add("Baron;Baroness");
                ownedTitles.add("Mayor;Mayor");
                ownedTitles.add("Chief;Chief");
                int size = land.getTrustedPlayers().size();
                if (size >= 3) {
                    ownedTitles.add("Count;Countess");
                    ownedTitles.add("Emir;Emira");
                    ownedTitles.add("Ealdorman;Ealdorwoman");
                    if (size >= 9) {
                        ownedTitles.add("Duke;Duchess");
                        ownedTitles.add("Chieftain;Chieftess");
                    }
                }
            }
            ownedSuffixes.add("of " + land.getName());
            Nation nation = land.getNation();
            if (nation != null) {
                ownedSuffixes.add("of " + nation.getName());
                if (nation.getOwnerUID().equals(p.getUniqueId())) {
                    ownedTitles.add("Grand-Duke;Grand-Duchess");
                    ownedTitles.add("Petty-King;Petty-Queen");
                    int size = nation.getLands().size();
                    if (size >= 3) {
                        ownedTitles.add("King;Queen");
                        ownedTitles.add("Shogun;Shogun");
                        ownedTitles.add("Malek;Malikah");
                        ownedTitles.add("Khan;Khatun");
                        ownedTitles.add("Rex;Regina");
                        if (size >= 9) {
                            ownedTitles.add("Emperor;Empress");
                            ownedTitles.add("High-King;High-Queen");
                            ownedTitles.add("Great-Khan;Great-Khatun");
                            ownedTitles.add("Sultan;Sultana");
                            ownedTitles.add("Basileus;Basileia");
                        }
                    }
                }
            }
        }*/

        OC oc = system.getOC(p);
        String curpre = oc.getPrefix();
        String cursuf = oc.getSuffix();
        for (int i = 0; i < ownedTitles.size(); i+=1) {
            if (ownedTitles.get(i).substring(0,ownedTitles.get(i).indexOf(";")).equals(curpre) || ownedTitles.get(i).substring(ownedTitles.get(i).indexOf(";")+1).equals(curpre)) {
                pI=i;
            }
        }
        for (int i = 0; i < ownedSuffixes.size(); i+=1) {
            if (ownedSuffixes.get(i).equals(cursuf)) {
                sI=i;
            }
        }
        Inventory tsgui = titleSelectionGui();
        p.openInventory(tsgui);
    }

    public Inventory titleSelectionGui() {
        Inventory gui = Bukkit.createInventory(null,9, ChatColor.BLACK + "Noble Title Selection");
        if (pI>=ownedTitles.size()) {
            pI=0;
        } else if (pI<0) {
            pI=ownedTitles.size()-1;
        }

        if (sI>=ownedSuffixes.size()) {
            sI=0;
        } else if (sI<0) {
            sI=ownedSuffixes.size()-1;
        }

        ItemStack blankStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

        ItemStack slot1 = new ItemStack(Material.SPECTRAL_ARROW);
        ItemMeta slot1meta = slot1.getItemMeta();
        String prefix = ownedTitles.get(pI);
        if (male) {
            prefix=prefix.substring(0,prefix.indexOf(";"));
        } else {
            prefix=prefix.substring(prefix.indexOf(";")+1);
        }
        slot1meta.setDisplayName(ChatColor.WHITE +"Prefix: " + prefix);
        List<String> lore = new ArrayList<>();
        int size = ownedTitles.size();
        for (int i = pI+1; i < size; i+=1) {
            String cur=ownedTitles.get(i);
            if (male) {
                cur=cur.substring(0,cur.indexOf(";"));
            } else {
                cur=cur.substring(cur.indexOf(";")+1);
            }
            lore.add(cur);
        }
        for (int i = 0; i<pI; i+=1) {
            String cur=ownedTitles.get(i);
            if (male) {
                cur=cur.substring(0,cur.indexOf(";"));
            } else {
                cur=cur.substring(cur.indexOf(";")+1);
            }
            lore.add(cur);        }
        slot1meta.setLore(lore);
        slot1.setItemMeta(slot1meta);

        ItemStack slot3 = new ItemStack(Material.ARROW);
        ItemMeta slot3meta = slot3.getItemMeta();
        slot3meta.setDisplayName(ChatColor.WHITE + "Suffix: "+ownedSuffixes.get(sI));
        List<String> lore2 = new ArrayList<>();
        int size2 = ownedSuffixes.size();
        for (int i = sI+1; i<size2; i+=1) {
            lore2.add(ownedSuffixes.get(i));
        }
        for (int i = 0; i<sI; i+=1) {
            lore2.add(ownedSuffixes.get(i));
        }
        slot3meta.setLore(lore2);
        slot3.setItemMeta(slot3meta);

        ItemStack slot8 = new ItemStack(Material.LIME_WOOL);
        ItemMeta slot8meta = slot8.getItemMeta();
        slot8meta.setDisplayName(ChatColor.GREEN + "Confirm Title");
        slot8.setItemMeta(slot8meta);

        ItemStack slot6;
        if (male) {
            slot6 = new ItemStack(Material.LIGHT_BLUE_WOOL);
            ItemMeta slot6meta = slot6.getItemMeta();
            slot6meta.setDisplayName(ChatColor.WHITE + "Gendered Titles: Male");
            slot6.setItemMeta(slot6meta);
        } else {
            slot6 = new ItemStack(Material.PINK_WOOL);
            ItemMeta slot6meta = slot6.getItemMeta();
            slot6meta.setDisplayName(ChatColor.WHITE + "Gendered Titles: Female");
            slot6.setItemMeta(slot6meta);
        }
        gui.setItem(0,blankStack);
        gui.setItem(1,slot1);
        gui.setItem(2,blankStack);
        gui.setItem(3,slot3);
        gui.setItem(4,blankStack);
        gui.setItem(5,blankStack);
        gui.setItem(6,slot6);
        gui.setItem(7,blankStack);
        gui.setItem(8,slot8);
        return gui;
    }

    public void confirm() {
        String prefix = ownedTitles.get(pI);
        String suffix = ownedSuffixes.get(sI);
        if (male) {
            prefix=prefix.substring(0,prefix.indexOf(";"));
        } else {
            prefix=prefix.substring(prefix.indexOf(";")+1);
        }
        OC oc = system.getOC(p);
        if (suffix.equals("Blank") || prefix.equals("Blank")) {
            oc.setPrefix(prefix);
            oc.setSuffix(suffix);
            p.sendMessage(ChatColor.GREEN + "Set your titles!");
        } else {
            boolean ownedLand = false;
            Land land = lapi.getLandByName(suffix.substring(suffix.indexOf(" ")+1));
            if (land==null) {
                Nation nation = lapi.getNationByName(suffix.substring(suffix.indexOf(" ")+1));
                if (nation==null || nation.getOwnerUID().equals(p.getUniqueId())) {
                    ownedLand=true;
                }
            } else {
                if (land.getOwnerUID().equals(p.getUniqueId())) {
                    ownedLand=true;
                }
            }
            if (ownedLand) {
                oc.setPrefix(prefix);
                oc.setSuffix(suffix);
                p.sendMessage(ChatColor.GREEN + "Set your titles!");
                p.closeInventory();
                system.removeTitle(p);
            } else {
                p.sendMessage(ChatColor.RED + "Did not set your title: If you have a rank as your prefix, your suffix must be a town that you own!");
            }
        }
    }

    public void clickPrefix(boolean leftclick) {
        if (leftclick) {
            pI+=1;
        } else {
            pI-=1;
        }
        Inventory tsgui = titleSelectionGui();
        p.openInventory(tsgui);
        system.putTitle(p,this);
    }

    public void clickGender() {
        male=!male;
        Inventory tsgui = titleSelectionGui();
        p.openInventory(tsgui);
        system.putTitle(p,this);
    }

    public void clickSuffix(boolean leftclick) {
        if (leftclick) {
            sI+=1;
        } else {
            sI-=1;
        }
        Inventory tsgui = titleSelectionGui();
        p.openInventory(tsgui);
        system.putTitle(p,this);
    }
}
