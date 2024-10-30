package me.voleia.volands.Abilities;

import me.voleia.volands.OC;
import me.voleia.volands.Volands;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVisionToggleLevelTwo implements Ability, ToggleableAbility{

    PotionEffect nightVision = new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,1,false,false,false);

    public boolean trigger(OC o, Player player, int level) {
        o.getRaceClass().setFlag("nightvision",null);
        if (player.isInWater()) {
            return true;
        } else {
            return false;
        }
    }

    public int getCooldown() {
        return 0;
    }

    String name = Volands.getSystem().translate("Toggle Night Vision");

    public String getName() {
        return name;
    }
    public int getLevel() {
        return 2;
    }

    public String failureMessage() {
        return ChatColor.YELLOW + "Toggled successfully, but that ability will only activate once you go underwater";
    }
}
