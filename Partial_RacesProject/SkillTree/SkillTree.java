package me.voleia.volands.SkillTree;

import me.voleia.volands.Enums.Race;
import me.voleia.volands.OC;

import java.util.HashMap;

public interface SkillTree {
    Skill constructTree();
    Race getRace();
    OC getOC();
    void setSkill(Skill skill, boolean val);
    boolean hasSkill(SkillType type);
    HashMap<SkillType, Boolean> getMap();
}
