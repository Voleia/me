package me.voleia.volands.Races;

import me.voleia.volands.Abilities.Ability;
import me.voleia.volands.Enums.ExpType;
import me.voleia.volands.SkillTree.Skill;
import me.voleia.volands.SkillTree.SkillTree;
import me.voleia.volands.Volands;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Neko implements RaceClass {

    boolean canDoubleJump = true;
    PotionEffect vision = new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,0,false,false,true);

    Ability[] abilities = new Ability[2];

    Player cur;

    public Neko() {
        multipliers.put(ExpType.COMBAT, 0.8);
        multipliers.put(ExpType.FARMING, 1.3);
        multipliers.put(ExpType.MINING, 1.2);
        multipliers.put(ExpType.HUNTING,0.7);
        abilities[0]=Volands.getSystem().getAbilities()[8];
        abilities[1]=Volands.getSystem().getAbilities()[9];
    }

    HashMap<ExpType, Double> multipliers = new HashMap<>();

    public double getMultiplier(ExpType type) {
        return multipliers.get(type);
    }

    public List<String> getInfo() {
        return Arrays.asList(new String[]{"TBA"});
    }

    public void loadTree(SkillTree tree) {

    }

    public void updateTree(Skill skill, boolean value) {

    }

    @Override
    public boolean getToggledAbility() {
        return cur.hasPotionEffect(PotionEffectType.NIGHT_VISION);
    }

    public boolean getCanClimb() {
        return canClimb;
    }

    public boolean blockAround(Location location) {
        Block[] blocks = new Block[4];
        blocks[0]=location.getWorld().getBlockAt(location.getBlockX()-1,location.getBlockY(),location.getBlockZ());
        blocks[1]=location.getWorld().getBlockAt(location.getBlockX()+1,location.getBlockY(),location.getBlockZ());
        blocks[2]=location.getWorld().getBlockAt(location.getBlockX(),location.getBlockY(),location.getBlockZ()-1);
        blocks[3]=location.getWorld().getBlockAt(location.getBlockX(),location.getBlockY(),location.getBlockZ()+1);
        for (Block block : blocks) {
            if (!block.getType().equals(Material.AIR) && block.getType().isCollidable()) {
                return true;
            }
        }
        return false;
    }

    PotionEffect lev = new PotionEffect(PotionEffectType.LEVITATION,Integer.MAX_VALUE,1,false,false,false);

    public void onTick(Player p, int level) {
        cur = p;
        if (p.isSneaking() && blockAround(p.getLocation()) && canClimb) {
            p.addPotionEffect(lev);
        } else {
            removeArtificialPotion(p,PotionEffectType.LEVITATION);
        }
        Biome biome = p.getLocation().getBlock().getBiome();
        if (!(level>=2 && (biome.equals(Biome.JUNGLE) || biome.equals(Biome.SPARSE_JUNGLE) || biome.equals(Biome.BAMBOO_JUNGLE) ) )) {
            removeArtificialPotion(p,PotionEffectType.NIGHT_VISION);
        }
        if (p.isOnGround()) {
            canDoubleJump=true;
        }

    }

    void removeArtificialPotion(Player p, PotionEffectType pot) {
        if (p.hasPotionEffect(pot) && p.getPotionEffect(pot).getDuration()>500000) {
            p.removePotionEffect(pot);
        }
    }

    public Ability[] getAbilities() {
        return abilities;
    }

    String name = Volands.getSystem().translate("Neko");

    public String getDisplayName() {
        return name;
    }

    boolean canClimb = true;

    public void setFlag(String flag, Object value) {
        switch (flag) {
            case "doubleJump":
                canDoubleJump=(boolean) value;
            case "climb":
                canClimb=(boolean) value;
        }
    }

    public Object getFlag(String flag) {
        switch (flag) {
            case "doubleJump":
                return canDoubleJump;
            case "climb":
                return canClimb;
        }
        return null;
    }
}
