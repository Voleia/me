package me.voleia.volands.Races;

import me.voleia.volands.Abilities.Ability;
import me.voleia.volands.Enums.ExpType;
import me.voleia.volands.SkillTree.Skill;
import me.voleia.volands.SkillTree.SkillTree;
import me.voleia.volands.Volands;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Demon implements RaceClass {

    PotionEffect fireRes = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE,0,false,false,false);
    PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION,Integer.MAX_VALUE,0,false,false,true);

    public Demon() {
        multipliers.put(ExpType.COMBAT, 1.7);
        multipliers.put(ExpType.FARMING, 0.6);
        multipliers.put(ExpType.MINING, 1.1);
        multipliers.put(ExpType.HUNTING,0.6);
    }

    HashMap<ExpType, Double> multipliers = new HashMap<>();

    public double getMultiplier(ExpType type) {
        return multipliers.get(type);
    }

    public void onTick(Player p, int level) {
        p.addPotionEffect(fireRes);
        if (level>=3 && p.isInLava()) {
            p.addPotionEffect(regen);
        } else {
            removeArtificialPotion(p,PotionEffectType.REGENERATION);
        }
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
        return false;
    }


    void removeArtificialPotion(Player p, PotionEffectType pot) {
        if (p.hasPotionEffect(pot) && p.getPotionEffect(pot).getDuration()>500000) {
            p.removePotionEffect(pot);
        }
    }

    public Ability[] getAbilities() {
        return new Ability[0];
    }

    String name = Volands.getSystem().translate("Demon");

    public String getDisplayName() {
        return name;
    }

    public void setFlag(String flag, Object value) {

    }

    public Object getFlag(String flag) {
        return null;
    }
}
