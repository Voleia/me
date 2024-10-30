package me.voleia.volands.GUIs;

import me.voleia.volands.Volands;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class TitleGUIHandler implements Listener {

    Volands system = Volands.getSystem();
    @EventHandler
    public void invclick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();
            if (system.getTitle(player)!=null) {
                e.setCancelled(true);
                TitleGUI gui = system.getTitle(player);
                switch (e.getSlot()) {
                    case 1:
                        gui.clickPrefix(e.isLeftClick());
                        break;
                    case 3:
                        gui.clickSuffix(e.isLeftClick());
                        break;
                    case 8:
                        gui.confirm();
                        break;
                    case 6:
                        gui.clickGender();
                        break;
                }
            }
        }
    }

    @EventHandler
    public void close(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals(ChatColor.BLACK + "Noble Title Selection")) {
            system.removeTitle((Player)e.getPlayer());
        }
    }
}
