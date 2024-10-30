package me.voleia.volands;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StaticPresets {
    public static PotionEffect strengthI = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE,0,false,false,true);
    public static PotionEffect regenI = new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE,0,false,false,true);

    public static void removeArtificialEffect(Player p, PotionEffectType pot) {
        if (p.hasPotionEffect(pot) && p.getPotionEffect(pot).getDuration()>500000) {
            p.removePotionEffect(pot);
        }
    }

    public static PotionEffect getPotionEffect(int level, PotionEffectType type) {
        return new PotionEffect(type, Integer.MAX_VALUE, level-1, false, false, true);
    }

    public static boolean playerIsInBiome(Player player, Biome biome) {
        Biome cur = player.getLocation().getBlock().getBiome();
        return cur.equals(biome);
    }

    public static boolean playerIsInBiome(Player player, Biome[] biomes) {
        Biome cur = player.getLocation().getBlock().getBiome();
        for (Biome biome : biomes) {
            if (cur.equals(biome)) {
                return true;
            }
        }
        return false;
    }

    public static void setPlayerHealth(Player player, int health) {
        if (player.getMaxHealth()!=health) {
            player.setMaxHealth(health);
        }
    }
}
