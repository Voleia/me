package me.voleia.progressionwar;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class HandleRespawns {
    public void onProgWarPlayerKilled(Player player, WarProg war, Team team) {
        player.setGameMode(GameMode.SPECTATOR);
        new BukkitRunnable() {
            int respawnTime = (int) (20 + team.getKills(player));
            @Override
            public void run() {
                player.setGameMode(GameMode.SPECTATOR);
                respawnTime-=1;
                player.sendTitle(ChatColor.RED + "YOU ARE DEAD", "Respawn in " + respawnTime + " seconds");
                if (respawnTime <=0) {
                    player.setVelocity(new Vector(0,0,0));
                    player.setGameMode(GameMode.SURVIVAL);
                    if (team.spawnPoint!=null) {
                        player.teleport(team.spawnPoint);
                    } else if (player.getBedSpawnLocation() != null){
                        player.teleport(player.getBedSpawnLocation());
                    } else {
                        player.teleport(new Location(player.getWorld(), -304, 90, -711));
                    }
                    cancel();
                }
            }
        }.runTaskTimer(ProgressionWar.getSystem(), 20, 20);
    }
}
