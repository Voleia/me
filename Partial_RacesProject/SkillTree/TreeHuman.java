package me.voleia.volands.SkillTree;

import me.voleia.volands.Enums.Race;
import me.voleia.volands.OC;
import me.voleia.volands.Volands;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TreeHuman implements SkillTree {
    HashMap<SkillType, Boolean> unlockedSkills = new HashMap<>();
    OC oc;

    public TreeHuman(OC oc_, List<String> current) {
        oc = oc_;
        unlockedSkills.put(SkillType.Human_Lineage, true);
        for (String cur : current) {
            try {
                unlockedSkills.put(SkillType.valueOf(cur), true);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public Skill constructTree() {
        Skill HumanQualities = new Skill(SkillType.Human_Lineage, Arrays.asList("&f - The base skill for all things human"), this, 0, SkillCat.PASSIVE);
            Skill FastLearner = new Skill(SkillType.Fast_Learner, Arrays.asList("&f - Unlocks the 'XP Multiplier' Skill Tree"), this, 0, SkillCat.PASSIVE);
                Skill FarmingProwessI = new Skill(SkillType.Farming_Prowess_1, Arrays.asList("&2 - 1.2x Farming Race XP Multiplier"), this, 1000, SkillCat.PASSIVE);
                    Skill FastFarmingI = new Skill(SkillType.Fast_Farming_1, Arrays.asList("&2 - 1.5x crop drops while farming"), this, 2500, SkillCat.PASSIVE);
                        Skill FastFarmingII = new Skill(SkillType.Fast_Farming_2, Arrays.asList("&2 - 2x crop drops while farming"), this, 5000, SkillCat.PASSIVE);
                        FastFarmingII.addCost(Material.WHEAT, 256);
                        FastFarmingI.addChild(FastFarmingII);
                    Skill FarmingProwessII = new Skill(SkillType.Farming_Prowess_2, Arrays.asList("&2 - 1.5x Farming Race XP Multiplier"), this, 3000, SkillCat.PASSIVE);
                        Skill FarmingProwessIII = new Skill(SkillType.Farming_Prowess_3, Arrays.asList("&2 - 2x Farming Race XP Multiplier"), this, 7500, SkillCat.PASSIVE);
                        FarmingProwessII.addChild(FarmingProwessIII);
                    FarmingProwessI.addChild(FastFarmingI);
                    FarmingProwessI.addChild(FarmingProwessII);
                FastLearner.addChild(FarmingProwessI);

                Skill CombatProwessI = new Skill(SkillType.Combat_Prowess_1, Arrays.asList("&2 - 1.2x Combat Race XP Multiplier"), this, 1000, SkillCat.PASSIVE);
                    Skill CombatProwessII = new Skill(SkillType.Combat_Prowess_2, Arrays.asList("&2 - 1.5x Combat Race XP Multiplier"), this, 1500, SkillCat.PASSIVE);
                        Skill CombatProwessIII = new Skill(SkillType.Combat_Prowess_3, Arrays.asList("&2 - 2x Combat Race XP Multiplier"), this, 4000, SkillCat.PASSIVE);
                        CombatProwessII.addChild(CombatProwessIII);
                    CombatProwessI.addChild(CombatProwessII);
                FastLearner.addChild(CombatProwessI);

                Skill Hunting_ProwessI = new Skill(SkillType.Hunting_Prowess_1, Arrays.asList("&2 - 1.2x Hunting Race XP Multiplier"), this, 1000, SkillCat.PASSIVE);
                    Skill Hunting_ProwessII = new Skill(SkillType.Hunting_Prowess_2, Arrays.asList("&2 - 1.5x Hunting Race XP Multiplier"), this, 1500, SkillCat.PASSIVE);
                        Skill Hunting_ProwessIII = new Skill(SkillType.Hunting_Prowess_3, Arrays.asList("&2 - 2x Hunting Race XP Multiplier"), this, 4000, SkillCat.PASSIVE);
                        Hunting_ProwessII.addChild(Hunting_ProwessIII);
                    Hunting_ProwessI.addChild(Hunting_ProwessII);
                FastLearner.addChild(Hunting_ProwessI);

                Skill Mining_ProwessI = new Skill(SkillType.Mining_Prowess_1, Arrays.asList("&2 - 1.2x Mining Race XP Multiplier"), this, 1000, SkillCat.PASSIVE);
                    Skill Mining_ProwessII = new Skill(SkillType.Mining_Prowess_2, Arrays.asList("&2 - 1.5x Mining Race XP Multiplier"), this, 1500, SkillCat.PASSIVE);
                        Skill Mining_ProwessIII = new Skill(SkillType.Mining_Prowess_3, Arrays.asList("&2 - 2x Mining Race XP Multiplier"), this, 4000, SkillCat.PASSIVE);
                        Mining_ProwessII.addChild(Mining_ProwessIII);
                    Mining_ProwessI.addChild(Mining_ProwessII);
                FastLearner.addChild(Mining_ProwessI);
            HumanQualities.addChild(FastLearner);

            Skill Denizen_of_the_Plains = new Skill(SkillType.Denizen_of_the_Plains, Arrays.asList("&2 - Strength 1 in the plains"), this, 5000, SkillCat.PASSIVE);
                Skill Custodian_of_the_Wood = new Skill(SkillType.Custodian_of_the_Wood, Arrays.asList("&2 - Strength 1 while in the forest", "&2 - +2 hearts while in the forest"), this, 5000, SkillCat.PASSIVE);
                    Skill One_with_Nature = new Skill(SkillType.One_with_Nature, Arrays.asList("&2 - Regen 1 in all forest biomes", "&2 - +4 hearts (total) while in forest biomes"), this, 15000, SkillCat.PASSIVE);
                    One_with_Nature.addCost(Material.ROTTEN_FLESH, 576);
                    Custodian_of_the_Wood.addChild(One_with_Nature);
                Denizen_of_the_Plains.addChild(Custodian_of_the_Wood);

                Skill Nomad_of_the_Steppe = new Skill(SkillType.Nomad_of_the_Steppe, Arrays.asList("&2 - Strength 1 in the Savannah", "&2 - +2 hearts while in the savannah"), this, 5000, SkillCat.PASSIVE);
                    Skill Sand_Surfer = new Skill(SkillType.Sand_Surfer, Arrays.asList("&2 - Regen 1 in all Savannah & Desert biomes", "&2 - Strength 1 in the desert","&2 - +4 hearts (total) while in Savannah & Desert biomes"), this, 15000, SkillCat.PASSIVE);
                    Sand_Surfer.addCost(Material.ENDER_PEARL, 144);
                    Nomad_of_the_Steppe.addChild(Sand_Surfer);
                Denizen_of_the_Plains.addChild(Nomad_of_the_Steppe);

                Skill Commodore = new Skill(SkillType.Commodore, Arrays.asList("&2 - Regen 1 while in (including above) ocean biomes", "&2 - +4 hearts while in ocean biomes"), this, 5000, SkillCat.PASSIVE);
                    Skill Admiral = new Skill(SkillType.Admiral, Arrays.asList("&2 - Strength 1 while in ocean biomes", "&2 - +6 hearts (total) while in ocean biomes", "&2 - Can hold breath for double the time"), this, 15000, SkillCat.PASSIVE);
                    Admiral.addCost(Material.TRIDENT, 1);
                    Commodore.addChild(Admiral);
                Denizen_of_the_Plains.addChild(Commodore);

                Custodian_of_the_Wood.addIncompatibility(SkillType.Nomad_of_the_Steppe);
                Custodian_of_the_Wood.addIncompatibility(SkillType.Commodore);
                Nomad_of_the_Steppe.addIncompatibility(SkillType.Commodore);
            HumanQualities.addChild(Denizen_of_the_Plains);
        return HumanQualities;
    }

    public Race getRace() {
        return Race.HUMAN;
    }

    public OC getOC() {
        return oc;
    }

    public void setSkill(Skill skill, boolean val) {
        unlockedSkills.put(skill.type, val);
        oc.getRaceClass().updateTree(skill.type,val);
    }

    public boolean hasSkill(SkillType type) {
        Boolean val = unlockedSkills.get(type);
        if (val == null || !val) {
            return false;
        }
        return true;
    }

    @Override
    public HashMap<SkillType, Boolean> getMap() {
        return unlockedSkills;
    }
}
