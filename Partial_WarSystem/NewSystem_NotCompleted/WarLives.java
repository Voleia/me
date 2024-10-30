package me.voleia.progressionwar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class WarLives implements WarFace {

    ArrayList<Team> teams;
    String warName;
    boolean started = false;
    ChatColor[] cols = new ChatColor[5];
    ProgressionWar system;

    public WarLives(String warName_, String[] teamNames) {
        teams = new ArrayList<>();
        for (int i = 0; i < teamNames.length; i++) {
            teams.add(new Team(teamNames[i], i));
        }
        warName=warName_;

        cols[0]=ChatColor.BLUE;
        cols[1]=ChatColor.RED;
        cols[2]=ChatColor.LIGHT_PURPLE;
        cols[3]=ChatColor.GOLD;
        cols[4]=ChatColor.AQUA;

        system = ProgressionWar.getSystem();
        system.addWar(this);

        String teamList = "";
        for (int i = 0; i < teamNames.length; i++) {
            teamList+=VolUtilities.getColorFromTeamNum(i)+teamNames[i]+", ";
        }
        teamList = teamList.substring(0,teamList.length()-2);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ChatColor.YELLOW + "A war is going to start soon!");
            p.sendMessage(ChatColor.YELLOW + "To join the war, do " + ChatColor.GOLD + "/wp join " + warName + " <TeamName>");
            p.sendMessage(ChatColor.YELLOW + "Teams: " + ChatColor.translateAlternateColorCodes('&', teamList));
        }
    }

    public boolean adminPlayerJoin(Player player, String teamName) {
        for (Team t : teams) {
            if (t.getName().equals(teamName)) {
                if (t.addPlayer(player)) {
                    player.sendMessage(ChatColor.GREEN + "[WAR] An admin has added you to the war [" + ChatColor.YELLOW + warName + ChatColor.GREEN + "] on behalf of team [" + cols[teams.indexOf(t)] + teamName + ChatColor.GREEN + "]");
                    messageAll(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " has been conscripted into the Battle of " + ChatColor.GOLD + warName + ChatColor.YELLOW + " for team " + ChatColor.GOLD + t.getName() + ChatColor.YELLOW + " by an admin.");
                    return true;
                }
            }
        }
        return false;
    }

    public boolean adminPlayerLeave(Player player) {
        boolean success=false;
        for (Team t : teams) {
            if(t.removePlayer(player)) {
                success=true;
                player.sendMessage(ChatColor.RED + "[WAR] An admin has removed you from the war [" + ChatColor.YELLOW + warName + ChatColor.RED + "]");
                messageAll(ChatColor.GOLD + player.getName() + ChatColor.RED + " has been kicked from the Battle of " + ChatColor.GOLD + warName + ChatColor.RED + " by an admin.");
            }
        }
        return success;
    }

    public boolean playerJoin(Player player, String teamName) {
        if (!started && getTeam(player)==null) {
            for (Team t : teams) {
                if (t.getName().equals(teamName)) {
                    if (t.addPlayer(player)) {
                        player.sendMessage(ChatColor.GREEN + "[WAR] You have joined the war [" + ChatColor.YELLOW + warName + ChatColor.GREEN + "] on behalf of team [" + cols[teams.indexOf(t)] + t.getName() + ChatColor.GREEN + "]");
                        messageAll(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " has joined the Battle of " + ChatColor.GOLD + warName + ChatColor.YELLOW + " for team " + ChatColor.GOLD + t.getName());
                        return true;
                    }
                }
            }
            return false;
        }
        player.sendMessage(ChatColor.YELLOW + "[WAR] That war has already begun! Message an admin if you need to join.");
        return false;
    }

    public boolean playerLeave(Player player) {
        boolean success=false;
        for (Team t : teams) {
            if(t.removePlayer(player)) {
                success=true;
                messageAll(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " has left the Battle of " + ChatColor.GOLD + warName);
            }
        }
        return success;
    }

    public boolean playerQuit(Player player) {
        boolean success=false;
        for (Team t : teams) {
            if(t.forceRemovePlayer(player)) {
                success=true;
                messageAll(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " has left the Battle of " + ChatColor.GOLD + warName);
            }
        }
        return success;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public boolean startWar() {
        started=true;
        String msg = ChatColor.GREEN + "[WAR] The Battle of " + warName + "has begun! The teams for this battle are ";
        for (int i = 0; i < teams.size(); i+=1) {
            msg+=cols[i]+teams.get(i).getName() + ChatColor.GREEN;
            if (i<teams.size()-1) {
                msg+=" vs. ";
            }
        }
        msg+=ChatColor.GREEN+". This battle is one-life only.";
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(msg);
            //p.sendMessage(ChatColor.YELLOW + "To join the war, do " + ChatColor.GOLD + "/wp join " + warName + " <TeamName>");
        }
        for (Team t : teams) {
            if (t.spawnPoint!=null) {
                for (Player p : t.getPlayers()) {
                    p.teleport(t.spawnPoint);
                }
            }
        }
        return true;
    }

    @Override
    public boolean setSpawnPoint(Location loc, String teamname) {
        for (Team t : teams) {
            if (t.getName().equalsIgnoreCase(teamname)) {
                t.spawnPoint = loc;
                return true;
            }
        }
        return false;
    }

    @Override
    public Team getTeam(Player player) {
        for (Team t : teams) {
            for (Player p : t.getPlayers()) {
                if (p==player) {
                    return t;
                }
            }
        }
        return null;
    }

    public boolean getStarted() {
        return started;
    }

    public void playerKilled(Player dead, Player killer) { //precondition: This is only called if the dead player is 100% on this team.
        //remove player from their team
        Team deadTeam = teams.get(0);
        Team killerTeam = teams.get(0);
        for (Team t : teams) {
            if(t.removePlayer(dead)) {
                deadTeam = t;
            }
            if (killer!=null) {
                if (t.playerIsOnTeam(killer)) {
                    t.addKill(killer);
                    killer.sendMessage(ChatColor.YELLOW + "[WAR] You killed enemy " + dead.getName());
                    killerTeam=t;
                }
            }
        }
        messageAll(ChatColor.YELLOW + "[WAR] Player " + ChatColor.GOLD + dead.getName() + ChatColor.YELLOW + " of team " + cols[teams.indexOf(deadTeam)] + deadTeam.getName() + ChatColor.YELLOW + " was killed. That team has " + deadTeam.getPlayers().size() + " players remaining.");
        //check if killer is null; check if killer is on a team. If so, add a kill to their kills.
        //check if any team has won. If so, call endWar(winner);

        //check if any team is fully eliminated.
        if (deadTeam.getPlayers().isEmpty()) {
            messageAll(ChatColor.YELLOW + "Team " + cols[teams.indexOf(deadTeam)] + deadTeam.getName() + ChatColor.YELLOW + " has been eliminated!");
        }
        //check if all teams but one are eliminated
        int numAboveZero=0;
        Team winner=teams.get(0);
        for (Team t : teams) {
            int numPlayers = 0;
            for (Player p : t.getPlayers()) {
                if (p.isOnline()) {
                    numPlayers+=1;
                }
            }
            if (numPlayers>0) {
                winner=t;
                numAboveZero+=1;
            }
        }

        if (numAboveZero<=1) {
            endWar(winner);
        }
    }

    void messageAll(String msg) {
        for (Team t : teams) {
            for (Player p : t.getPlayers()) {
                if (p.isOnline()) {
                    p.sendMessage(msg);
                }
            }
            for (Player p : t.getDeadPlayers()) {
                if (p.isOnline()) {
                    p.sendMessage(msg);
                }
            }
        }
    }

    public void endWar() { //this is for ending a war early
        Team winner = teams.get(0);
        for (Team t : teams) {
            if (t.getPlayers().size() > winner.getPlayers().size()) {
                winner = t;
            }
        }
        endWar(winner);
    }

    void endWar(Team winner) { //this is for ending a war automatically
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ChatColor.GREEN + "[WAR] The Battle of " + ChatColor.GOLD + warName + ChatColor.GREEN + " is over, and " + cols[teams.indexOf(winner)] + winner.getName() + ChatColor.GREEN + " has won!");
        }
        for (Team t : teams) {
            for (Player p : t.getPlayers()) {
                if (p.isOnline()) {
                    double numKills = t.getKills(p);
                    p.sendMessage("You had " + ((int)numKills) + " kills!");
                }
            }
            for (Player p : t.getDeadPlayers()) {
                if (p.isOnline()) {
                    double numKills = t.getKills(p);
                    p.sendMessage("You had " + ((int)numKills) + " kills!");
                }
            }
        }

        system.removeWar(this);
    }

    public String getWarName() {
        return warName;
    }

    public void onTick() {} //nothing here.

    public void addCapturePoint(Location center, int radius, String name, int startingTeam) {

    }
}
