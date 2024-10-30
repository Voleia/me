package me.voleia.volands.Abilities;

import me.voleia.volands.OC;
import me.voleia.volands.Volands;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ElvenDash implements Ability{
    public boolean trigger(OC o, Player player, int level) {
        Vector dir = player.getLocation().getDirection();
        dir.setY(0);
        player.setSneaking(false);
        player.setVelocity(dir.normalize().multiply(30));
        return true;
    }

    public int getCooldown() {
        return 45;
    }

    String name = Volands.getSystem().translate("Elven Dash");
    public String getName() {
        return name;
    }

    public int getLevel() {
        return 3;
    }

    public String failureMessage() {
        return "";
    }
}
