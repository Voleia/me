package me.voleia.volands.Abilities;

import me.voleia.volands.OC;
import me.voleia.volands.Races.RaceClass;
import me.voleia.volands.Volands;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ThreeByThree implements Ability{

    Volands system = Volands.getSystem();

    public boolean trigger(OC o, Player player, int level) {
        RaceClass raceClass = o.getRaceClass();
        raceClass.setFlag("3by3", true);
        player.sendMessage("Your mining strength has increased for 12 seconds!");
        new BukkitRunnable() { //turn ability off after 8 seconds
            @Override
            public void run() {
                raceClass.setFlag("3by3",false);
                cancel();
            }
        }.runTaskTimer(system,240,20); //repeat every second;
        return true;
    }

    public int getCooldown() {
        return 180;
    }


    String name = Volands.getSystem().translate("3x3 Mining");

    public String getName() {
        return name;
    }

    public int getLevel() {
        return 3;
    }

    public String failureMessage() {
        return "";
    }
}
