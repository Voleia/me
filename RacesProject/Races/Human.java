package me.voleia.volands.Races;

import me.voleia.volands.Abilities.Ability;
import me.voleia.volands.Enums.ExpType;
import me.voleia.volands.OC;
import me.voleia.volands.SkillTree.Skill;
import me.voleia.volands.SkillTree.SkillTree;
import me.voleia.volands.SkillTree.SkillType;
import me.voleia.volands.SkillTree.TreeHuman;
import me.voleia.volands.StaticPresets;
import me.voleia.volands.Volands;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Human implements RaceClass {

    Ability[] abilities;
    Volands system;
    HashMap<String, Object> flags = new HashMap<>();

    PotionEffect strengthI = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE,0,false,false,true);
    PotionEffect regenI = new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE,0,false,false,true);

    public Human() {
        abilities = new Ability[0];
        system = Volands.getSystem();
    }

    HashMap<ExpType, Double> multipliers = new HashMap<>();

    public double getMultiplier(ExpType type) {
        return multipliers.get(type);
    }

    public void onTick(Player p, OC oc) {
        SkillTree tree = oc.getTree();

        boolean giveStrengthI = false;
        boolean giveRegenI = false;

        if (tree.hasSkill(SkillType.Denizen_of_the_Plains)) {
            if (tree.hasSkill(SkillType.One_with_Nature)) { //upper skill
                if(StaticPresets.playerIsInBiome(p, new Biome[]{Biome.FOREST, Biome.FLOWER_FOREST, Biome.BIRCH_FOREST, Biome.OLD_GROWTH_BIRCH_FOREST, Biome.DARK_FOREST, Biome.WINDSWEPT_FOREST})) {
                    giveStrengthI=true;
                    giveRegenI=true;
                    StaticPresets.setPlayerHealth(p, 28);
                } else {
                    StaticPresets.setPlayerHealth(p, 20);
                }
            } else if (tree.hasSkill(SkillType.Custodian_of_the_Wood)) { //lesser skill
                if(StaticPresets.playerIsInBiome(p, new Biome[]{Biome.FOREST, Biome.FLOWER_FOREST, Biome.BIRCH_FOREST, Biome.OLD_GROWTH_BIRCH_FOREST, Biome.DARK_FOREST, Biome.WINDSWEPT_FOREST})) {
                    giveStrengthI=true;
                    StaticPresets.setPlayerHealth(p, 24);
                } else {
                    StaticPresets.setPlayerHealth(p, 20);
                }
            } else if (tree.hasSkill(SkillType.Sand_Surfer)) { //upper skill
                if(StaticPresets.playerIsInBiome(p, new Biome[]{Biome.SAVANNA, Biome.WINDSWEPT_SAVANNA, Biome.SAVANNA_PLATEAU, Biome.DESERT})) {
                    giveStrengthI=true;
                    giveRegenI=true;
                    StaticPresets.setPlayerHealth(p, 28);
                } else {
                    StaticPresets.setPlayerHealth(p, 20);
                }
            } else if (tree.hasSkill(SkillType.Nomad_of_the_Steppe)) { //lesser skill
                if(StaticPresets.playerIsInBiome(p, new Biome[]{Biome.SAVANNA, Biome.SAVANNA_PLATEAU, Biome.WINDSWEPT_SAVANNA})) {
                    giveStrengthI=true;
                    StaticPresets.setPlayerHealth(p, 24);
                } else {
                    StaticPresets.setPlayerHealth(p, 20);
                }
            } else if (tree.hasSkill(SkillType.Admiral)) { //upper skill

            } else if (tree.hasSkill(SkillType.Commodore)) { //lesser skill

            }
            if (!giveStrengthI) {
                giveStrengthI = StaticPresets.playerIsInBiome(p, new Biome[]{Biome.SUNFLOWER_PLAINS, Biome.PLAINS});
            }
        }

        /*if (level>=3) {
            Biome biome = p.getLocation().getBlock().getBiome();
            if (biome.equals(Biome.PLAINS) || biome.equals(Biome.SUNFLOWER_PLAINS) || biome.equals(Biome.FOREST) || biome.equals(Biome.FLOWER_FOREST)) {
                p.addPotionEffect(strength);
            } else {
                removeArtificialPotion(p,PotionEffectType.INCREASE_DAMAGE);
            }
        }*/
    }

    public List<String> getInfo() {
        return Arrays.asList(new String[]{
                "Humans are the undisputed masters",
                "of the overworld realms."
        });
    }

    public void loadTree(SkillTree tree) {
        if (tree.hasSkill(SkillType.Farming_Prowess_3)) { multipliers.put(ExpType.FARMING, 2.0); }
        else if (tree.hasSkill(SkillType.Farming_Prowess_2)) { multipliers.put(ExpType.FARMING, 1.5); }
        else if (tree.hasSkill(SkillType.Farming_Prowess_1)) { multipliers.put(ExpType.FARMING, 1.2); }
        else { multipliers.put(ExpType.FARMING, 1.0); }

        if (tree.hasSkill(SkillType.Combat_Prowess_3)) { multipliers.put(ExpType.COMBAT, 2.0); }
        else if (tree.hasSkill(SkillType.Combat_Prowess_2)) { multipliers.put(ExpType.COMBAT, 1.5); }
        else if (tree.hasSkill(SkillType.Combat_Prowess_1)) { multipliers.put(ExpType.COMBAT, 1.2); }
        else { multipliers.put(ExpType.COMBAT, 1.0); }

        if (tree.hasSkill(SkillType.Mining_Prowess_3)) { multipliers.put(ExpType.MINING, 2.0); }
        else if (tree.hasSkill(SkillType.Mining_Prowess_2)) { multipliers.put(ExpType.MINING, 1.5); }
        else if (tree.hasSkill(SkillType.Mining_Prowess_1)) { multipliers.put(ExpType.MINING, 1.2); }
        else { multipliers.put(ExpType.MINING, 1.0); }

        if (tree.hasSkill(SkillType.Hunting_Prowess_3)) { multipliers.put(ExpType.HUNTING, 2.0); }
        else if (tree.hasSkill(SkillType.Hunting_Prowess_2)) { multipliers.put(ExpType.HUNTING, 1.5); }
        else if (tree.hasSkill(SkillType.Hunting_Prowess_1)) { multipliers.put(ExpType.HUNTING, 1.2); }
        else { multipliers.put(ExpType.HUNTING, 1.0); }
    }

    public void updateTree(SkillType skill, boolean value) {
        switch (skill) {
            case Farming_Prowess_1:
                if (value) { multipliers.put(ExpType.FARMING, 1.2); }
                else { multipliers.put(ExpType.FARMING, 1.0); }
                break;
            case Farming_Prowess_2:
                if (value) { multipliers.put(ExpType.FARMING, 1.5); }
                else { multipliers.put(ExpType.FARMING, 1.2); }
                break;
            case Farming_Prowess_3:
                if (value) { multipliers.put(ExpType.FARMING, 2.0); }
                else { multipliers.put(ExpType.FARMING, 1.5); }
                break;
            case Combat_Prowess_1:
                if (value) { multipliers.put(ExpType.COMBAT, 1.2); }
                else { multipliers.put(ExpType.COMBAT, 1.0); }
                break;
            case Combat_Prowess_2:
                if (value) { multipliers.put(ExpType.COMBAT, 1.5); }
                else { multipliers.put(ExpType.COMBAT, 1.2); }
                break;
            case Combat_Prowess_3:
                if (value) { multipliers.put(ExpType.COMBAT, 2.0); }
                else { multipliers.put(ExpType.COMBAT, 1.5); }
                break;
            case Hunting_Prowess_1:
                if (value) { multipliers.put(ExpType.HUNTING, 1.2); }
                else { multipliers.put(ExpType.HUNTING, 1.0); }
                break;
            case Hunting_Prowess_2:
                if (value) { multipliers.put(ExpType.HUNTING, 1.5); }
                else { multipliers.put(ExpType.HUNTING, 1.2); }
                break;
            case Hunting_Prowess_3:
                if (value) { multipliers.put(ExpType.HUNTING, 2.0); }
                else { multipliers.put(ExpType.HUNTING, 1.5); }
                break;
            case Mining_Prowess_1:
                if (value) { multipliers.put(ExpType.MINING, 1.2); }
                else { multipliers.put(ExpType.MINING, 1.0); }
                break;
            case Mining_Prowess_2:
                if (value) { multipliers.put(ExpType.MINING, 1.5); }
                else { multipliers.put(ExpType.MINING, 1.2); }
                break;
            case Mining_Prowess_3:
                if (value) { multipliers.put(ExpType.MINING, 2.0); }
                else { multipliers.put(ExpType.MINING, 1.5); }
                break;
        }
    }

    @Override
    public boolean getToggledAbility() {
        return false;
    }

    public Ability[] getAbilities() {
        return abilities;
    }

    String name = Volands.getSystem().translate("Human");

    public String getDisplayName() {
        return name;
    }

    public void setFlag(String flag, Object value) {

    }

    public Object getFlag(String flag) {
        return flags.get(flag);
    }
}
