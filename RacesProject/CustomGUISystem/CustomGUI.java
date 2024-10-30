package me.voleia.volands.CustomGUISystem;

import me.voleia.volands.CustomGUISystem.Funcs.Func_Click;
import me.voleia.volands.CustomGUISystem.Funcs.Func_Ignore;
import me.voleia.volands.CustomGUISystem.Funcs.Func_Scroll;
import me.voleia.volands.OC;
import me.voleia.volands.SkillTree.InvIcon;
import me.voleia.volands.SkillTree.Skill;
import me.voleia.volands.SkillTree.SkillTree;
import me.voleia.volands.Volands;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomGUI {
    public Inventory viewport;
    InvGrid grid;
    OC oc;
    SkillTree tree;

    public CustomGUI(int height, String title, OC oc_, SkillTree tree_) {
        oc = oc_;
        grid = new InvGrid(height);
        viewport = Bukkit.createInventory(null, height*9, title);

        tree = tree_;

        grid.rightOffset = setContents()/2 - 4;
        if (grid.rightOffset<0) {
            grid.rightOffset=0;
        }
        createUI();
        Volands.getSystem().getRaceListener().addGUI(this);
        oc.getPlayer().openInventory(viewport);
    }

    public int setContents() { //int returns the rightmost int
        rightmostPoint=0;
        Skill top = tree.constructTree();
        setBottomLeftmostUnexploredChild(top, 0);
        return rightmostPoint;
    }

    int rightmostPoint = 0;

    public void setBottomLeftmostUnexploredChild(Skill cur, int y) {
        cur.updateItemStackFromSkillStatus();
        for (Skill child : cur.children) {
            setBottomLeftmostUnexploredChild(child, y+1);
        }
        if (cur.children.isEmpty()) {
            grid.set(y, rightmostPoint, new InvSlot(cur.skillStack, new Func_Click(cur)));
            cur.x = rightmostPoint;
            rightmostPoint+=2;
        } else {
            int dif = cur.children.get(cur.children.size()-1).x-cur.children.get(0).x;
            int newPos = (rightmostPoint-2)-(dif/2);
            cur.x = newPos;
            grid.set(y, newPos, new InvSlot(cur.skillStack, new Func_Click(cur)));
            if (grid.get(y+1,newPos)==null) {
                grid.set(y+1, newPos, new InvSlot(Volands.getSystem().getSkillItemStack(InvIcon.T), new Func_Ignore()));
            }
            int lastx = cur.children.get(cur.children.size()-1).x;
            Skill curChild = cur.children.get(0);
            int curChildIndex = 0;
            for (int x = cur.children.get(0).x; x < lastx; x++) {
                if (grid.get(y+1, x) == null) {
                    if (cur.children.size()>curChildIndex+1) {
                        if (curChild.incompatibilities.contains(cur.children.get(curChildIndex+1).type)) {
                            grid.set(y+1, x, new InvSlot(Volands.getSystem().getSkillItemStack(InvIcon.INCOMP_STRAIGHT_ACROSS), new Func_Ignore()));
                        } else {
                            grid.set(y+1, x, new InvSlot(Volands.getSystem().getSkillItemStack(InvIcon.STRAIGHT_ACROSS), new Func_Ignore()));
                        }
                    } else {
                        grid.set(y+1, x, new InvSlot(Volands.getSystem().getSkillItemStack(InvIcon.STRAIGHT_ACROSS), new Func_Ignore()));
                    }
                }
            }
        }
    }

    public InvGrid getGrid() {
        return grid;
    }

    public void createUI() {
        viewport.setContents(grid.createContents());
    }

    @EventHandler
    public void InventoryClicked(int raw, boolean leftClick) {
        InvSlot slot = grid.getSlotAtRaw(raw);
        if (slot!=null) {
            if (leftClick) {
                slot.runFunction(oc,oc.getPlayer(),this);
            } else {
                slot.runAltFunction(oc, oc.getPlayer(),this);
            }
            System.out.println("Click on slot " + slot.getStack().getItemMeta().getDisplayName());
            createUI();
        }
    }
}
