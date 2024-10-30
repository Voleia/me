package me.voleia.volands.Races;

import me.voleia.volands.Abilities.Ability;
import me.voleia.volands.Enums.ExpType;
import me.voleia.volands.SkillTree.Skill;
import me.voleia.volands.SkillTree.SkillTree;
import me.voleia.volands.Volands;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Dwarf implements RaceClass {

    Ability[] abilities = new Ability[1];
    boolean IIIxIII=false;

    public Dwarf() {
        abilities[0]=Volands.getSystem().getAbilities()[0];
        multipliers.put(ExpType.COMBAT, 1.0);
        multipliers.put(ExpType.FARMING, 0.5);
        multipliers.put(ExpType.MINING, 1.7);
        multipliers.put(ExpType.HUNTING,0.8);
    }

    HashMap<ExpType, Double> multipliers = new HashMap<>();

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

    public double getMultiplier(ExpType type) {
        return multipliers.get(type);
    }

    public void onTick(Player p, int level) {

    }

    public Ability[] getAbilities() {
        return abilities;
    }

    String name = Volands.getSystem().translate("Dwarf");

    public String getDisplayName() {
        return name;
    }

    public void setFlag(String flag, Object value) {
        if (flag.equals("3by3")) {
            IIIxIII=(boolean)value;
        }
    }

    public Object getFlag(String flag) {
        if (flag.equals("3by3")) {
            return IIIxIII;
        }
        return null;
    }
}
