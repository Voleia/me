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

public class Wraith implements RaceClass {

    Player cur;

    PotionEffect slowFall = new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE,0,false,false,true);
    Ability[] abilities = new Ability[2];

    public Wraith() {
        abilities[1]= Volands.getSystem().getAbilities()[6];
        abilities[0]= Volands.getSystem().getAbilities()[5];
        multipliers.put(ExpType.COMBAT, 1.7);
        multipliers.put(ExpType.FARMING, 0.6);
        multipliers.put(ExpType.MINING, 0.8);
        multipliers.put(ExpType.HUNTING,0.8);
    }

    HashMap<ExpType, Double> multipliers = new HashMap<>();

    public double getMultiplier(ExpType type) {
        return multipliers.get(type);
    }

    public void onTick(Player p, int level) {
        cur = p;
        if (level>=2 && p.isSneaking()) {
            p.addPotionEffect(slowFall);
        } else {
            removeArtificialPotion(p,PotionEffectType.SLOW_FALLING);
        }
    }

    @Override
    public boolean getToggledAbility() {
        return cur.hasPotionEffect(PotionEffectType.NIGHT_VISION);
    }

    public List<String> getInfo() {
        return Arrays.asList(new String[]{"TBA"});
    }

    public void loadTree(SkillTree tree) {

    }

    public void updateTree(Skill skill, boolean value) {

    }

    void removeArtificialPotion(Player p, PotionEffectType pot) {
        if (p.hasPotionEffect(pot) && p.getPotionEffect(pot).getDuration()>500000) {
            p.removePotionEffect(pot);
        }
    }

    public Ability[] getAbilities() {
        return abilities;
    }

    String name = Volands.getSystem().translate("Wraith");

    public String getDisplayName() {
        return name;
    }

    public void setFlag(String flag, Object value) {

    }

    public Object getFlag(String flag) {
        return null;
    }
}
