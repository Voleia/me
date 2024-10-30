package me.voleia.progressionwar;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class VolUtilities {
    static HashMap<Integer, BukkitRunnable> ongoingRunnables = new HashMap<>();
    public static void CreateRunnable(BukkitRunnable runnable, Plugin plugin, double startDelay, double rate, int id) {
        runnable.runTaskTimer(plugin, (long)startDelay*20, (long)rate*20);
        ongoingRunnables.put(id, runnable);
    }

    public static void CreateDelayedTask(BukkitRunnable task, Plugin plugin, double delay) {
        task.runTaskLater(plugin, (long) delay*20);
    }

    public static void CreateDelayedTask(BukkitRunnable interimBehavior, BukkitRunnable endBehavior, Plugin plugin, double interimDelay, double delay) {
        long tickSpeed = (long)interimDelay*20;

        new BukkitRunnable() {
            int ticksToDelay = (int)(delay/interimDelay);
            @Override
            public void run() {
                ticksToDelay-=1;
                if (ticksToDelay<0) {
                    endBehavior.run();
                    cancel();
                } else {
                    interimBehavior.run();
                }
            }
        }.runTaskTimer(plugin, 0, tickSpeed);

    }

    public static boolean endRunnable(int id) {
        BukkitRunnable runnable = ongoingRunnables.get(id);
        if (runnable!=null) {
            runnable.cancel();
            ongoingRunnables.remove(id);
        }
        return false;
    }

    public static String getColorFromTeamNum(int num) {
        switch (num) {
            case 0:
                return "&a&l";
            case 1:
                return "&c&l";
            case 2:
                return "&9&l";
            case 3:
                return "&e&l";
            case 4:
                return "&d&l";
            default:
                return "&1&l";
        }
    }
}
