package me.voleia.volands.CustomGUISystem;

import me.voleia.volands.CustomGUISystem.Funcs.CardinalDirection;
import me.voleia.volands.CustomGUISystem.Funcs.Func_Scroll;
import me.voleia.volands.OC;
import me.voleia.volands.SkillTree.InvIcon;
import me.voleia.volands.Volands;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InvGrid {
    //Translates inventory coordinates into grid coordinates and back. Stores InvSlots in a 2d array

    int rightOffset = 0;
    int downOffset = 0;
    int guiHeight;

    public InvGrid(int height) {
        guiHeight = height;
    }

    public int[] getCoordinatesAtRaw(int raw) {
        return new int[]{raw/9 + downOffset, raw-((raw/9)*9) + rightOffset};
    }

    public int getRawAtCoordinates(int y, int x) {
        y-=downOffset;
        x-=rightOffset;
        return x+(y/9);
    }

    public ItemStack[] createContents() {
        ItemStack[] toAdd = new ItemStack[9*guiHeight];
        int index = 0;
        for (int y = 0; y < guiHeight; y++) {
            for (int x = 0; x < 9; x++) {
                InvSlot slot = get(y+downOffset,x+rightOffset);
                if (slot==null) {
                    toAdd[index] = Volands.getSystem().getSkillItemStack(InvIcon.BACKGROUND);
                } else {
                    toAdd[index] = slot.getStack();
                }
                index++;
            }
        }
        if (rightOffset!=0) {
            toAdd[((guiHeight-1)/2) * 9]=Volands.getSystem().getSkillItemStack(InvIcon.ARROW_LEFT); //left arrow
        }
        if (downOffset!=0) {
            toAdd[4] = Volands.getSystem().getSkillItemStack(InvIcon.ARROW_UP); //top arrow
        }
        toAdd[4 + (9*(guiHeight-1))] = Volands.getSystem().getSkillItemStack(InvIcon.ARROW_DOWN); //bottom arrow
        toAdd[8+(((guiHeight-1)/2) * 9)]=Volands.getSystem().getSkillItemStack(InvIcon.ARROW_RIGHT); //right arrow
        return toAdd;
    }

    public InvSlot convertSlotToArrow(InvSlot slot, int raw) {
        if (rightOffset!=0 && raw==((guiHeight-1)/2) * 9) {
            return new InvSlot("Scroll", new ArrayList<>(), Material.ARROW, new Func_Scroll(CardinalDirection.LEFT));
        }
        if (downOffset!=0 && raw==4) {
            return new InvSlot("Scroll", new ArrayList<>(), Material.ARROW, new Func_Scroll(CardinalDirection.UP));
        }
        if (raw==(4 + (9*(guiHeight-1)))) {
            return new InvSlot("Scroll", new ArrayList<>(), Material.ARROW, new Func_Scroll(CardinalDirection.DOWN));
        }
        if (raw==(8+(((guiHeight-1)/2) * 9))) {
            return new InvSlot("Scroll", new ArrayList<>(), Material.ARROW, new Func_Scroll(CardinalDirection.RIGHT));
        }
        return slot;
    }

    public boolean moveDown(int down) {
        downOffset+=down;
        return true;
    }

    public boolean moveRight(int right) {
        rightOffset+=right;
        return true;
    }

    public boolean moveLeft(int left) {
        if (rightOffset-left >= 0) {
            rightOffset-=left;
            return true;
        }
        return false;
    }

    public boolean moveUp(int up) {
        if (downOffset-up >= 0) {
            downOffset-=up;
            return true;
        }
        return false;
    }

    ArrayList<ArrayList<InvSlot>> myArray = new ArrayList<>();

    public InvSlot get(int[] coords) {
        return get(coords[0],coords[1]);
    }

    public InvSlot get(int y, int x) {
        if (y >= myArray.size() || y < 0) {
            return null;
        }
        if (x >= myArray.get(y).size() || x < 0) {
            return null;
        }
        return myArray.get(y).get(x);
    }

    public void set(int y, int x, InvSlot slot) {
        while (y>=myArray.size()) {
            myArray.add(new ArrayList<InvSlot>());
        }
        while (x>=myArray.get(y).size()) {
            myArray.get(y).add(null);
        }
        myArray.get(y).set(x, slot);
    }

    public InvSlot getSlotAtRaw(int raw) {
        return convertSlotToArrow(get(getCoordinatesAtRaw(raw)), raw);
    }
}
