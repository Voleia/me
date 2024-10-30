package me.voleia.volands.Races;

import me.voleia.volands.Abilities.Ability;
import me.voleia.volands.Enums.ExpType;
import me.voleia.volands.SkillTree.Skill;
import me.voleia.volands.SkillTree.SkillTree;
import me.voleia.volands.Volands;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Ambystoma implements RaceClass {

    PotionEffect waterBreathing = new PotionEffect(PotionEffectType.WATER_BREATHING,Integer.MAX_VALUE,0,false,false,false);
    PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION,Integer.MAX_VALUE,0,false,false,true);
    PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE,Integer.MAX_VALUE,0,false,false,true);
    PotionEffect dolphinsGrace = new PotionEffect(PotionEffectType.DOLPHINS_GRACE,Integer.MAX_VALUE,0,false,false,false);
    PotionEffect conduit = new PotionEffect(PotionEffectType.CONDUIT_POWER,Integer.MAX_VALUE,0,false,false,false);
    PotionEffect vision = new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,0,false,false,false);
    Ability[] abilities = new Ability[1];

    public Ambystoma() {
        abilities[0]= Volands.getSystem().getAbilities()[7];
        multipliers.put(ExpType.COMBAT, 1.0);
        multipliers.put(ExpType.FARMING, 1.1);
        multipliers.put(ExpType.MINING, 1.1);
        multipliers.put(ExpType.HUNTING,0.8);
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
        return nightVisionEnabled;
    }

    public void onTick(Player p, int level) {
        p.addPotionEffect(waterBreathing);
        if (p.isInWater()) {
            p.addPotionEffect(regen);
            if (level>=2) {
                p.addPotionEffect(conduit);
                if (nightVisionEnabled) {
                    p.addPotionEffect(vision);
                } else {
                    removeArtificialPotion(p,PotionEffectType.NIGHT_VISION);
                }
                if (level>=3) {
                    if (!p.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) {
                        p.sendMessage(ChatColor.GOLD + "You have been strengthened by the waves!");
                    }
                    p.addPotionEffect(strength);
                    p.addPotionEffect(dolphinsGrace);
                }
            }
        } else {
            removeArtificialPotion(p,PotionEffectType.NIGHT_VISION);
            removeArtificialPotion(p,PotionEffectType.REGENERATION);
            removeArtificialPotion(p,PotionEffectType.INCREASE_DAMAGE);
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

    String name = Volands.getSystem().translate("Ambystoma");

    public String getDisplayName() {
        return name;
    }

    boolean nightVisionEnabled = false;

    public void setFlag(String flag, Object value) {
        nightVisionEnabled=!nightVisionEnabled;
    }

    public Object getFlag(String flag) {
        return nightVisionEnabled;
    }
}
