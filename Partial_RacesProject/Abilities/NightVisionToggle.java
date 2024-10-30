package me.voleia.volands.Abilities;

import me.voleia.volands.OC;
import me.voleia.volands.Volands;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVisionToggle implements Ability, ToggleableAbility{

    PotionEffect nightVision = new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,1,false,false,false);

    public boolean trigger(OC o, Player player, int level) {
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        } else {
            player.addPotionEffect(nightVision);
        }
        return true;
    }

    public int getCooldown() {
        return 0;
    }

    String name = Volands.getSystem().translate("Toggle Night Vision");

    public String getName() {
        return name;
    }

    public int getLevel() {
        return 0;
    }

    public String failureMessage() {
        return "";
    }
}
