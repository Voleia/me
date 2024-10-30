package me.voleia.volands.GUIs;

import me.voleia.volands.Enums.Race;
import me.voleia.volands.Volands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RaceSelectionGUI implements Listener {

    Volands system = Volands.getSystem();

    public Inventory NekoGUI = NekoGuiCreator();
    public Inventory DragonbornGUI = DragonbornGuiCreator();
    public Inventory AmbystomaGUI = AmbystomaGuiCreator();
    public Inventory ElfGUI = ElfGuiCreator();
    public Inventory DemonGUI = DemonGuiCreator();
    public Inventory ShadowkinGUI = ShadowkinGuiCreator();
    public Inventory WraithGUI = WraithGuiCreator();
    public Inventory DwarfGUI = DwarfGuiCreator();
    public Inventory HumanGUI = HumanGuiCreator();


    public static Inventory NekoGuiCreator()
    {
        //Creating the varriable gui within an Inventory type
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE370");


        //Makes The ItemStacks
        //slot 1
        ItemStack slot1 = new ItemStack(Material.BARRIER);
        ItemMeta slot1meta = slot1.getItemMeta();
        slot1meta.setDisplayName(ChatColor.WHITE + "\uE00E");
        slot1meta.setCustomModelData(1);
        slot1.setItemMeta(slot1meta);
        //slot 2
        ItemStack slot2 = new ItemStack(Material.BARRIER);
        ItemMeta slot2meta = slot2.getItemMeta();
        slot2meta.setDisplayName(ChatColor.WHITE + "\uE005");
        slot2meta.setCustomModelData(1);
        slot2.setItemMeta(slot2meta);
        //slot 3
        ItemStack slot3 = new ItemStack(Material.BARRIER);
        ItemMeta slot3meta = slot3.getItemMeta();
        slot3meta.setDisplayName(ChatColor.WHITE + "\uE00D");
        slot3meta.setCustomModelData(1);
        slot3.setItemMeta(slot3meta);
        //slot 4
        ItemStack slot4 = new ItemStack(Material.BARRIER);
        ItemMeta slot4meta = slot4.getItemMeta();
        slot4meta.setDisplayName(ChatColor.WHITE + "\uE003");
        slot4meta.setCustomModelData(1);
        slot4.setItemMeta(slot4meta);
        //slot 5
        ItemStack slot5 = new ItemStack(Material.BARRIER);
        ItemMeta slot5meta = slot5.getItemMeta();
        slot5meta.setDisplayName(ChatColor.WHITE + "\uE00A");
        slot5meta.setCustomModelData(1);
        slot5.setItemMeta(slot5meta);
        //slot 6
        ItemStack slot6 = new ItemStack(Material.BARRIER);
        ItemMeta slot6meta = slot6.getItemMeta();
        slot6meta.setDisplayName(ChatColor.WHITE + "\uE002");
        slot6meta.setCustomModelData(1);
        slot6.setItemMeta(slot6meta);
        //slot 7
        ItemStack slot7 = new ItemStack(Material.BARRIER);
        ItemMeta slot7meta = slot7.getItemMeta();
        slot7meta.setDisplayName(ChatColor.WHITE + "\uE001");
        slot7meta.setCustomModelData(1);
        slot7.setItemMeta(slot7meta);
        //slot 8
        ItemStack slot8 = new ItemStack(Material.BARRIER);
        ItemMeta slot8meta = slot8.getItemMeta();
        slot8meta.setDisplayName(ChatColor.WHITE + "\uE00B");
        slot8meta.setAttributeModifiers(null);
        slot8meta.setCustomModelData(1);
        slot8.setItemMeta(slot8meta);

        // 48-45

        //slot 45
        ItemStack slot45 = new ItemStack(Material.BARRIER);
        ItemMeta slot45meta = slot45.getItemMeta();
        slot45meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot45meta.setCustomModelData(1);
        slot45.setItemMeta(slot45meta);

        ItemStack slot46 = new ItemStack(Material.BARRIER);
        ItemMeta slot46meta = slot46.getItemMeta();
        slot46meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot46meta.setCustomModelData(1);
        slot46.setItemMeta(slot46meta);

        ItemStack slot47 = new ItemStack(Material.BARRIER);
        ItemMeta slot47meta = slot47.getItemMeta();
        slot47meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot47meta.setCustomModelData(1);
        slot47.setItemMeta(slot47meta);

        ItemStack slot48 = new ItemStack(Material.BARRIER);
        ItemMeta slot48meta = slot48.getItemMeta();
        slot48meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot48meta.setCustomModelData(1);
        slot48.setItemMeta(slot48meta);

        ItemStack slot34 = new ItemStack(Material.STICK);
        ItemMeta slot34meta = slot34.getItemMeta();
        slot34meta.setDisplayName(ChatColor.WHITE + "\uE00C");
        List<String> lore = new ArrayList<>();
        lore.add(net.md_5.bungee.api.ChatColor.of("#ba0746") + "level 1");
        lore.add(net.md_5.bungee.api.ChatColor.of("#fc4988") + "* Climb Walls (Shift)");
        lore.add(net.md_5.bungee.api.ChatColor.of("#ba0746") + "level 2");
        lore.add(net.md_5.bungee.api.ChatColor.of("#fc4988") + "* Night Vision in Jungles");
        lore.add(net.md_5.bungee.api.ChatColor.of("#fc4988") + "* Eating fish gives regen");
        lore.add(net.md_5.bungee.api.ChatColor.of("#ba0746") + "level 3");
        lore.add(net.md_5.bungee.api.ChatColor.of("#fc4988") + "* Creepers don't attack");
        lore.add(net.md_5.bungee.api.ChatColor.of("#fc4988") + "* Double Jump (press F in air)");
        slot34meta.setLore(lore);
        slot34meta.setCustomModelData(8);
        slot34.setItemMeta(slot34meta);

        gui.setItem(1, slot1);
        gui.setItem(2, slot2);
        gui.setItem(3, slot3);
        gui.setItem(4, slot4);
        gui.setItem(5, slot5);
        gui.setItem(6, slot6);
        gui.setItem(7, slot7);
        gui.setItem(8, slot8);
        gui.setItem(45, slot45);
        gui.setItem(46, slot46);
        gui.setItem(47, slot47);
        gui.setItem(48, slot48);
        gui.setItem(34, slot34);

        //returns the gui
        return gui;


    }
    public static Inventory DragonbornGuiCreator()
    {
        //Creating the varriable gui within an Inventory type
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE371");


        //Makes The ItemStacks
        //slot 0
        ItemStack slot0 = new ItemStack(Material.BARRIER);
        ItemMeta slot0meta = slot0.getItemMeta();
        slot0meta.setDisplayName(ChatColor.WHITE + "\uE00C");
        slot0meta.setCustomModelData(1);
        slot0.setItemMeta(slot0meta);
        //slot 2
        ItemStack slot2 = new ItemStack(Material.BARRIER);
        ItemMeta slot2meta = slot2.getItemMeta();
        slot2meta.setDisplayName(ChatColor.WHITE + "\uE005");
        slot2meta.setCustomModelData(1);
        slot2.setItemMeta(slot2meta);
        //slot 3
        ItemStack slot3 = new ItemStack(Material.BARRIER);
        ItemMeta slot3meta = slot3.getItemMeta();
        slot3meta.setDisplayName(ChatColor.WHITE + "\uE00D");
        slot3meta.setCustomModelData(1);
        slot3.setItemMeta(slot3meta);
        //slot 4
        ItemStack slot4 = new ItemStack(Material.BARRIER);
        ItemMeta slot4meta = slot4.getItemMeta();
        slot4meta.setDisplayName(ChatColor.WHITE + "\uE003");
        slot4meta.setCustomModelData(1);
        slot4.setItemMeta(slot4meta);
        //slot 5
        ItemStack slot5 = new ItemStack(Material.BARRIER);
        ItemMeta slot5meta = slot5.getItemMeta();
        slot5meta.setDisplayName(ChatColor.WHITE + "\uE00A");
        slot5meta.setCustomModelData(1);
        slot5.setItemMeta(slot5meta);
        //slot 6
        ItemStack slot6 = new ItemStack(Material.BARRIER);
        ItemMeta slot6meta = slot6.getItemMeta();
        slot6meta.setDisplayName(ChatColor.WHITE + "\uE002");
        slot6meta.setCustomModelData(1);
        slot6.setItemMeta(slot6meta);
        //slot 7
        ItemStack slot7 = new ItemStack(Material.BARRIER);
        ItemMeta slot7meta = slot7.getItemMeta();
        slot7meta.setDisplayName(ChatColor.WHITE + "\uE001");
        slot7meta.setCustomModelData(1);
        slot7.setItemMeta(slot7meta);
        //slot 8
        ItemStack slot8 = new ItemStack(Material.BARRIER);
        ItemMeta slot8meta = slot8.getItemMeta();
        slot8meta.setDisplayName(ChatColor.WHITE + "\uE00B");
        slot8meta.setAttributeModifiers(null);
        slot8meta.setCustomModelData(1);
        slot8.setItemMeta(slot8meta);

        // 48-45

        //slot 45
        ItemStack slot45 = new ItemStack(Material.BARRIER);
        ItemMeta slot45meta = slot45.getItemMeta();
        slot45meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot45meta.setCustomModelData(1);
        slot45.setItemMeta(slot45meta);

        ItemStack slot46 = new ItemStack(Material.BARRIER);
        ItemMeta slot46meta = slot46.getItemMeta();
        slot46meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot46meta.setCustomModelData(1);
        slot46.setItemMeta(slot46meta);

        ItemStack slot47 = new ItemStack(Material.BARRIER);
        ItemMeta slot47meta = slot47.getItemMeta();
        slot47meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot47meta.setCustomModelData(1);
        slot47.setItemMeta(slot47meta);

        ItemStack slot48 = new ItemStack(Material.BARRIER);
        ItemMeta slot48meta = slot48.getItemMeta();
        slot48meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot48meta.setCustomModelData(1);
        slot48.setItemMeta(slot48meta);

        ItemStack slot34 = new ItemStack(Material.STICK);
        ItemMeta slot34meta = slot34.getItemMeta();
        slot34meta.setDisplayName(ChatColor.WHITE + "\uE00E");
        List<String> lore = new ArrayList<>();
        lore.add(net.md_5.bungee.api.ChatColor.of("#a80000") + "level 1");
        lore.add(net.md_5.bungee.api.ChatColor.of("#d62f2f") + "* Dragon Wings");
        lore.add(net.md_5.bungee.api.ChatColor.of("#d62f2f") + "* Slowness");
        lore.add(net.md_5.bungee.api.ChatColor.of("#a80000") + "level 2");
        lore.add(net.md_5.bungee.api.ChatColor.of("#d62f2f") + "* Stay Underwater 2x Longer");
        lore.add(net.md_5.bungee.api.ChatColor.of("#d62f2f") + "* Regeneration 1");
        lore.add(net.md_5.bungee.api.ChatColor.of("#d62f2f") + "* Flight Boost Ability");
        lore.add(net.md_5.bungee.api.ChatColor.of("#a80000") + "level 3");
        lore.add(net.md_5.bungee.api.ChatColor.of("#d62f2f") + "* Let players mount you");
        lore.add(net.md_5.bungee.api.ChatColor.of("#d62f2f") + "* Resistance 1");

        slot34meta.setLore(lore);
        slot34meta.setCustomModelData(9);
        slot34.setItemMeta(slot34meta);

        gui.setItem(0, slot0);
        gui.setItem(2, slot2);
        gui.setItem(3, slot3);
        gui.setItem(4, slot4);
        gui.setItem(5, slot5);
        gui.setItem(6, slot6);
        gui.setItem(7, slot7);
        gui.setItem(8, slot8);
        gui.setItem(45, slot45);
        gui.setItem(46, slot46);
        gui.setItem(47, slot47);
        gui.setItem(48, slot48);
        gui.setItem(34, slot34);



        //returns the gui
        return gui;


    }
    public static Inventory AmbystomaGuiCreator()
    {
        //Creating the varriable gui within an Inventory type
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE372");


        //Makes The ItemStacks
        //slot 0
        ItemStack slot0 = new ItemStack(Material.BARRIER);
        ItemMeta slot0meta = slot0.getItemMeta();
        slot0meta.setDisplayName(ChatColor.WHITE + "\uE00C");
        slot0meta.setCustomModelData(1);
        slot0.setItemMeta(slot0meta);
        //slot 1
        ItemStack slot1 = new ItemStack(Material.BARRIER);
        ItemMeta slot1meta = slot1.getItemMeta();
        slot1meta.setDisplayName(ChatColor.WHITE + "\uE00E");
        slot1meta.setCustomModelData(1);
        slot1.setItemMeta(slot1meta);
        //slot 2
        ItemStack slot2 = new ItemStack(Material.BARRIER);
        ItemMeta slot2meta = slot2.getItemMeta();
        slot2meta.setDisplayName(ChatColor.WHITE + "\uE005");
        slot2meta.setCustomModelData(1);
        slot2.setItemMeta(slot2meta);
        //slot 3
        ItemStack slot3 = new ItemStack(Material.BARRIER);
        ItemMeta slot3meta = slot3.getItemMeta();
        slot3meta.setDisplayName(ChatColor.WHITE + "\uE00D");
        slot3meta.setCustomModelData(1);
        slot3.setItemMeta(slot3meta);
        //slot 4
        ItemStack slot4 = new ItemStack(Material.BARRIER);
        ItemMeta slot4meta = slot4.getItemMeta();
        slot4meta.setDisplayName(ChatColor.WHITE + "\uE003");
        slot4meta.setCustomModelData(1);
        slot4.setItemMeta(slot4meta);
        //slot 5
        ItemStack slot5 = new ItemStack(Material.BARRIER);
        ItemMeta slot5meta = slot5.getItemMeta();
        slot5meta.setDisplayName(ChatColor.WHITE + "\uE00A");
        slot5meta.setCustomModelData(1);
        slot5.setItemMeta(slot5meta);
        //slot 6
        ItemStack slot6 = new ItemStack(Material.BARRIER);
        ItemMeta slot6meta = slot6.getItemMeta();
        slot6meta.setDisplayName(ChatColor.WHITE + "\uE002");
        slot6meta.setCustomModelData(1);
        slot6.setItemMeta(slot6meta);
        //slot 7
        ItemStack slot7 = new ItemStack(Material.BARRIER);
        ItemMeta slot7meta = slot7.getItemMeta();
        slot7meta.setDisplayName(ChatColor.WHITE + "\uE001");
        slot7meta.setCustomModelData(1);
        slot7.setItemMeta(slot7meta);
        //slot 8
        ItemStack slot8 = new ItemStack(Material.BARRIER);
        ItemMeta slot8meta = slot8.getItemMeta();
        slot8meta.setDisplayName(ChatColor.WHITE + "\uE00B");
        slot8meta.setAttributeModifiers(null);
        slot8meta.setCustomModelData(1);
        slot8.setItemMeta(slot8meta);

        // 48-45

        //slot 45
        ItemStack slot45 = new ItemStack(Material.BARRIER);
        ItemMeta slot45meta = slot45.getItemMeta();
        slot45meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot45meta.setCustomModelData(1);
        slot45.setItemMeta(slot45meta);

        ItemStack slot46 = new ItemStack(Material.BARRIER);
        ItemMeta slot46meta = slot46.getItemMeta();
        slot46meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot46meta.setCustomModelData(1);
        slot46.setItemMeta(slot46meta);

        ItemStack slot47 = new ItemStack(Material.BARRIER);
        ItemMeta slot47meta = slot47.getItemMeta();
        slot47meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot47meta.setCustomModelData(1);
        slot47.setItemMeta(slot47meta);

        ItemStack slot48 = new ItemStack(Material.BARRIER);
        ItemMeta slot48meta = slot48.getItemMeta();
        slot48meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot48meta.setCustomModelData(1);
        slot48.setItemMeta(slot48meta);

        ItemStack slot34 = new ItemStack(Material.STICK);
        ItemMeta slot34meta = slot34.getItemMeta();
        slot34meta.setDisplayName(ChatColor.WHITE + "\uE005");
        List<String> lore = new ArrayList<>();
        lore.add(net.md_5.bungee.api.ChatColor.of("#00a89d") + "level 1");
        lore.add(net.md_5.bungee.api.ChatColor.of("#4afff3") + "* Water Breathing");
        lore.add(net.md_5.bungee.api.ChatColor.of("#4afff3") + "* Regen Under Water");
        lore.add(net.md_5.bungee.api.ChatColor.of("#00a89d") + "level 2");
        lore.add(net.md_5.bungee.api.ChatColor.of("#4afff3") + "* Night Vision underwater");
        lore.add(net.md_5.bungee.api.ChatColor.of("#00a89d") + "level 3");
        lore.add(net.md_5.bungee.api.ChatColor.of("#4afff3") + "* Conduit");
        lore.add(net.md_5.bungee.api.ChatColor.of("#4afff3") + "* Dolphin's Grace");
        lore.add(net.md_5.bungee.api.ChatColor.of("#4afff3") + "* Strength underwater");

        slot34meta.setLore(lore);
        slot34meta.setCustomModelData(10);
        slot34.setItemMeta(slot34meta);

        gui.setItem(0, slot0);
        gui.setItem(1, slot1);
        gui.setItem(3, slot3);
        gui.setItem(4, slot4);
        gui.setItem(5, slot5);
        gui.setItem(6, slot6);
        gui.setItem(7, slot7);
        gui.setItem(8, slot8);
        gui.setItem(45, slot45);
        gui.setItem(46, slot46);
        gui.setItem(47, slot47);
        gui.setItem(48, slot48);
        gui.setItem(34, slot34);



        //returns the gui
        return gui;


    }
    public static Inventory ElfGuiCreator()
    {
        //Creating the varriable gui within an Inventory type
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE373");


        //Makes The ItemStacks
        //slot 0
        ItemStack slot0 = new ItemStack(Material.BARRIER);
        ItemMeta slot0meta = slot0.getItemMeta();
        slot0meta.setDisplayName(ChatColor.WHITE + "\uE00C");
        slot0meta.setCustomModelData(1);
        slot0.setItemMeta(slot0meta);
        //slot 1
        ItemStack slot1 = new ItemStack(Material.BARRIER);
        ItemMeta slot1meta = slot1.getItemMeta();
        slot1meta.setDisplayName(ChatColor.WHITE + "\uE00E");
        slot1meta.setCustomModelData(1);
        slot1.setItemMeta(slot1meta);
        //slot 2
        ItemStack slot2 = new ItemStack(Material.BARRIER);
        ItemMeta slot2meta = slot2.getItemMeta();
        slot2meta.setDisplayName(ChatColor.WHITE + "\uE005");// "\uE012"
        slot2meta.setCustomModelData(1);
        slot2.setItemMeta(slot2meta);
        //slot 3
        ItemStack slot3 = new ItemStack(Material.BARRIER);
        ItemMeta slot3meta = slot3.getItemMeta();
        slot3meta.setDisplayName(ChatColor.WHITE + "\uE00D");
        slot3meta.setCustomModelData(1);
        slot3.setItemMeta(slot3meta);
        //slot 4
        ItemStack slot4 = new ItemStack(Material.BARRIER);
        ItemMeta slot4meta = slot4.getItemMeta();
        slot4meta.setDisplayName(ChatColor.WHITE + "\uE003");
        slot4meta.setCustomModelData(1);
        slot4.setItemMeta(slot4meta);
        //slot 5
        ItemStack slot5 = new ItemStack(Material.BARRIER);
        ItemMeta slot5meta = slot5.getItemMeta();
        slot5meta.setDisplayName(ChatColor.WHITE + "\uE00A");
        slot5meta.setCustomModelData(1);
        slot5.setItemMeta(slot5meta);
        //slot 6
        ItemStack slot6 = new ItemStack(Material.BARRIER);
        ItemMeta slot6meta = slot6.getItemMeta();
        slot6meta.setDisplayName(ChatColor.WHITE + "\uE002");
        slot6meta.setCustomModelData(1);
        slot6.setItemMeta(slot6meta);
        //slot 7
        ItemStack slot7 = new ItemStack(Material.BARRIER);
        ItemMeta slot7meta = slot7.getItemMeta();
        slot7meta.setDisplayName(ChatColor.WHITE + "\uE001");
        slot7meta.setCustomModelData(1);
        slot7.setItemMeta(slot7meta);
        //slot 8
        ItemStack slot8 = new ItemStack(Material.BARRIER);
        ItemMeta slot8meta = slot8.getItemMeta();
        slot8meta.setDisplayName(ChatColor.WHITE + "\uE00B");
        slot8meta.setAttributeModifiers(null);
        slot8meta.setCustomModelData(1);
        slot8.setItemMeta(slot8meta);

        // 48-45

        //slot 45
        ItemStack slot45 = new ItemStack(Material.BARRIER);
        ItemMeta slot45meta = slot45.getItemMeta();
        slot45meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot45meta.setCustomModelData(1);
        slot45.setItemMeta(slot45meta);

        ItemStack slot46 = new ItemStack(Material.BARRIER);
        ItemMeta slot46meta = slot46.getItemMeta();
        slot46meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot46meta.setCustomModelData(1);
        slot46.setItemMeta(slot46meta);

        ItemStack slot47 = new ItemStack(Material.BARRIER);
        ItemMeta slot47meta = slot47.getItemMeta();
        slot47meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot47meta.setCustomModelData(1);
        slot47.setItemMeta(slot47meta);

        ItemStack slot48 = new ItemStack(Material.BARRIER);
        ItemMeta slot48meta = slot48.getItemMeta();
        slot48meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot48meta.setCustomModelData(1);
        slot48.setItemMeta(slot48meta);

        ItemStack slot34 = new ItemStack(Material.STICK);
        ItemMeta slot34meta = slot34.getItemMeta();
        slot34meta.setDisplayName(ChatColor.WHITE + "\uE00D");
        List<String> lore = new ArrayList<>();
        lore.add(net.md_5.bungee.api.ChatColor.of("#c9a40e") + "level 1");
        lore.add(net.md_5.bungee.api.ChatColor.of("#ffdc4f") + "* Speed 1");
        lore.add(net.md_5.bungee.api.ChatColor.of("#c9a40e") + "level 2");
        lore.add(net.md_5.bungee.api.ChatColor.of("#ffdc4f") + "* 50% Less Fall Damage");
        lore.add(net.md_5.bungee.api.ChatColor.of("#c9a40e") + "level 3");
        lore.add(net.md_5.bungee.api.ChatColor.of("#ffdc4f") + "* Infinite Arrows");
        lore.add(net.md_5.bungee.api.ChatColor.of("#ffdc4f") + "* Dash Ability");
        slot34meta.setLore(lore);
        slot34meta.setCustomModelData(11);
        slot34.setItemMeta(slot34meta);

        gui.setItem(0, slot0);
        gui.setItem(1, slot1);
        gui.setItem(2, slot2);
        gui.setItem(4, slot4);
        gui.setItem(5, slot5);
        gui.setItem(6, slot6);
        gui.setItem(7, slot7);
        gui.setItem(8, slot8);
        gui.setItem(45, slot45);
        gui.setItem(46, slot46);
        gui.setItem(47, slot47);
        gui.setItem(48, slot48);
        gui.setItem(34, slot34);



        //returns the gui
        return gui;


    }
    public static Inventory DemonGuiCreator()
    {
        //Creating the varriable gui within an Inventory type
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE374");


        //Makes The ItemStacks
        //slot 0
        ItemStack slot0 = new ItemStack(Material.BARRIER);
        ItemMeta slot0meta = slot0.getItemMeta();
        slot0meta.setDisplayName(ChatColor.WHITE + "\uE00C");
        slot0meta.setCustomModelData(1);
        slot0.setItemMeta(slot0meta);
        //slot 1
        ItemStack slot1 = new ItemStack(Material.BARRIER);
        ItemMeta slot1meta = slot1.getItemMeta();
        slot1meta.setDisplayName(ChatColor.WHITE + "\uE00E");
        slot1meta.setCustomModelData(1);
        slot1.setItemMeta(slot1meta);
        //slot 2
        ItemStack slot2 = new ItemStack(Material.BARRIER);
        ItemMeta slot2meta = slot2.getItemMeta();
        slot2meta.setDisplayName(ChatColor.WHITE + "\uE005");
        slot2meta.setCustomModelData(1);
        slot2.setItemMeta(slot2meta);
        //slot 3
        ItemStack slot3 = new ItemStack(Material.BARRIER);
        ItemMeta slot3meta = slot3.getItemMeta();
        slot3meta.setDisplayName(ChatColor.WHITE + "\uE00D");
        slot3meta.setCustomModelData(1);
        slot3.setItemMeta(slot3meta);
        //slot 4
        ItemStack slot4 = new ItemStack(Material.BARRIER);
        ItemMeta slot4meta = slot4.getItemMeta();
        slot4meta.setDisplayName(ChatColor.WHITE + "\uE003");
        slot4meta.setCustomModelData(1);
        slot4.setItemMeta(slot4meta);
        //slot 5
        ItemStack slot5 = new ItemStack(Material.BARRIER);
        ItemMeta slot5meta = slot5.getItemMeta();
        slot5meta.setDisplayName(ChatColor.WHITE + "\uE00A");
        slot5meta.setCustomModelData(1);
        slot5.setItemMeta(slot5meta);
        //slot 6
        ItemStack slot6 = new ItemStack(Material.BARRIER);
        ItemMeta slot6meta = slot6.getItemMeta();
        slot6meta.setDisplayName(ChatColor.WHITE + "\uE002");
        slot6meta.setCustomModelData(1);
        slot6.setItemMeta(slot6meta);
        //slot 7
        ItemStack slot7 = new ItemStack(Material.BARRIER);
        ItemMeta slot7meta = slot7.getItemMeta();
        slot7meta.setDisplayName(ChatColor.WHITE + "\uE001");
        slot7meta.setCustomModelData(1);
        slot7.setItemMeta(slot7meta);
        //slot 8
        ItemStack slot8 = new ItemStack(Material.BARRIER);
        ItemMeta slot8meta = slot8.getItemMeta();
        slot8meta.setDisplayName(ChatColor.WHITE + "\uE00B");
        slot8meta.setAttributeModifiers(null);
        slot8meta.setCustomModelData(1);
        slot8.setItemMeta(slot8meta);

        // 48-45

        //slot 45
        ItemStack slot45 = new ItemStack(Material.BARRIER);
        ItemMeta slot45meta = slot45.getItemMeta();
        slot45meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot45meta.setCustomModelData(1);
        slot45.setItemMeta(slot45meta);

        ItemStack slot46 = new ItemStack(Material.BARRIER);
        ItemMeta slot46meta = slot46.getItemMeta();
        slot46meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot46meta.setCustomModelData(1);
        slot46.setItemMeta(slot46meta);

        ItemStack slot47 = new ItemStack(Material.BARRIER);
        ItemMeta slot47meta = slot47.getItemMeta();
        slot47meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot47meta.setCustomModelData(1);
        slot47.setItemMeta(slot47meta);

        ItemStack slot48 = new ItemStack(Material.BARRIER);
        ItemMeta slot48meta = slot48.getItemMeta();
        slot48meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot48meta.setCustomModelData(1);
        slot48.setItemMeta(slot48meta);

        ItemStack slot34 = new ItemStack(Material.STICK);
        ItemMeta slot34meta = slot34.getItemMeta();
        slot34meta.setDisplayName(ChatColor.WHITE + "\uE003");
        List<String> lore = new ArrayList<>();
        lore.add(net.md_5.bungee.api.ChatColor.of("#c73f00") + "level 1");
        lore.add(net.md_5.bungee.api.ChatColor.of("#ff7433") + "* Fire Resistance");
        lore.add(net.md_5.bungee.api.ChatColor.of("#c73f00") + "level 2");
        lore.add(net.md_5.bungee.api.ChatColor.of("#ff7433") + "* Nether Mobs Passive");
        lore.add(net.md_5.bungee.api.ChatColor.of("#c73f00") + "level 3");
        lore.add(net.md_5.bungee.api.ChatColor.of("#ff7433") + "* Regen Health in Lava");
        slot34meta.setLore(lore);
        slot34meta.setCustomModelData(12);
        slot34.setItemMeta(slot34meta);

        gui.setItem(0, slot0);
        gui.setItem(1, slot1);
        gui.setItem(2, slot2);
        gui.setItem(3, slot3);
        gui.setItem(5, slot5);
        gui.setItem(6, slot6);
        gui.setItem(7, slot7);
        gui.setItem(8, slot8);
        gui.setItem(45, slot45);
        gui.setItem(46, slot46);
        gui.setItem(47, slot47);
        gui.setItem(48, slot48);
        gui.setItem(34, slot34);



        //returns the gui
        return gui;


    }
    public static Inventory ShadowkinGuiCreator()
    {
        //Creating the varriable gui within an Inventory type
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE375");


        //Makes The ItemStacks
        //slot 0
        ItemStack slot0 = new ItemStack(Material.BARRIER);
        ItemMeta slot0meta = slot0.getItemMeta();
        slot0meta.setDisplayName(ChatColor.WHITE + "\uE00C");
        slot0meta.setCustomModelData(1);
        slot0.setItemMeta(slot0meta);
        //slot 1
        ItemStack slot1 = new ItemStack(Material.BARRIER);
        ItemMeta slot1meta = slot1.getItemMeta();
        slot1meta.setDisplayName(ChatColor.WHITE + "\uE00E");
        slot1meta.setCustomModelData(1);
        slot1.setItemMeta(slot1meta);
        //slot 2
        ItemStack slot2 = new ItemStack(Material.BARRIER);
        ItemMeta slot2meta = slot2.getItemMeta();
        slot2meta.setDisplayName(ChatColor.WHITE + "\uE005");
        slot2meta.setCustomModelData(1);
        slot2.setItemMeta(slot2meta);
        //slot 3
        ItemStack slot3 = new ItemStack(Material.BARRIER);
        ItemMeta slot3meta = slot3.getItemMeta();
        slot3meta.setDisplayName(ChatColor.WHITE + "\uE00D");
        slot3meta.setCustomModelData(1);
        slot3.setItemMeta(slot3meta);
        //slot 4
        ItemStack slot4 = new ItemStack(Material.BARRIER);
        ItemMeta slot4meta = slot4.getItemMeta();
        slot4meta.setDisplayName(ChatColor.WHITE + "\uE003");
        slot4meta.setCustomModelData(1);
        slot4.setItemMeta(slot4meta);
        //slot 5
        ItemStack slot5 = new ItemStack(Material.BARRIER);
        ItemMeta slot5meta = slot5.getItemMeta();
        slot5meta.setDisplayName(ChatColor.WHITE + "\uE00A");
        slot5meta.setCustomModelData(1);
        slot5.setItemMeta(slot5meta);
        //slot 6
        ItemStack slot6 = new ItemStack(Material.BARRIER);
        ItemMeta slot6meta = slot6.getItemMeta();
        slot6meta.setDisplayName(ChatColor.WHITE + "\uE002");
        slot6meta.setCustomModelData(1);
        slot6.setItemMeta(slot6meta);
        //slot 7
        ItemStack slot7 = new ItemStack(Material.BARRIER);
        ItemMeta slot7meta = slot7.getItemMeta();
        slot7meta.setDisplayName(ChatColor.WHITE + "\uE001");
        slot7meta.setCustomModelData(1);
        slot7.setItemMeta(slot7meta);
        //slot 8
        ItemStack slot8 = new ItemStack(Material.BARRIER);
        ItemMeta slot8meta = slot8.getItemMeta();
        slot8meta.setDisplayName(ChatColor.WHITE + "\uE00B");
        slot8meta.setAttributeModifiers(null);
        slot8meta.setCustomModelData(1);
        slot8.setItemMeta(slot8meta);

        // 48-45

        //slot 45
        ItemStack slot45 = new ItemStack(Material.BARRIER);
        ItemMeta slot45meta = slot45.getItemMeta();
        slot45meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot45meta.setCustomModelData(1);
        slot45.setItemMeta(slot45meta);

        ItemStack slot46 = new ItemStack(Material.BARRIER);
        ItemMeta slot46meta = slot46.getItemMeta();
        slot46meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot46meta.setCustomModelData(1);
        slot46.setItemMeta(slot46meta);

        ItemStack slot47 = new ItemStack(Material.BARRIER);
        ItemMeta slot47meta = slot47.getItemMeta();
        slot47meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot47meta.setCustomModelData(1);
        slot47.setItemMeta(slot47meta);

        ItemStack slot48 = new ItemStack(Material.BARRIER);
        ItemMeta slot48meta = slot48.getItemMeta();
        slot48meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot48meta.setCustomModelData(1);
        slot48.setItemMeta(slot48meta);

        ItemStack slot34 = new ItemStack(Material.STICK);
        ItemMeta slot34meta = slot34.getItemMeta();
        slot34meta.setDisplayName(ChatColor.WHITE + "\uE00A");
        List<String> lore = new ArrayList<>();
        lore.add(net.md_5.bungee.api.ChatColor.of("#560a8a") + "level 1");
        lore.add(net.md_5.bungee.api.ChatColor.of("#a94de8") + "* Spiders Passive");
        lore.add(net.md_5.bungee.api.ChatColor.of("#a94de8") + "* Night Vision");
        lore.add(net.md_5.bungee.api.ChatColor.of("#560a8a") + "level 2");
        lore.add(net.md_5.bungee.api.ChatColor.of("#a94de8") + "* Shadow Walk (Shift)");
        lore.add(net.md_5.bungee.api.ChatColor.of("#560a8a") + "level 3");
        lore.add(net.md_5.bungee.api.ChatColor.of("#a94de8") + "* Teleport Ability");
        slot34meta.setLore(lore);
        slot34meta.setCustomModelData(13);
        slot34.setItemMeta(slot34meta);

        gui.setItem(0, slot0);
        gui.setItem(1, slot1);
        gui.setItem(2, slot2);
        gui.setItem(3, slot3);
        gui.setItem(4, slot4);
        gui.setItem(6, slot6);
        gui.setItem(7, slot7);
        gui.setItem(8, slot8);
        gui.setItem(45, slot45);
        gui.setItem(46, slot46);
        gui.setItem(47, slot47);
        gui.setItem(48, slot48);
        gui.setItem(34, slot34);

        //returns the gui
        return gui;


    }
    public static Inventory WraithGuiCreator()
    {
        //Creating the varriable gui within an Inventory type
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE376");


        //Makes The ItemStacks
        //slot 0
        ItemStack slot0 = new ItemStack(Material.BARRIER);
        ItemMeta slot0meta = slot0.getItemMeta();
        slot0meta.setDisplayName(ChatColor.WHITE + "\uE00C");
        slot0meta.setCustomModelData(1);
        slot0.setItemMeta(slot0meta);
        //slot 1
        ItemStack slot1 = new ItemStack(Material.BARRIER);
        ItemMeta slot1meta = slot1.getItemMeta();
        slot1meta.setDisplayName(ChatColor.WHITE + "\uE00E");
        slot1meta.setCustomModelData(1);
        slot1.setItemMeta(slot1meta);
        //slot 2
        ItemStack slot2 = new ItemStack(Material.BARRIER);
        ItemMeta slot2meta = slot2.getItemMeta();
        slot2meta.setDisplayName(ChatColor.WHITE + "\uE005");
        slot2meta.setCustomModelData(1);
        slot2.setItemMeta(slot2meta);
        //slot 3
        ItemStack slot3 = new ItemStack(Material.BARRIER);
        ItemMeta slot3meta = slot3.getItemMeta();
        slot3meta.setDisplayName(ChatColor.WHITE + "\uE00D");
        slot3meta.setCustomModelData(1);
        slot3.setItemMeta(slot3meta);
        //slot 4
        ItemStack slot4 = new ItemStack(Material.BARRIER);
        ItemMeta slot4meta = slot4.getItemMeta();
        slot4meta.setDisplayName(ChatColor.WHITE + "\uE003");
        slot4meta.setCustomModelData(1);
        slot4.setItemMeta(slot4meta);
        //slot 5
        ItemStack slot5 = new ItemStack(Material.BARRIER);
        ItemMeta slot5meta = slot5.getItemMeta();
        slot5meta.setDisplayName(ChatColor.WHITE + "\uE00A");
        slot5meta.setCustomModelData(1);
        slot5.setItemMeta(slot5meta);
        //slot 6
        ItemStack slot6 = new ItemStack(Material.BARRIER);
        ItemMeta slot6meta = slot6.getItemMeta();
        slot6meta.setDisplayName(ChatColor.WHITE + "\uE002");
        slot6meta.setCustomModelData(1);
        slot6.setItemMeta(slot6meta);
        //slot 7
        ItemStack slot7 = new ItemStack(Material.BARRIER);
        ItemMeta slot7meta = slot7.getItemMeta();
        slot7meta.setDisplayName(ChatColor.WHITE + "\uE001");
        slot7meta.setCustomModelData(1);
        slot7.setItemMeta(slot7meta);
        //slot 8
        ItemStack slot8 = new ItemStack(Material.BARRIER);
        ItemMeta slot8meta = slot8.getItemMeta();
        slot8meta.setDisplayName(ChatColor.WHITE + "\uE00B");
        slot8meta.setAttributeModifiers(null);
        slot8meta.setCustomModelData(1);
        slot8.setItemMeta(slot8meta);

        // 48-45

        //slot 45
        ItemStack slot45 = new ItemStack(Material.BARRIER);
        ItemMeta slot45meta = slot45.getItemMeta();
        slot45meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot45meta.setCustomModelData(1);
        slot45.setItemMeta(slot45meta);

        ItemStack slot46 = new ItemStack(Material.BARRIER);
        ItemMeta slot46meta = slot46.getItemMeta();
        slot46meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot46meta.setCustomModelData(1);
        slot46.setItemMeta(slot46meta);

        ItemStack slot47 = new ItemStack(Material.BARRIER);
        ItemMeta slot47meta = slot47.getItemMeta();
        slot47meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot47meta.setCustomModelData(1);
        slot47.setItemMeta(slot47meta);

        ItemStack slot48 = new ItemStack(Material.BARRIER);
        ItemMeta slot48meta = slot48.getItemMeta();
        slot48meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot48meta.setCustomModelData(1);
        slot48.setItemMeta(slot48meta);

        ItemStack slot34 = new ItemStack(Material.STICK);
        ItemMeta slot34meta = slot34.getItemMeta();
        slot34meta.setDisplayName(ChatColor.WHITE + "\uE002");
        List<String> lore = new ArrayList<>();
        lore.add(net.md_5.bungee.api.ChatColor.of("#1625a8") + "level 1");
        lore.add(net.md_5.bungee.api.ChatColor.of("#3f4fe0") + "* Night Vision");
        lore.add(net.md_5.bungee.api.ChatColor.of("#3f4fe0") + "* Releases A Shriek When Killed");
        lore.add(net.md_5.bungee.api.ChatColor.of("#1625a8") + "level 2");
        lore.add(net.md_5.bungee.api.ChatColor.of("#3f4fe0") + "* Slow Falling (Crouch)");
        lore.add(net.md_5.bungee.api.ChatColor.of("#3f4fe0") + "* Undead Mobs Passive");
        lore.add(net.md_5.bungee.api.ChatColor.of("#1625a8") + "level 3");
        lore.add(net.md_5.bungee.api.ChatColor.of("#3f4fe0") + "* Emergency Invisibility Ability");
        slot34meta.setLore(lore);
        slot34meta.setCustomModelData(14);
        slot34.setItemMeta(slot34meta);

        gui.setItem(0, slot0);
        gui.setItem(1, slot1);
        gui.setItem(2, slot2);
        gui.setItem(3, slot3);
        gui.setItem(4, slot4);
        gui.setItem(5, slot5);
        gui.setItem(7, slot7);
        gui.setItem(8, slot8);
        gui.setItem(45, slot45);
        gui.setItem(46, slot46);
        gui.setItem(47, slot47);
        gui.setItem(48, slot48);
        gui.setItem(34, slot34);



        //returns the gui
        return gui;


    }
    public static Inventory DwarfGuiCreator()
    {
        //Creating the varriable gui within an Inventory type
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE377");


        //Makes The ItemStacks
        //slot 0
        ItemStack slot0 = new ItemStack(Material.BARRIER);
        ItemMeta slot0meta = slot0.getItemMeta();
        slot0meta.setDisplayName(ChatColor.WHITE + "\uE00C");
        slot0meta.setCustomModelData(1);
        slot0.setItemMeta(slot0meta);
        //slot 1
        ItemStack slot1 = new ItemStack(Material.BARRIER);
        ItemMeta slot1meta = slot1.getItemMeta();
        slot1meta.setDisplayName(ChatColor.WHITE + "\uE00E");
        slot1meta.setCustomModelData(1);
        slot1.setItemMeta(slot1meta);
        //slot 2
        ItemStack slot2 = new ItemStack(Material.BARRIER);
        ItemMeta slot2meta = slot2.getItemMeta();
        slot2meta.setDisplayName(ChatColor.WHITE + "\uE005");
        slot2meta.setCustomModelData(1);
        slot2.setItemMeta(slot2meta);
        //slot 3
        ItemStack slot3 = new ItemStack(Material.BARRIER);
        ItemMeta slot3meta = slot3.getItemMeta();
        slot3meta.setDisplayName(ChatColor.WHITE + "\uE00D");
        slot3meta.setCustomModelData(1);
        slot3.setItemMeta(slot3meta);
        //slot 4
        ItemStack slot4 = new ItemStack(Material.BARRIER);
        ItemMeta slot4meta = slot4.getItemMeta();
        slot4meta.setDisplayName(ChatColor.WHITE + "\uE003");
        slot4meta.setCustomModelData(1);
        slot4.setItemMeta(slot4meta);
        //slot 5
        ItemStack slot5 = new ItemStack(Material.BARRIER);
        ItemMeta slot5meta = slot5.getItemMeta();
        slot5meta.setDisplayName(ChatColor.WHITE + "\uE00A");
        slot5meta.setCustomModelData(1);
        slot5.setItemMeta(slot5meta);
        //slot 6
        ItemStack slot6 = new ItemStack(Material.BARRIER);
        ItemMeta slot6meta = slot6.getItemMeta();
        slot6meta.setDisplayName(ChatColor.WHITE + "\uE002");
        slot6meta.setCustomModelData(1);
        slot6.setItemMeta(slot6meta);
        //slot 7
        ItemStack slot7 = new ItemStack(Material.BARRIER);
        ItemMeta slot7meta = slot7.getItemMeta();
        slot7meta.setDisplayName(ChatColor.WHITE + "\uE001");
        slot7meta.setCustomModelData(1);
        slot7.setItemMeta(slot7meta);
        //slot 8
        ItemStack slot8 = new ItemStack(Material.BARRIER);
        ItemMeta slot8meta = slot8.getItemMeta();
        slot8meta.setDisplayName(ChatColor.WHITE + "\uE00B");
        slot8meta.setAttributeModifiers(null);
        slot8meta.setCustomModelData(1);
        slot8.setItemMeta(slot8meta);

        // 48-45

        //slot 45
        ItemStack slot45 = new ItemStack(Material.BARRIER);
        ItemMeta slot45meta = slot45.getItemMeta();
        slot45meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot45meta.setCustomModelData(1);
        slot45.setItemMeta(slot45meta);

        ItemStack slot46 = new ItemStack(Material.BARRIER);
        ItemMeta slot46meta = slot46.getItemMeta();
        slot46meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot46meta.setCustomModelData(1);
        slot46.setItemMeta(slot46meta);

        ItemStack slot47 = new ItemStack(Material.BARRIER);
        ItemMeta slot47meta = slot47.getItemMeta();
        slot47meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot47meta.setCustomModelData(1);
        slot47.setItemMeta(slot47meta);

        ItemStack slot48 = new ItemStack(Material.BARRIER);
        ItemMeta slot48meta = slot48.getItemMeta();
        slot48meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot48meta.setCustomModelData(1);
        slot48.setItemMeta(slot48meta);

        ItemStack slot34 = new ItemStack(Material.STICK);
        ItemMeta slot34meta = slot34.getItemMeta();
        slot34meta.setDisplayName(ChatColor.WHITE + "\uE001");
        List<String> lore = new ArrayList<>();
        lore.add(net.md_5.bungee.api.ChatColor.of("#944a51") + "level 1");
        lore.add(net.md_5.bungee.api.ChatColor.of("#c4787f") + "* Haste while holding pickaxe");
        lore.add(net.md_5.bungee.api.ChatColor.of("#944a51") + "level 2");
        lore.add(net.md_5.bungee.api.ChatColor.of("#c4787f") + "* More ore drops");
        lore.add(net.md_5.bungee.api.ChatColor.of("#944a51") + "level 3");
        lore.add(net.md_5.bungee.api.ChatColor.of("#c4787f") + "* 3x3 mining ability");
        slot34meta.setLore(lore);
        slot34meta.setCustomModelData(15);
        slot34.setItemMeta(slot34meta);

        gui.setItem(0, slot0);
        gui.setItem(1, slot1);
        gui.setItem(2, slot2);
        gui.setItem(3, slot3);
        gui.setItem(4, slot4);
        gui.setItem(5, slot5);
        gui.setItem(6, slot6);
        gui.setItem(8, slot8);
        gui.setItem(45, slot45);
        gui.setItem(46, slot46);
        gui.setItem(47, slot47);
        gui.setItem(48, slot48);
        gui.setItem(34, slot34);



        //returns the gui
        return gui;


    }
    public static Inventory HumanGuiCreator()
    {
        //Creating the varriable gui within an Inventory type
        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE378");


        //Makes The ItemStacks
        //slot 0
        ItemStack slot0 = new ItemStack(Material.BARRIER);
        ItemMeta slot0meta = slot0.getItemMeta();
        slot0meta.setDisplayName(ChatColor.WHITE + "\uE00C");
        slot0meta.setCustomModelData(1);
        slot0.setItemMeta(slot0meta);
        //slot 1
        ItemStack slot1 = new ItemStack(Material.BARRIER);
        ItemMeta slot1meta = slot1.getItemMeta();
        slot1meta.setDisplayName(ChatColor.WHITE + "\uE00E");
        slot1meta.setCustomModelData(1);
        slot1.setItemMeta(slot1meta);
        //slot 2
        ItemStack slot2 = new ItemStack(Material.BARRIER);
        ItemMeta slot2meta = slot2.getItemMeta();
        slot2meta.setDisplayName(ChatColor.WHITE + "\uE005");
        slot2meta.setCustomModelData(1);
        slot2.setItemMeta(slot2meta);
        //slot 3
        ItemStack slot3 = new ItemStack(Material.BARRIER);
        ItemMeta slot3meta = slot3.getItemMeta();
        slot3meta.setDisplayName(ChatColor.WHITE + "\uE00D");
        slot3meta.setCustomModelData(1);
        slot3.setItemMeta(slot3meta);
        //slot 4
        ItemStack slot4 = new ItemStack(Material.BARRIER);
        ItemMeta slot4meta = slot4.getItemMeta();
        slot4meta.setDisplayName(ChatColor.WHITE + "\uE003");
        slot4meta.setCustomModelData(1);
        slot4.setItemMeta(slot4meta);
        //slot 5
        ItemStack slot5 = new ItemStack(Material.BARRIER);
        ItemMeta slot5meta = slot5.getItemMeta();
        slot5meta.setDisplayName(ChatColor.WHITE + "\uE00A");
        slot5meta.setCustomModelData(1);
        slot5.setItemMeta(slot5meta);
        //slot 6
        ItemStack slot6 = new ItemStack(Material.BARRIER);
        ItemMeta slot6meta = slot6.getItemMeta();
        slot6meta.setDisplayName(ChatColor.WHITE + "\uE002");
        slot6meta.setCustomModelData(1);
        slot6.setItemMeta(slot6meta);
        //slot 7
        ItemStack slot7 = new ItemStack(Material.BARRIER);
        ItemMeta slot7meta = slot7.getItemMeta();
        slot7meta.setDisplayName(ChatColor.WHITE + "\uE001");
        slot7meta.setCustomModelData(1);
        slot7.setItemMeta(slot7meta);
        //slot 8
        ItemStack slot8 = new ItemStack(Material.BARRIER);
        ItemMeta slot8meta = slot8.getItemMeta();
        slot8meta.setDisplayName(ChatColor.WHITE + "\uE00B");
        slot8meta.setAttributeModifiers(null);
        slot8meta.setCustomModelData(1);
        slot8.setItemMeta(slot8meta);

        // 48-45


        //slot 45
        ItemStack slot45 = new ItemStack(Material.BARRIER);
        ItemMeta slot45meta = slot45.getItemMeta();
        slot45meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot45meta.setCustomModelData(1);
        slot45.setItemMeta(slot45meta);

        ItemStack slot46 = new ItemStack(Material.BARRIER);
        ItemMeta slot46meta = slot46.getItemMeta();
        slot46meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot46meta.setCustomModelData(1);
        slot46.setItemMeta(slot46meta);

        ItemStack slot47 = new ItemStack(Material.BARRIER);
        ItemMeta slot47meta = slot47.getItemMeta();
        slot47meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot47meta.setCustomModelData(1);
        slot47.setItemMeta(slot47meta);

        ItemStack slot48 = new ItemStack(Material.BARRIER);
        ItemMeta slot48meta = slot48.getItemMeta();
        slot48meta.setDisplayName(net.md_5.bungee.api.ChatColor.of("#00ff24") + "ᴄᴏɴꜰɪʀᴍ ʀᴀᴄᴇ");
        slot48meta.setCustomModelData(1);
        slot48.setItemMeta(slot48meta);

        ItemStack slot34 = new ItemStack(Material.STICK);
        ItemMeta slot34meta = slot34.getItemMeta();
        slot34meta.setDisplayName(ChatColor.WHITE + "\uE00B");
        List<String> lore = new ArrayList<>();
        lore.add(net.md_5.bungee.api.ChatColor.of("#1cb037") + "level 1");
        lore.add(net.md_5.bungee.api.ChatColor.of("#55ed71") + "* Better Farming");
        lore.add(net.md_5.bungee.api.ChatColor.of("#1cb037") + "level 2");
        lore.add(net.md_5.bungee.api.ChatColor.of("#55ed71") + "* Better Fishing");
        lore.add(net.md_5.bungee.api.ChatColor.of("#1cb037") + "level 3");
        lore.add(net.md_5.bungee.api.ChatColor.of("#55ed71") + "* Haste 2 while holding axe");
        lore.add(net.md_5.bungee.api.ChatColor.of("#55ed71") + "* Strength 1 in plains biome");

        slot34meta.setLore(lore);
        slot34meta.setCustomModelData(16);
        slot34.setItemMeta(slot34meta);

        gui.setItem(0, slot0);
        gui.setItem(1, slot1);
        gui.setItem(2, slot2);
        gui.setItem(3, slot3);
        gui.setItem(4, slot4);
        gui.setItem(5, slot5);
        gui.setItem(6, slot6);
        gui.setItem(7, slot7);
        gui.setItem(45, slot45);
        gui.setItem(46, slot46);
        gui.setItem(47, slot47);
        gui.setItem(48, slot48);
        gui.setItem(34, slot34);



        //returns the gui
        return gui;


    }


    public void SelectRace(Race race, Player p)
    {
        system.removeFromCreation(p);
        if (race!=Race.DRAGONBORN && p.getInventory().getItem(38)!=null && p.getInventory().getItem(38).getType().equals(Material.ELYTRA)) {
            p.getInventory().clear(38);
        }
        system.addSelecting(p, race);
        p.sendMessage(ChatColor.YELLOW + "You have selected your race! Now, type a name for your character in chat");
        p.sendMessage(ChatColor.GREEN + "This name can be anything you want between 3 and 16 characters, and it can be changed later. You can also just type your username.");
        system.sendSelectingMessages();
        p.closeInventory();
    }

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent e)
    {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE370"))
        {
            e.setCancelled(true);
            if (!e.getClickedInventory().equals(p.getOpenInventory().getTopInventory()))
                return;

            switch (e.getSlot())
            {
                case 1:
                    p.openInventory(this.DragonbornGUI);
                    break;
                case 2:
                    p.openInventory(this.AmbystomaGUI);
                    break;
                case 3:
                    p.openInventory(this.ElfGUI);
                    break;
                case 4:
                    p.openInventory(this.DemonGUI);
                    break;
                case 5:
                    p.openInventory(this.ShadowkinGUI);
                    break;
                case 6:
                    p.openInventory(this.WraithGUI);
                    break;
                case 7:
                    p.openInventory(this.DwarfGUI);
                    break;
                case 8:
                    p.openInventory(this.HumanGUI);
                    break;

                case 45:
                    SelectRace(Race.NEKO, p);
                    break;
                case 46:
                    SelectRace(Race.NEKO, p);
                    break;
                case 47:
                    SelectRace(Race.NEKO, p);
                    break;
                case 48:
                    SelectRace(Race.NEKO, p);
                    break;

            }
        }
        else if (e.getView().getTitle().equals(ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE371"))
        {
            e.setCancelled(true);
            if (!e.getClickedInventory().equals(p.getOpenInventory().getTopInventory()))
                return;

            switch (e.getSlot())
            {
                case 0:
                    p.openInventory(this.NekoGUI);
                    break;
                case 2:
                    p.openInventory(this.AmbystomaGUI);
                    break;
                case 3:
                    p.openInventory(this.ElfGUI);
                    break;
                case 4:
                    p.openInventory(this.DemonGUI);
                    break;
                case 5:
                    p.openInventory(this.ShadowkinGUI);
                    break;
                case 6:
                    p.openInventory(this.WraithGUI);
                    break;
                case 7:
                    p.openInventory(this.DwarfGUI);
                    break;
                case 8:
                    p.openInventory(this.HumanGUI);
                    break;

                case 45:
                    SelectRace(Race.DRAGONBORN, p);
                    break;
                case 46:
                    SelectRace(Race.DRAGONBORN, p);
                    break;
                case 47:
                    SelectRace(Race.DRAGONBORN, p);
                    break;
                case 48:
                    SelectRace(Race.DRAGONBORN, p);
                    break;

            }
        }
        else if (e.getView().getTitle().equals(ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE372"))
        {
            e.setCancelled(true);
            if (!e.getClickedInventory().equals(p.getOpenInventory().getTopInventory()))
                return;

            switch (e.getSlot())
            {
                case 0:
                    p.openInventory(this.NekoGUI);
                    break;
                case 1:
                    p.openInventory(this.DragonbornGUI);
                    break;
                case 3:
                    p.openInventory(this.ElfGUI);
                    break;
                case 4:
                    p.openInventory(this.DemonGUI);
                    break;
                case 5:
                    p.openInventory(this.ShadowkinGUI);
                    break;
                case 6:
                    p.openInventory(this.WraithGUI);
                    break;
                case 7:
                    p.openInventory(this.DwarfGUI);
                    break;
                case 8:
                    p.openInventory(this.HumanGUI);
                    break;

                case 45:
                    SelectRace(Race.AMBYSTOMA, p);
                    break;
                case 46:
                    SelectRace(Race.AMBYSTOMA, p);
                    break;
                case 47:
                    SelectRace(Race.AMBYSTOMA, p);
                    break;
                case 48:
                    SelectRace(Race.AMBYSTOMA, p);
                    break;

            }
        }
        else if (e.getView().getTitle().equals(ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE373"))
        {
            e.setCancelled(true);
            if (!e.getClickedInventory().equals(p.getOpenInventory().getTopInventory()))
                return;

            switch (e.getSlot())
            {
                case 0:
                    p.openInventory(this.NekoGUI);
                    break;
                case 1:
                    p.openInventory(this.DragonbornGUI);
                    break;
                case 2:
                    p.openInventory(this.AmbystomaGUI);
                    break;
                case 4:
                    p.openInventory(this.DemonGUI);
                    break;
                case 5:
                    p.openInventory(this.ShadowkinGUI);
                    break;
                case 6:
                    p.openInventory(this.WraithGUI);
                    break;
                case 7:
                    p.openInventory(this.DwarfGUI);
                    break;
                case 8:
                    p.openInventory(this.HumanGUI);
                    break;

                case 45:
                    SelectRace(Race.ELF, p);
                    break;
                case 46:
                    SelectRace(Race.ELF, p);
                    break;
                case 47:
                    SelectRace(Race.ELF, p);
                    break;
                case 48:
                    SelectRace(Race.ELF, p);
                    break;

            }
        }
        else if (e.getView().getTitle().equals(ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE374"))
        {
            e.setCancelled(true);
            if (!e.getClickedInventory().equals(p.getOpenInventory().getTopInventory()))
                return;

            switch (e.getSlot())
            {
                case 0:
                    p.openInventory(this.NekoGUI);
                    break;
                case 1:
                    p.openInventory(this.DragonbornGUI);
                    break;
                case 2:
                    p.openInventory(this.AmbystomaGUI);
                    break;
                case 3:
                    p.openInventory(this.ElfGUI);
                    break;
                case 5:
                    p.openInventory(this.ShadowkinGUI);
                    break;
                case 6:
                    p.openInventory(this.WraithGUI);
                    break;
                case 7:
                    p.openInventory(this.DwarfGUI);
                    break;
                case 8:
                    p.openInventory(this.HumanGUI);
                    break;

                case 45:
                    SelectRace(Race.DEMON, p);
                    break;
                case 46:
                    SelectRace(Race.DEMON, p);
                    break;
                case 47:
                    SelectRace(Race.DEMON, p);
                    break;
                case 48:
                    SelectRace(Race.DEMON, p);
                    break;

            }
        }
        else if (e.getView().getTitle().equals(ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE375"))
        {
            e.setCancelled(true);
            if (!e.getClickedInventory().equals(p.getOpenInventory().getTopInventory()))
                return;

            switch (e.getSlot())
            {
                case 0:
                    p.openInventory(this.NekoGUI);
                    break;
                case 1:
                    p.openInventory(this.DragonbornGUI);
                    break;
                case 2:
                    p.openInventory(this.AmbystomaGUI);
                    break;
                case 3:
                    p.openInventory(this.ElfGUI);
                    break;
                case 4:
                    p.openInventory(this.DemonGUI);
                    break;
                case 6:
                    p.openInventory(this.WraithGUI);
                    break;
                case 7:
                    p.openInventory(this.DwarfGUI);
                    break;
                case 8:
                    p.openInventory(this.HumanGUI);
                    break;

                case 45:
                    SelectRace(Race.DARKELF, p);
                    break;
                case 46:
                    SelectRace(Race.DARKELF, p);
                    break;
                case 47:
                    SelectRace(Race.DARKELF, p);
                    break;
                case 48:
                    SelectRace(Race.DARKELF, p);
                    break;

            }
        }
        else if (e.getView().getTitle().equals(ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE376"))
        {
            e.setCancelled(true);
            if (!e.getClickedInventory().equals(p.getOpenInventory().getTopInventory()))
                return;

            switch (e.getSlot())
            {
                case 0:
                    p.openInventory(this.NekoGUI);
                    break;
                case 1:
                    p.openInventory(this.DragonbornGUI);
                    break;
                case 2:
                    p.openInventory(this.AmbystomaGUI);
                    break;
                case 3:
                    p.openInventory(this.ElfGUI);
                    break;
                case 4:
                    p.openInventory(this.DemonGUI);
                    break;
                case 5:
                    p.openInventory(this.ShadowkinGUI);
                    break;
                case 7:
                    p.openInventory(this.DwarfGUI);
                    break;
                case 8:
                    p.openInventory(this.HumanGUI);
                    break;

                case 45:
                    SelectRace(Race.WRAITH, p);
                    break;
                case 46:
                    SelectRace(Race.WRAITH, p);
                    break;
                case 47:
                    SelectRace(Race.WRAITH, p);
                    break;
                case 48:
                    SelectRace(Race.WRAITH, p);
                    break;

            }
        }
        else if (e.getView().getTitle().equals(ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE377"))
        {
            e.setCancelled(true);
            if (!e.getClickedInventory().equals(p.getOpenInventory().getTopInventory()))
                return;

            switch (e.getSlot())
            {
                case 0:
                    p.openInventory(this.NekoGUI);
                    break;
                case 1:
                    p.openInventory(this.DragonbornGUI);
                    break;
                case 2:
                    p.openInventory(this.AmbystomaGUI);
                    break;
                case 3:
                    p.openInventory(this.ElfGUI);
                    break;
                case 4:
                    p.openInventory(this.DemonGUI);
                    break;
                case 5:
                    p.openInventory(this.ShadowkinGUI);
                    break;
                case 6:
                    p.openInventory(this.WraithGUI);
                    break;
                case 8:
                    p.openInventory(this.HumanGUI);
                    break;

                case 45:
                    SelectRace(Race.DWARF, p);
                    break;
                case 46:
                    SelectRace(Race.DWARF, p);
                    break;
                case 47:
                    SelectRace(Race.DWARF, p);
                    break;
                case 48:
                    SelectRace(Race.DWARF, p);
                    break;

            }
        }
        else if (e.getView().getTitle().equals(ChatColor.WHITE + "\uF801\uF801\uF801\uF801\uF801\uF801\uF801\uF801" + "\uE378"))
        {
            e.setCancelled(true);
            if (!e.getClickedInventory().equals(p.getOpenInventory().getTopInventory()))
                return;

            switch (e.getSlot())
            {
                case 0:
                    p.openInventory(this.NekoGUI);
                    break;
                case 1:
                    p.openInventory(this.DragonbornGUI);
                    break;
                case 2:
                    p.openInventory(this.AmbystomaGUI);
                    break;
                case 3:
                    p.openInventory(this.ElfGUI);
                    break;
                case 4:
                    p.openInventory(this.DemonGUI);
                    break;
                case 5:
                    p.openInventory(this.ShadowkinGUI);
                    break;
                case 6:
                    p.openInventory(this.WraithGUI);
                    break;
                case 7:
                    p.openInventory(this.DwarfGUI);
                    break;

                case 45:
                    SelectRace(Race.HUMAN, p);
                    break;
                case 46:
                    SelectRace(Race.HUMAN, p);
                    break;
                case 47:
                    SelectRace(Race.HUMAN, p);
                    break;
                case 48:
                    SelectRace(Race.HUMAN, p);
                    break;

            }
        }
    }
}
