package me.voleia.volands.Abilities;

import me.voleia.volands.OC;
import me.voleia.volands.Volands;
import org.bukkit.entity.Player;

public class DragonMount implements Ability, ToggleableAbility {
    public boolean trigger(OC o, Player player, int level) {
        o.getRaceClass().setFlag("rideable","");
        return true;
    }

    public int getCooldown() {
        return 0;
    }

    String name = Volands.getSystem().translate("Dragon Mount");

    public String getName() {
        return name;
    }

    public int getLevel() {
        return 3;
    }

    public String failureMessage() {
        return "You are hurt! Try again in a few seconds";
    }
}
