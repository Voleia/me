package me.voleia.volands.Races;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ArmorStack {
    ItemStack helmet;
    ItemStack chestplate;
    ItemStack leggings;
    ItemStack boots;

    public ArmorStack(Player player) {
        PlayerInventory inventory = player.getInventory();
        helmet = inventory.getItem(39);
        chestplate = inventory.getItem(38);
        leggings = inventory.getItem(37);
        boots = inventory.getItem(36);
        if (chestplate!=null && chestplate.getType().equals(Material.ELYTRA)) {
            chestplate=null;
        }
        if (helmet!=null) {
            inventory.setItem(39,ItemStack.empty());
        }
        if (chestplate!=null) {
            inventory.setItem(38,ItemStack.empty());
        }
        if (leggings!=null)  {
            inventory.setItem(37,ItemStack.empty());
        }
        if (boots!=null) {
            inventory.setItem(36,ItemStack.empty());
        }
    }

    public void loadInventory(Player player) {
        if (helmet!=null) {
            player.getInventory().setItem(39,helmet);
        }
        if (chestplate!=null) {
            player.getInventory().setItem(38,chestplate);
        }
        if (leggings!=null) {
            player.getInventory().setItem(37,leggings);
        }
        if (boots!=null){
            player.getInventory().setItem(36,boots);
        }
    }
}
