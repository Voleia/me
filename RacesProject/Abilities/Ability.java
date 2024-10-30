package me.voleia.volands.Abilities;

import me.voleia.volands.OC;
import org.bukkit.entity.Player;

public interface Ability {
    public boolean trigger(OC o, Player player, int level);
    public int getCooldown();
    public String getName();
    public int getLevel();
    public String failureMessage();
}
