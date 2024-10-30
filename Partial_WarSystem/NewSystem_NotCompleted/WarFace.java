package me.voleia.progressionwar;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public interface WarFace {
    public boolean adminPlayerJoin(Player player, String teamName);
    public boolean adminPlayerLeave(Player player);
    public boolean playerJoin(Player player, String teamName);
    public boolean playerLeave(Player player);
    public ArrayList<Team> getTeams();
    public boolean startWar();
    public Team getTeam(Player player);
    public boolean getStarted();
    public void playerKilled(Player player, Player killer);
    public String getWarName();
    public void endWar();
    public void onTick();
    public void addCapturePoint(Location center, int radius, String name, int startingTeam);
    public boolean setSpawnPoint(Location location, String team);
}
