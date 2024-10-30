package me.voleia.volands.CustomGUISystem;

import me.voleia.volands.CustomGUISystem.Funcs.InventoryFunc;
import me.voleia.volands.OC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InvSlot {
    ItemStack stack;
    InventoryFunc func;

    public InvSlot(String name, List<String> lore, Material mat, InventoryFunc func_) {
        func = func_;
        stack = new ItemStack(mat);
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
    }

    public InvSlot(ItemStack stack_, InventoryFunc func_) {
        stack = stack_;
        func = func_;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void runFunction(OC oc, Player sender, CustomGUI inv) {
        func.run(oc,sender,inv);
        hasBeenRightClicked=false;
    }

    boolean hasBeenRightClicked = false;

    public void runAltFunction(OC oc, Player sender, CustomGUI inv) {
        if (hasBeenRightClicked) {
            func.altRun(oc,sender,inv);
        }
        hasBeenRightClicked=true;
    }
}
