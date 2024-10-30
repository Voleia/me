package me.voleia.asheawarsystem.wars;

import me.voleia.asheawarsystem.AsheaWarSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

import static org.bukkit.GameMode.SPECTATOR;
import static org.bukkit.GameMode.SURVIVAL;

public class LivesTracker  {

    AsheaWarSystem system = AsheaWarSystem.getSystem();

    public ArrayList<Player> team0Players = new ArrayList<>();
    public ArrayList<Player> team1Players = new ArrayList<>();
    public ArrayList<Integer> team0Lives = new ArrayList<>();
    public ArrayList<Integer> team1Lives = new ArrayList<>();
    public String team0Name="";
    public String warName="";
    public String team1Name="";
    public Location team0Spawn=null;
    public Location team1Spawn=null;

    public int lives=1;

    public LivesTracker(String name, String t0N, String t1N, int numLives) {
        team0Name=t0N;
        team1Name=t1N;
        warName=name;
        lives=numLives;
        //system.getLivesWars().add(this);
    }

    public void setSpawn(int team, Player player) {
        if (team==1) {
            team1Spawn=player.getLocation();
            player.sendMessage(ChatColor.GREEN + "Spawn set");
        } else if (team==0) {
            team0Spawn=player.getLocation();
            player.sendMessage(ChatColor.GREEN + "Spawn set");
        } else {
            player.sendMessage(ChatColor.RED + "Error: Invalid team");
        }
    }

    public void joinWar(String team, Player player) {
        if (team.equalsIgnoreCase(team1Name) || team.equalsIgnoreCase("1")) {
            team1Players.add(player);
            team1Lives.add(lives);
            player.sendMessage(ChatColor.GREEN + "You have joined team " + team);
        } else if (team.equalsIgnoreCase(team0Name) || team.equalsIgnoreCase("0")) {
            team0Players.add(player);
            team0Lives.add(lives);
            player.sendMessage(ChatColor.GREEN + "You have joined team " + team);
        } else {
            player.sendMessage(ChatColor.RED + "Invalid team");
        }
    }

    public void startWar(Player player) {
        if (team0Spawn!=null && team1Spawn!=null) {
            for (int i = 0; i < team0Players.size(); i+=1) {
                team1Players.get(i).teleport(team0Spawn);
            }
            for (int i = 0; i < team1Players.size(); i+=1) {
                team1Players.get(i).teleport(team1Spawn);
            }
        } else {
            player.sendMessage(ChatColor.RED + "Set spawns first");
        }
    }

    public void endWar() {
        int t0l=0;
        int t1l=0;
        for (int i = 0; i < team0Lives.size(); i+=1) {
            t0l+=team0Lives.get(i);
        }
        for (int i = 0; i < team1Lives.size(); i+=1) {
            t1l+=team1Lives.get(i);
        }
        if (t1l==t0l) { //tie
            Bukkit.broadcastMessage(ChatColor.GOLD + warName + ChatColor.GREEN + " has ended in a draw!");
        } else if (t0l > t1l) { //t0 wins
            Bukkit.broadcastMessage(ChatColor.GOLD + warName + ChatColor.GREEN + " has with a " + ChatColor.BLUE + team0Name + ChatColor.GREEN + " victory!");
        } else {
            Bukkit.broadcastMessage(ChatColor.GOLD + warName + ChatColor.GREEN + " has with a " + ChatColor.RED + team1Name + ChatColor.GREEN + " victory!");
        }
        for (int i = 0; i < system.getLivesWars().size(); i+=1) {
            if (system.getLivesWars().get(i).equals(this)) {
                system.getLivesWars().remove(i);
            }
        }
    }

    public void setLives(Player sender, String reciever, int newLives) {
        boolean success=false;
        for (int i = 0; i < team0Players.size(); i+=1) {
            if (team0Players.get(i).getDisplayName().equalsIgnoreCase(reciever)) {
                if (team0Lives.get(i)<1 && newLives>0) {
                    team0Lives.set(i,newLives);
                    team0Players.get(i).setGameMode(GameMode.SURVIVAL);
                    team0Players.get(i).teleport(team1Spawn);
                } else if (team0Lives.get(i)>0 && newLives<1) {
                    team0Lives.set(i,newLives);
                    team0Players.get(i).setGameMode(GameMode.SPECTATOR);
                    team0Players.get(i).sendMessage(ChatColor.RED + "The admins have used a command to kill you");
                } else {
                    team0Lives.set(i,newLives);
                    team0Players.get(i).sendMessage(ChatColor.GREEN + "The admins have changed your lives");
                }
            }
        }
        for (int i = 0; i < team1Players.size(); i+=1) {
            if (team1Players.get(i).getDisplayName().equalsIgnoreCase(reciever)) {
                if (team1Lives.get(i)<1 && newLives>0) {
                    team1Lives.set(i,newLives);
                    team1Players.get(i).setGameMode(GameMode.SURVIVAL);
                    team1Players.get(i).teleport(team1Spawn);
                } else if (team1Lives.get(i)>0 && newLives<1) {
                    team1Lives.set(i,newLives);
                    team1Players.get(i).setGameMode(GameMode.SPECTATOR);
                    team1Players.get(i).sendMessage(ChatColor.RED + "The admins have used a command to kill you");
                } else {
                    team1Lives.set(i,newLives);
                    team1Players.get(i).sendMessage(ChatColor.GREEN + "The admins have changed your lives");
                }
            }
        }
    }

    public void checkVictor() {
        boolean gameOver=false;
        int t0l=0;
        int t1l=0;
        for (int i = 0; i < team1Lives.size(); i+=1) {
            t1l+=team1Lives.get(i);
        }
        for (int i = 0; i < team0Lives.size(); i+=1) {
            t0l+=team0Lives.get(i);
        }
        if (t0l==0 || t1l==0) {
            endWar();
        } else {
            Bukkit.broadcastMessage(ChatColor.GREEN + team0Name + " has a combined " + t0l + " lives remaining");
            Bukkit.broadcastMessage(ChatColor.GREEN + team1Name + "has a combined " + t1l + " lives remaining");
        }
    }

    int k = 0;
    public void killspawn(Player player, int numSecs, int team) {
        k = (numSecs+1);
        if (k<1) {
            k=15;
        }
        player.sendMessage(ChatColor.GREEN + "You are dead! You will respawn soon.");
        player.setGameMode(SPECTATOR);
        if (team==0) {
            player.teleport(team0Spawn);
        } else {
            player.teleport(team1Spawn);
        }
        System.out.println("[AWS] Debug >> timer triggered");
        new BukkitRunnable() {
            @Override
            public void run() {
                k-=1;
                player.sendMessage(ChatColor.GREEN + "Respawning in " + k + " seconds");
                if (k<=0) {
                    System.out.println("[AWS] Debug >> respawn triggered");
                    if (team==0) {
                        player.teleport(team0Spawn);
                    } else {
                        player.teleport(team1Spawn);
                    }
                    player.setGameMode(SURVIVAL);
                    player.sendMessage(ChatColor.GREEN + "You have respawned");
                    this.cancel();
                }
            }
        }.runTaskTimer(system,0,20);
    }
}
