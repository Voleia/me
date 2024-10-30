package me.voleia.volands.Abilities;

import me.voleia.volands.OC;
import me.voleia.volands.Volands;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NekoNightVisionToggle implements Ability, ToggleableAbility{
    PotionEffect nightVision = new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,1,false,false,false);

    public boolean trigger(OC o, Player player, int level) {
        Biome biome = player.getLocation().getBlock().getBiome();
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            return true;
        } else if (level>=2 && (biome.equals(Biome.JUNGLE) || biome.equals(Biome.SPARSE_JUNGLE) || biome.equals(Biome.BAMBOO_JUNGLE) )){
            player.addPotionEffect(nightVision);
            return true;
        }
        return false;
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
        return "That ability can only be activated in the jungle!";
    }
}
