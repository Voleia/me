package me.voleia.volands.SkillTree;

import me.voleia.volands.Enums.Race;
import me.voleia.volands.OC;

import java.util.HashMap;

public class TreeElf implements SkillTree{
    @Override
    public Skill constructTree() {
        return null;
    }

    @Override
    public Race getRace() {
        return null;
    }

    @Override
    public OC getOC() {
        return null;
    }

    @Override
    public void setSkill(Skill skill, boolean val) {

    }

    @Override
    public boolean hasSkill(SkillType type) {
        return false;
    }

    @Override
    public HashMap<SkillType, Boolean> getMap() {
        return null;
    }
}
