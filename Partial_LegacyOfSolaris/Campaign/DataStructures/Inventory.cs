using System.Collections.Generic;

public class Inventory {

    readonly bool CHECK_BANNED;
    Dictionary<E_Resource, long> contents;
    List<E_Resource> allowed;

    public Inventory(List<E_Resource> containableTypes, bool checkForBannedItems) {
        contents = new Dictionary<E_Resource, long>();
        foreach (E_Resource cur in containableTypes) {
            contents[cur] = 0;
        }
        allowed = new List<E_Resource>(containableTypes);
        CHECK_BANNED = checkForBannedItems;
    }

    public long put(E_Resource resource, long quantity) {
        if (CHECK_BANNED && !allowed.Contains(resource)) {
            return 0;
        }
        long oldval = contents[resource];
        contents[resource] = quantity;
        return quantity - oldval;
    }

    public void add(E_Resource resource, long amountToAdd) {
        if (CHECK_BANNED && !allowed.Contains(resource)) {
            return;
        }
        contents[resource] += amountToAdd;
    }

    public bool remove(E_Resource resource, long amountToRemove) {
        if (amountToRemove == 0) {
            return true;
        }
        if (contents[resource] <= amountToRemove) {
            contents[resource] -= amountToRemove;
            return true;
        }
        return false;
    }

    public long removeUntilZero(E_Resource resource, long amountToRemove) {
        long quantity = contents[resource];
        if (amountToRemove <= quantity) {
            contents[resource] -= amountToRemove;
            return amountToRemove;
        }
        contents[resource] = 0;
        return amountToRemove - quantity;
    }

    public void removeBypass(E_Resource resource, long amountToRemove) {
        contents[resource] -= amountToRemove;
    }

    public long getResource(E_Resource resource) {
        if (CHECK_BANNED && !allowed.Contains(resource)) {
            return 0;
        }
        return contents[resource];
    }

    public Dictionary<E_Resource, long> getContents() {
        return contents;
    }
}
