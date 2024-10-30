package me.voleia.progressionwar;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {
    public final int teamIndex;
    private String name;
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Double> kills = new ArrayList<Double>();

    private ArrayList<Player> deadPlayers = new ArrayList<Player>();
    private ArrayList<Double> deadKills = new ArrayList<Double>();
    public Location spawnPoint = null;

    public Team(String name_, int ind) {
        name=name_;
        teamIndex=ind;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Player> getDeadPlayers() {
        return deadPlayers;
    }

    public Double getKills(Player player) {
        for (int i = 0; i < players.size(); i+=1) {
            if (players.get(i)==player) {
                return kills.get(i);
            }
        }
        for (int i = 0; i < deadPlayers.size(); i+=1) {
            if (deadPlayers.get(i)==player) {
                return deadKills.get(i);
            }
        }
        return -1.0;
    }

    public boolean forceRemovePlayer(Player player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(player)) {
                players.remove(i);
                kills.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean removePlayer(Player player) {
        for (int i = 0; i < players.size(); i+=1) {
            if (players.get(i).equals(player)) {
                deadPlayers.add(players.get(i));
                deadKills.add(kills.get(i));
                players.remove(i);
                kills.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean addPlayer(Player player) {
        for (Player p : players) {
            if (p==player) {
                return false;
            }
        }
        players.add(player);
        kills.add(0.0);
        return true;
    }

    public Double getKills(int index) {
        return kills.get(index);
    }

    public void addKill(Player player) {
        for (int i = 0; i < players.size(); i+=1) {
            if (player==players.get(i)) {
                kills.set(i,kills.get(i)+1);
            }
        }
    }

    public boolean playerIsOnTeam(Player player) {
        for (Player p : players) {
            if (p==player) {
                return true;
            }
        }
        return false;
    }
}
