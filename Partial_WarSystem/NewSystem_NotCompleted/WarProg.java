package me.voleia.progressionwar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class WarProg implements WarFace{

    ArrayList<Team> teams;
    String warName;
    boolean started = false;
    ChatColor[] cols = new ChatColor[5];
    ProgressionWar system;
    ArrayList<CapturePoint> capturePoints = new ArrayList<>();
    int currentPoint;
    int totalTime;
    int elapsedTime=0;

    public WarProg(String warName_, String[] teamNames, int totalTime_) { //precondition: ONLY TWO TEAMS
        teams = new ArrayList<>();
        for (int i = 0; i < teamNames.length; i++) {
            teams.add(new Team(teamNames[i], i));
        }
        warName=warName_;

        totalTime=totalTime_;

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

    public ArrayList<CapturePoint> getCapturePoints() {
        return capturePoints;
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
        if (capturePoints.size()%2==0) {
            started = true;

            currentPoint=capturePoints.size()/2;
            for (int i = 0; i < capturePoints.size(); i+=1) {
                if (i<currentPoint) {
                    capturePoints.get(i).setStartingTeam(0);
                } else {
                    capturePoints.get(i).setStartingTeam(1);
                }
            }

            String msg = ChatColor.GREEN + "[WAR] The Battle of " + warName + "has begun! The teams for this battle are ";
            for (int i = 0; i < teams.size(); i+=1) {
                msg+=cols[i]+teams.get(i).getName() + ChatColor.GREEN;
                if (i<teams.size()-1) {
                    msg+=" vs. ";
                }
            }
            msg+=ChatColor.GREEN+". This battle is progression-based. Capture the enemy control points to win!";
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(msg);
            }

            return true;
        }
        for (Team t : teams) {
            if (t.spawnPoint!=null) {
                for (Player p : t.getPlayers()) {
                    p.teleport(t.spawnPoint);
                }
            }
        }
        return false;
    }

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

    public boolean getStarted() {
        return started;
    }

    public void playerKilled(Player player, Player killer) {
        for (Team t : teams) {
            for (Player p : t.getPlayers()) {
                if (p.isOnline()) {
                    if (getTeam(player) == t) {
                        p.sendMessage(ChatColor.RED + "[WAR] " + player.getName() + " was killed by " + killer.getName());
                    } else {
                        p.sendMessage(ChatColor.GREEN + "[WAR] " + player.getName() + " was killed by " + killer.getName());
                    }
                }
            }
        }
    }

    public void messageAll(String msg) {
        for (Team t : teams) {
            for (Player p : t.getPlayers()) {
                if (p.isOnline()) {
                    p.sendMessage(msg);
                }
            }
        }
    }

    public String getWarName() {
        return warName;
    }

    public void endWar() {
        messageAll(ChatColor.RED + "The war has timed out before all points could be captured!");
        int num = 0;
        for (CapturePoint point : capturePoints) {
            if (point.controllingTeam==0) {
                num-=1;
            } else {
                num+=1;
            }
        }
        if (num<0) {
            endWar(teams.get(0));
        } else if (num>0) {
            endWar(teams.get(1));
        } else {
            endWar(null);
        }
    }

    void endWar(Team winner) {
        for (CapturePoint point : capturePoints) {
            point.reverseBlocks();
        }
        if (winner==null) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "THE BATTLE OF " + warName + " WAS A DRAW! NOBODY WON");
        } else {
            Bukkit.broadcastMessage(ChatColor.GOLD + winner.getName() + " HAS WON THE BATTLE OF " + warName);
        }
        system.removeWar(this);
    }

    public String placehold = "";

    public void onTick() { //call this from the main class
        if (!started) {
            return;
        }
        capturePoints.get(currentPoint).onTick();
        capturePoints.get(currentPoint-1).onTick();

        elapsedTime+=1;
        if (elapsedTime>=totalTime) {
            endWar();
        }

        placehold = "\n\n" + ChatColor.GOLD+warName+"\n" + (elapsedTime/60)+"m/"+(totalTime/60)+"m\n";
        for (CapturePoint point : capturePoints) {
            if (point.controllingTeam==0) {
                placehold+=ChatColor.GREEN + " ";
            } else {
                placehold+=ChatColor.RED + " ";
            }
        }
        CapturePoint left = capturePoints.get(currentPoint);
        placehold+="\n" + VolUtilities.getColorFromTeamNum(left.controllingTeam)+left.name + ": " + left.control + "%";
        CapturePoint right = capturePoints.get(currentPoint-1);
        placehold+="\n" + VolUtilities.getColorFromTeamNum(left.controllingTeam)+right.name + ": " + right.control + "%";
    }

    public void addCapturePoint(Location center, int radius, String name, int startingTeam) {
        capturePoints.add(new CapturePoint(this,center,radius,name));
        Location bloc = center;
        bloc.setY(bloc.getBlockY()-2);
        for (int x = -1; x <=1; x++) {
            for (int z = -1; z <=1; z++) {
                Location temp = bloc;
                temp.setX(temp.getBlockX()+x);
                temp.setZ(temp.getBlockZ()+z);
                temp.getBlock().setType(Material.IRON_BLOCK);
            }
        }
        bloc.setY((bloc.getBlockY()+1));
        bloc.getBlock().setType(Material.BEACON);
        bloc.setY(bloc.getBlockY()-2);
        bloc.getBlock().setType(Material.BARRIER);
    }

    public void controlSwitch(int newTeam) {
        capturePoints.get(currentPoint).reset();
        capturePoints.get(currentPoint-1).reset();
        if (newTeam==1) { //shift left;
            currentPoint-=1;
            capturePoints.get(currentPoint).setStartingTeam(1);
        } else { //shift right;
            capturePoints.get(currentPoint).setStartingTeam(0);
            currentPoint+=1;
        }
        messageAll(ChatColor.YELLOW + "[WAR] Team " + cols[newTeam] + teams.get(newTeam).getName() + ChatColor.YELLOW + " has captured a point, shifting the current points " + leftright(newTeam) + "!" );
        if (currentPoint>=capturePoints.size()) {
            endWar(teams.get(1));
        } else if (currentPoint<=0) {
            endWar(teams.get(0));
        }
    }

    String leftright(int a) {
        if (a==1) {
            return "left";
        }
        return "right";
    }
}
