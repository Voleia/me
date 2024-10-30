package me.voleia.volands.Abilities;

import me.voleia.volands.OC;
import me.voleia.volands.Volands;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WraithInvisibility implements Ability{

    PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY,300,0,false,false,true);
    PotionEffect speed = new PotionEffect(PotionEffectType.SPEED,300,1,false,false,true);

    public boolean trigger(OC o, Player player, int level) {
        if (player.getHealth()<=10) {
            player.addPotionEffect(invisibility);
            player.addPotionEffect(speed);
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_SCULK_SHRIEKER_SHRIEK, 1f, 1f);
            return true;
        }
        return false;
    }

    public int getCooldown() {
        return 30;
    }

    String name = Volands.getSystem().translate("Emergency Vanish");

    public String getName() {
        return name;
    }

    public int getLevel() {
        return 3;
    }

    public String failureMessage() {
        return "You can only enable that ability after reaching half health!";
    }
}
