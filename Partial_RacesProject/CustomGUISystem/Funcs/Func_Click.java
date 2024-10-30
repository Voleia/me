package me.voleia.volands.CustomGUISystem.Funcs;

import me.voleia.volands.CustomGUISystem.CustomGUI;
import me.voleia.volands.OC;
import me.voleia.volands.SkillTree.Skill;
import me.voleia.volands.SkillTree.SkillStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Func_Click implements InventoryFunc{

    Skill mySkill;
    public Func_Click(Skill skill) {
        mySkill=skill;
    }

    @Override
    public void run(OC oc, Player sender, CustomGUI inv) {
        if (mySkill.getSkillStatus().equals(SkillStatus.UNLOCKABLE)) {
            mySkill.tree.setSkill(mySkill, true);
            for (Material mat : mySkill.itemsToRemove.keySet()) {
                mySkill.removeItem(mat, mySkill.getAmountOfItem(mat));
            }
            mySkill.tree.getOC().setXP((int) (mySkill.tree.getOC().getXP()-mySkill.price));
            inv.setContents();
            inv.createUI();
        }
    }

    @Override
    public void altRun(OC oc, Player sender, CustomGUI inv) {
        if (mySkill.parent!=null && mySkill.getSkillStatus().equals(SkillStatus.UNLOCKED)) {
            for (Skill child : mySkill.children) {
                if (child.getSkillStatus().equals(SkillStatus.UNLOCKED)) {
                    return;
                }
            }
            mySkill.tree.setSkill(mySkill, false);
            inv.setContents();
            inv.createUI();
        }
    }
}
