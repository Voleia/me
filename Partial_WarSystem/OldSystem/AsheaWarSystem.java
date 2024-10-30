package me.voleia.asheawarsystem;

import me.voleia.asheawarsystem.wars.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class AsheaWarSystem extends JavaPlugin {

    public static ArrayList<War> wars = new ArrayList<>();
    public static ArrayList<LivesTracker> livesWars = new ArrayList<>();

    private static AsheaWarSystem system;

    private static FileConfiguration mainConfig;

    @Override
    public void onEnable() {

        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new killEventListener(), this);
        this.getCommand("warPlayer").setExecutor(new warPlayer());
        this.getCommand("wp").setExecutor(new warPlayer());
        this.getCommand("warAdmin").setExecutor(new warAdmin());
        this.getCommand("wa").setExecutor(new warAdmin());
        if (wars.size() > 0) {
            wars.clear();
        }
        if (livesWars.size() > 0) {
            livesWars.clear();
        }
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();
        mainConfig = this.getConfig();
        system = this;
        //future games: shared lives pool (holdfast)
        //future games: progression gamemode (conquerers blade)
    }

    public static ArrayList<War> getWars() {
        return wars;
    }

    public static ArrayList<LivesTracker> getLivesWars() {
        return livesWars;
    }
    public void removeWar(int i) {
        wars.remove(i);
    }

    public static FileConfiguration returnConfig() {
        return mainConfig;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static AsheaWarSystem getSystem() {
        return system;
    }

    public static String getConfigString(String name) {
        return mainConfig.getString(name);
    }
}
