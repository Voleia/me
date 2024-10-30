package me.voleia.volands.CustomGUISystem.Funcs;

import me.voleia.volands.CustomGUISystem.CustomGUI;
import me.voleia.volands.OC;
import org.bukkit.entity.Player;

public interface InventoryFunc {
    void run(OC oc, Player sender, CustomGUI inv);
    void altRun(OC oc, Player sender, CustomGUI inv);
}
