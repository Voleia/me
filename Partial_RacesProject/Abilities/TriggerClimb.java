package me.voleia.volands.Abilities;

import me.voleia.volands.OC;
import me.voleia.volands.Races.RaceClass;
import org.bukkit.entity.Player;

public class TriggerClimb implements Ability, ToggleableAbility{
    public boolean trigger(OC o, Player player, int level) {
        RaceClass raceClass = o.getRaceClass();
        raceClass.setFlag("climb",!((boolean)raceClass.getFlag("climb")));
        return true;
    }

    public int getCooldown() {
        return 0;
    }

    public String getName() {
        return "Toggle Climb";
    }

    public int getLevel() {
        return 0;
    }

    public String failureMessage() {
        return "Failed to climb";
    }
}
