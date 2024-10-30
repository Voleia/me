package me.voleia.progressionwar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.jpenilla.squaremap.api.Squaremap;

import java.util.ArrayList;
import java.util.Locale;

public final class ProgressionWar extends JavaPlugin {

    /* TO ADD
    * Respawn Timer
    * Beacons & Barriers
    * Block Protection
    * Scoreboard
    * Commands for Progression War
    * Nametags
    * Breakable block list
    * War ending for progression war
     */

    private static ProgressionWar system;
    private ArrayList<WarFace> wars = new ArrayList<>();

    @Override
    public void onEnable() {
        system = this;
        saveDefaultConfig();
        reloadConfig();
        getServer().getPluginManager().registerEvents(new Events(), this);
        PlayerCommands pc = new PlayerCommands();
        this.getCommand("wa").setExecutor(new AdminCommands());
        this.getCommand("wp").setExecutor(pc);
        this.getCommand("wp").setTabCompleter(pc);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null) {
            new PHExpansion(this).register();
        }

        new BukkitRunnable() { //run every second for every war
            @Override
            public void run() {
                for (WarFace war : wars) {
                    if (war instanceof WarProg) {
                        war.onTick();
                    }
                }
            }
        }.runTaskTimer(system,0,20);
    }

    public boolean isInWar(Player player) {
        for (WarFace war : wars) {
            for (Team team : war.getTeams()) {
                if (team.getPlayers().contains(player) || team.getDeadPlayers().contains(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ProgressionWar getSystem() {
        return system;
    }

    public void addWar(WarFace war) {
        wars.add(war);
    }

    public void removeWar(WarFace war) {
        wars.remove(war);
    }

    public ArrayList<WarFace> getWars() {
        return wars;
    }

    public WarFace getWarByName(String name) {
        for (WarFace war : wars) {
            if (war.getWarName().equals(name)) {
                return war;
            }
        }
        return null;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
