package me.voleia.volands.SkillTree;

import me.voleia.volands.Volands;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Skill { //Make "skills" a static class
    public ArrayList<Skill> children;
    public ItemStack skillStack;
    public List<SkillType> incompatibilities;
    public SkillTree tree;
    public Skill parent;
    public SkillType type;
    public double price;
    public SkillCat category;
    public HashMap<Material, Integer> itemsToRemove = new HashMap<>();
    public int x;
    public String ogName;
    List<String> ogLore;

    public Skill(SkillType type_, List<String> lore, SkillTree tree_, double xpPrice, SkillCat cat) {
        children = new ArrayList<>();
        ogName=type_.toString().replace("_", " ");
        incompatibilities = new ArrayList<>();
        switch (cat) {
            case ACTIVE:
                skillStack = Volands.getSystem().getSkillItemStack(InvIcon.ACTIVE_LOCKED, ogName, lore);
            case TOGGLE:
                skillStack = Volands.getSystem().getSkillItemStack(InvIcon.TOGGLE_LOCKED, ogName, lore);
            case PASSIVE:
            default:
                skillStack = Volands.getSystem().getSkillItemStack(InvIcon.PASSIVE_LOCKED, ogName, lore);
        }
        ogLore=lore;
        parent = null;
        tree = tree_;
        type=type_;
        price = xpPrice;
        category = cat;
    }

    public void addCost(Material item, int price) {
        itemsToRemove.put(item,price);
    }

    public void addChild(Skill child) {
        children.add(child);
        child.parent = this;
    }

    public boolean addIncompatibility(SkillType iType) { //can only add incompatibilities for "sister nodes"
        if (parent!=null) {
            Skill sister = null;
            for (Skill skill : parent.children) {
                if (skill.type.equals(iType)) {
                    sister = skill;
                    break;
                }
            }
            if (sister==null) {
                return false;
            }
            sister.incompatibilities.add(type);
            incompatibilities.add(iType);
        }
        return false;
    }

    public Skill getTopParent() {
        if (parent==null) {
            return this;
        }
        return parent.getTopParent();
    }

    public SkillStatus getSkillStatus() {
        if (parent!=null) {
            if (tree.hasSkill(type)) {
                return SkillStatus.UNLOCKED;
            }
            if (tree.hasSkill(parent.type)) {
                boolean canUnlock = true;
                if (tree.getOC().getXP() < price) {
                    return SkillStatus.EXPENSIVE;
                }

                for (Material mat : itemsToRemove.keySet()) {
                    if (getAmountOfItem(mat)<itemsToRemove.get(mat)) {
                        canUnlock=false;
                    }
                }

                if (canUnlock) {
                    return SkillStatus.UNLOCKABLE;
                } else {
                    return SkillStatus.EXPENSIVE;
                }
            }
            return SkillStatus.LOCKED;
        }
        return SkillStatus.UNLOCKED;
    }

    public void updateItemStackFromSkillStatus() {
        String actual = "";
        ArrayList<String> newLore = new ArrayList<>();
        switch (category) {
            case TOGGLE:
                actual="TOGGLE_";
                newLore.add(Volands.ptToggleable);
                break;
            case ACTIVE:
                actual="ACTIVE_";
                newLore.add(Volands.ptActive);
                break;
            case PASSIVE:
            default:
                actual="PASSIVE_";
                newLore.add(Volands.ptPassive);
                break;
        }

        switch (getSkillStatus()) {
            case LOCKED:
                actual+="LOCKED";
                newLore.set(0,newLore.get(0)+"&7, "+Volands.ptLocked);
                break;
            case UNLOCKED:
                actual+="UNLOCKED";
                newLore.set(0,newLore.get(0)+"&7, "+Volands.ptUnlocked);
                break;
            case EXPENSIVE:
                actual+="EXPENSIVE";
                newLore.set(0,newLore.get(0)+"&7, "+Volands.ptExpensive);
                break;
            case UNLOCKABLE:
            default:
                actual+="AVAILIBLE";
                newLore.set(0,newLore.get(0)+"&7, "+ Volands.ptAvailible);
                break;
        }
        newLore.add("&7&lAbilities:");
        newLore.addAll(ogLore);
        if (parent!=null && parent.parent!=null) {
            newLore.add(" ");
            newLore.add("&7&lRequires:");
            newLore.addAll(parent.getSkillListToTop(new ArrayList<String>()));
        }
        newLore.add(" ");
        newLore.add("&7&lCosts:");
        if (tree.getOC().getXP() >= price) {
            newLore.add("&a - Race XP: " + price + " (you have " + Math.round(tree.getOC().getXP()) +")");
        } else {
            newLore.add("&c - Race XP: " + price + " (you have " + Math.round(tree.getOC().getXP()) +")");
        }
        int size = itemsToRemove.size();
        for (Material mat : itemsToRemove.keySet()) {
            int amount = getAmountOfItem(mat);
            if (amount>=itemsToRemove.get(mat)) {
                newLore.add("&a - " + mat.toString() + ": " + itemsToRemove.get(mat) + " (you have " + amount+")");
            } else {
                newLore.add("&c - " + mat.toString() + ": " + itemsToRemove.get(mat) + " (you have " + amount+")");
            }
        }
        if (!incompatibilities.isEmpty()) {
            newLore.add(" ");
            newLore.add("&7&lIncompatible With:");
            for (Skill child : parent.children) {
                if (incompatibilities.contains(child.type)) {
                    newLore.add("&c - " + child.ogName);
                }
            }
        }
        newLore.add(" ");
        newLore.add("&8Left click to unlock");
        newLore.add("&8Double right click to re-lock (NO REFUNDS)");

        skillStack = Volands.getSystem().getSkillItemStack(InvIcon.valueOf(actual), "&f"+ogName, newLore);
    }

    public int getAmountOfItem(Material item) {
        int amount = 0;
        Inventory inv = tree.getOC().getPlayer().getInventory();
        for (ItemStack stack : inv.getContents()) {
            if (stack!=null && stack.getType().equals(item)) {
                amount+=stack.getAmount();
            }
        }
        return amount;
    }

    public boolean removeItem(Material item, int amount) {
        if (getAmountOfItem(item)<amount) {
            return false;
        } else {
            Inventory inv = tree.getOC().getPlayer().getInventory();
            int size = inv.getSize();
            for (int i = 0; i < size; i++) {
                if (inv.getItem(i)!=null && inv.getItem(i).getType().equals(item)) {
                    if (inv.getItem(i).getAmount() > amount) {
                        inv.getItem(i).setAmount(inv.getItem(i).getAmount()-amount);
                        return true;
                    } else if (inv.getItem(i).getAmount()==amount) {
                        inv.getItem(i).setAmount(0);
                        return true;
                    } else {
                        amount-=inv.getItem(i).getAmount();
                        inv.getItem(i).setAmount(0);
                    }
                }
            }
        }
        return true;
    }

    public List<String> getSkillListToTop(List<String> cur) {
        List<String> newStr = cur;
        if (parent!=null) {
            if (tree.hasSkill(type)) {
                newStr.add("&a + "+ogName);
            } else {
                newStr.add("&c - "+ogName);
            }
            return parent.getSkillListToTop(newStr);
        } else {
            return newStr;
        }
    }

    public SkillStatus getSkillStatusOfChild(SkillType goal) {
        Skill temp = getSkillInChildren(goal);
        if (temp!=null) {
            return temp.getSkillStatus();
        } else {
            return SkillStatus.NONEXISTANT;
        }
    }

    public Skill getSkillInChildren(SkillType goal) {
        if (goal==type) {
            return this;
        } else {
            for (Skill child : children) {
                Skill temp = child.getSkillInChildren(goal);
                if (temp!=null) {
                    return temp;
                }
            }
            return null;
        }
    }

    public List<String> getUnlockedSkills(List<String> cur, List<Skill> skillsToCheck) {
        List<String> newCur = cur;
        for (Skill skill : skillsToCheck) {
            if (tree.hasSkill(skill.type)) {
                newCur.add(ChatColor.translateAlternateColorCodes('&', "&a - " + skill.ogName));
            }
            newCur.addAll(getUnlockedSkills(newCur, skill.children));
        }
        return newCur;
    }
}



//Store an enum in the "skill-tree" function, but don't construct actual 'skills' until going into the unlocking menu.
//This will prevent memory overload.
//Pass a skill tree into the customGUI, and make it so when skills change value, they alert the parent class which will set them to enabled.
//Actual abilities will be done through abilities, tick statements, etc.