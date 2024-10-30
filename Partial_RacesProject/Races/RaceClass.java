package me.voleia.volands.Races;

import me.voleia.volands.Abilities.Ability;
import me.voleia.volands.Enums.ExpType;
import me.voleia.volands.OC;
import me.voleia.volands.SkillTree.Skill;
import me.voleia.volands.SkillTree.SkillTree;
import me.voleia.volands.SkillTree.SkillType;
import org.bukkit.entity.Player;

import java.util.List;

public interface RaceClass {
    public void onTick(Player p, OC oc); //passive abilities
    public Ability[] getAbilities(); //active abilities
    public String getDisplayName();
    public void setFlag(String flag, Object value);
    public Object getFlag(String flag);
    public double getMultiplier(ExpType type);
    public List<String> getInfo();
    public boolean getToggledAbility();
    public void loadTree(SkillTree tree);
    public void updateTree(SkillType skill, boolean value);
}
