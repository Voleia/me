package me.voleia.volands.Races;

import me.voleia.volands.Abilities.Ability;
import me.voleia.volands.Enums.ExpType;
import me.voleia.volands.SkillTree.Skill;
import me.voleia.volands.SkillTree.SkillTree;
import me.voleia.volands.Volands;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Dragonborn implements RaceClass{

    PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, false, false, false);
    PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION,Integer.MAX_VALUE,0,false,false,false);
    PotionEffect res = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0,false,false,false);
    boolean rideable=false;
    boolean saveBreath=false;
    int stunTime=0;
    Ability[] abilities = new Ability[2];

    public Dragonborn() {
        abilities[0]= Volands.getSystem().getAbilities()[3];
        abilities[1]=Volands.getSystem().getAbilities()[4];
        multipliers.put(ExpType.COMBAT, 1.2);
        multipliers.put(ExpType.FARMING, 1.0);
        multipliers.put(ExpType.MINING, 1.4);
        multipliers.put(ExpType.HUNTING,0.6);
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
        return rideable;
    }

    public void onTick(Player p, int level) {
        //p.addPotionEffect(slowness);
        if (level>=2) {
            p.addPotionEffect(regen);

            if (level>=3) {
                p.addPotionEffect(res);
            }
        }
        if (stunTime>0) {
            stunTime-=1;
        }
        if (p.isOnGround() || p.isInLava() || p.isInWater()) {
            if (p.hasPotionEffect(PotionEffectType.SLOW_FALLING) && p.getPotionEffect(PotionEffectType.SLOW_FALLING).getDuration()<210) {
                p.removePotionEffect(PotionEffectType.SLOW_FALLING);
            }
        }
    }

    public Ability[] getAbilities() {
        return abilities;
    }

    String name = Volands.getSystem().translate("Dragonborn");

    public String getDisplayName() {
        return name;
    }

    public void setFlag(String flag, Object value) {
        switch (flag) {
            case "rideable":
                rideable=!rideable;
                break;
            case "savebreath":
                saveBreath=!saveBreath;
                break;
            case "stunTime":
                stunTime=(int)value;
        }
    }

    public Object getFlag(String flag) {
        switch (flag) {
            case "rideable":
                return rideable;
            case "savebreath":
                return saveBreath;
            case "stunTime":
                return stunTime;
            default:
                return null;
        }
    }
}
