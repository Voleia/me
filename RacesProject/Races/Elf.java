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

public class Elf implements RaceClass {

    PotionEffect speedI = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE,0,false,false,false);
    Ability[] abilities = new Ability[1];

    public Elf() {
        abilities[0]=Volands.getSystem().getAbilities()[1];
        multipliers.put(ExpType.COMBAT, 1.3);
        multipliers.put(ExpType.FARMING, 1.5);
        multipliers.put(ExpType.MINING, 0.6);
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
        return false;
    }

    public void onTick(Player p, int level) {
        p.addPotionEffect(speedI);
    }

    public Ability[] getAbilities() {
        return abilities;
    }

    String name = Volands.getSystem().translate("Elf");

    public String getDisplayName() {
        return name;
    }

    public void setFlag(String flag, Object value) {
        //no flags
    }

    public Object getFlag(String flag) {
        return null;
    }
}
