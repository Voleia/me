package me.voleia.volands.CustomGUISystem.Funcs;

import me.voleia.volands.CustomGUISystem.CustomGUI;
import me.voleia.volands.CustomGUISystem.InvGrid;
import me.voleia.volands.OC;
import org.bukkit.entity.Player;

public class Func_Scroll implements InventoryFunc{

    CardinalDirection direction;
    public Func_Scroll(CardinalDirection dir) {
        direction=dir;
    }

    @Override
    public void run(OC oc, Player sender, CustomGUI inv) {
        switch (direction) {
            case UP:
                if (inv.getGrid().moveUp(1)) {
                    inv.createUI();
                }
                break;
            case DOWN:
                if (inv.getGrid().moveDown(1)) {
                    inv.createUI();
                }
                break;
            case LEFT:
                if (inv.getGrid().moveLeft(1)) {
                    inv.createUI();
                }
                break;
            case RIGHT:
                if (inv.getGrid().moveRight(1)) {
                    inv.createUI();
                }
                break;
        }
    }

    @Override
    public void altRun(OC oc, Player sender, CustomGUI inv) {
        run(oc,sender,inv);
    }
}
