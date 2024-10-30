package me.voleia.volands.Abilities;

import me.voleia.volands.OC;
import me.voleia.volands.Volands;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DraconicBoost implements Ability {
    public boolean trigger(OC o, Player player, int level) {
        if (((int)o.getRaceClass().getFlag("stunTime"))<=0) {
            player.setGliding(true);
            player.setVelocity(player.getLocation().getDirection().normalize().multiply(2));
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1f, 1f);
            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(),100,0.1,0.1,0.1);
            return true;
        }
        return false;
    }

    public int getCooldown() {
        return 10;
    }

    String name = Volands.getSystem().translate("Boost");
    public String getName() {
        return name;
    }

    public int getLevel() {
        return 2;
    }

    public String failureMessage() {
        return "You are hurt! Try again in a few seconds.";
    }
}
