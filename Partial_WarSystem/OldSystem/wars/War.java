package me.voleia.asheawarsystem.wars;

import net.minecraft.server.*;
import me.voleia.asheawarsystem.AsheaWarSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.minecraft.*;

import java.util.ArrayList;

import static org.bukkit.GameMode.SPECTATOR;
import static org.bukkit.GameMode.SURVIVAL;

public class War {

    public String warName = "";
    public boolean paused = true;
    public boolean started = false;

    public int checksElapsed = 0;
    //public int recentSecs=0;
    public int maxChecks = 160;
    int ticks = 0;

    public String[] teamNames = new String[2];
    public ArrayList<Player> team0Players = new ArrayList<>();
    public ArrayList<Integer> team0Kills = new ArrayList<>();
    public ArrayList<Player> team1Players = new ArrayList<>();
    public ArrayList<Integer> team1Kills = new ArrayList<>();
    public double team0Points = 0;
    public double team1Points = 0;

    public Location team0Spawn=null;
    public Location team1Spawn=null;

    public int maxEnemies = 0;
    public int minEnemies = 1;

    public boolean forceWarEnd = false;
    public int warRadius = 0;
    public Location centerPoint;
    private ArrayList<CapturePoint> caps = new ArrayList<>();
    private AsheaWarSystem system = AsheaWarSystem.getSystem();
    public War(String warName_, String team0Name, String team1Name, int maxChecks_, int warRadius_, Location centerPoint_) {
        warName = warName_;
        teamNames[0] = team0Name;
        teamNames[1] = team1Name;
        maxChecks=maxChecks_;
        warRadius=warRadius_;
        centerPoint=centerPoint_;
        //AsheaWarSystem.wars.add(this);
        Bukkit.broadcastMessage(ChatColor.GREEN + "A war is starting between " + ChatColor.BLUE + team0Name + ChatColor.GREEN + " and " + ChatColor.RED + team1Name + ChatColor.GREEN + ".");
        Bukkit.broadcastMessage(ChatColor.GREEN + "Use the command " + ChatColor.GOLD + "/wp join " + warName_ + " <team>" + ChatColor.GREEN + " to get involved!");
    }

    public boolean setSpawnPoint(String team, Location loc, Player sender) {
        if (team.equalsIgnoreCase("0")) {
            team0Spawn=loc;
            sender.sendMessage(ChatColor.GREEN + "Set spawnpoint for team 0");
            return true;
        } else if (team.equalsIgnoreCase("1")) {
            team1Spawn=loc;
            sender.sendMessage(ChatColor.GREEN + "Set spawnpoint for team 1");
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid team");
            return false;
        }
    }

    public boolean setPoints(String team, String pnts, Player sender) {
        try {
            Double val = (double) Integer.valueOf(pnts);
            if (team.equalsIgnoreCase("0")) {
                team0Points=val;
                sender.sendMessage(ChatColor.GREEN + "Points of team 0 changed");
                return true;
            } else if (team.equalsIgnoreCase("1")) {
                team1Points=val;
                sender.sendMessage(ChatColor.GREEN + "Points of team 1 changed");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Error: Invalid team");
                return false;
            }
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Error: Points value must be an integer");
            return false;
        }
    }

    public void createCapturePoint(Location loc, int radius, String name, double worth) {
        caps.add(new CapturePoint(loc, radius, name, worth,this));
    }

    public boolean deleteCapturePoint(String capName, Player sender) {
        for (int i = 0; i < caps.size(); i+=1) {
            if (caps.get(i).getName().equalsIgnoreCase(capName)) {
                caps.remove(i);
                sender.sendMessage(ChatColor.GREEN + "Removed capture point.");
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "Error: That capture point does not exist");
        return false;
    }

    public void startWar(Player starter) {
        if (caps.size()>0 && team0Spawn != null && team1Spawn !=null) {
            for (int i = 0; i < team0Players.size(); i+=1) {
                team0Players.get(i).teleport(team0Spawn);
            }
            for (int i = 0; i < team1Players.size(); i+=1) {
                team1Players.get(i).teleport(team1Spawn);
            }
            paused=false;
            started=true;
            Bukkit.broadcastMessage(ChatColor.GREEN + "The war between " + ChatColor.BLUE + teamNames[0] + ChatColor.GREEN + " and " + ChatColor.RED + teamNames[1] + ChatColor.GREEN + " is starting!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    boolean cancelled = false;
                    boolean pointUpd=false;
                    checksElapsed+=1;
                    pointUpd=true;
                    if (checksElapsed >= maxChecks || forceWarEnd) { //end the war if time limit is reached
                        cancelled = true;
                        endWar();
                        this.cancel();
                    }
                    if (checksElapsed==(maxChecks/3)) {
                        Bukkit.broadcastMessage(ChatColor.GREEN + "A third of the war has elapsed! Capture points are now worth double the points.");
                    } else if (checksElapsed==((2*maxChecks)/3)) {
                        Bukkit.broadcastMessage(ChatColor.GREEN + "Two thirds of the war have elapsed! Capture points are now worth quadrouple the points.");
                    }
                    if (!cancelled) {
                        for (int i = 0; i < caps.size(); i+=1) {
                            if (pointUpd) {
                                caps.get(i).updateControl();
                            }

                            //Bukkit.broadcastMessage(ChatColor.RED + "Current Points: " + teamNames[0] + " has " + team0Points + ", and " + teamNames[1] + " has " + team1Points + ".");
                            broadMsg(ChatColor.GOLD + "Current Points: " + teamNames[0] + " has " + team0Points + ", and " + teamNames[1] + " has " + team1Points + ".", 2);
                        }
                        for (int i = 0; i < team1Players.size(); i+=1) {
                            if (team1Players.get(i).isOnline()) {
                                Player pl = team1Players.get(i);
                                if (!inRadius(pl.getLocation(),centerPoint) && pl.getGameMode().equals(SURVIVAL)) {
                                    pl.sendMessage(ChatColor.RED + "You are leaving the war zone! Return immediately!");
                                    if (pl.getHealth() > 6) {
                                        pl.damage(6);
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < team0Players.size(); i+=1) {
                            if (team0Players.get(i).isOnline()) {
                                Player pl = team0Players.get(i);
                                if (!inRadius(pl.getLocation(),centerPoint) && pl.getGameMode().equals(SURVIVAL)) {
                                    pl.sendMessage(ChatColor.RED + "You are leaving the war zone! Return immediately!");
                                    if (pl.getHealth() > 6) {
                                        pl.setHealth(pl.getHealth()-6);
                                    }
                                }
                            }
                        }
                    }

                }
            }.runTaskTimer(system,0,300);
        } else {
            starter.sendMessage(ChatColor.RED + "Error: First assign spawns and capture points");
        }
    }

    private boolean inRadius(Location loc1, Location loc2) {
        double dist = Math.sqrt(Math.pow(loc1.getX()-loc2.getX(),2) + Math.pow(loc1.getZ()-loc2.getZ(),2));
        if (dist<warRadius) {
            return true;
        }
        return false;
    }

    public void endWar() {
        if (!forceWarEnd) { forceWarEnd=true; }
        Bukkit.broadcastMessage(ChatColor.RED + "The war between " + ChatColor.GOLD + teamNames[0] + ChatColor.RED + " and " + ChatColor.GOLD + teamNames[1] + ChatColor.RED + " is over!");
        if (team0Points > (team1Points+10)) {
            Bukkit.broadcastMessage(ChatColor.GOLD + teamNames[0] + ChatColor.RED + " has won decisively!");
        } else if (team1Points > (team0Points+10)) {
            Bukkit.broadcastMessage(ChatColor.GOLD + teamNames[1] + ChatColor.RED + " has won decisively!");
        } else {
            Bukkit.broadcastMessage(ChatColor.RED + "The battle was a draw!");
        }
        Bukkit.broadcastMessage(ChatColor.GOLD + teamNames[0] + " had " + team0Points + " points, and " + teamNames[1] + " had " + team1Points + " points.");
        for (int i = 0; i < system.getWars().size(); i+=1) {
            if (system.getWars().get(i) == this) {
                system.removeWar(i);
            }
        }
        for (Player player : team1Players) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user " + player.getDisplayName() + " meta setsuffix &f");
        }
        for (Player player : team0Players) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user " + player.getDisplayName() + " meta setsuffix &f");
        }
    }

    public void addPlayer(Player player, String team) {

        if (!started) {
            boolean alreadyJoined=false;
            for (int i = 0; i < team0Players.size(); i+=1) {
                if (team0Players.get(i) == player) {
                    alreadyJoined=true;
                }
            }
            for (int i = 0; i < team1Players.size(); i +=1) {
                if (team1Players.get(i) == player) {
                    alreadyJoined=true;
                }
            }
            if (!alreadyJoined) {
                //team = team.replaceAll("\\s", "");
                if (team.equalsIgnoreCase(teamNames[0])) {
                    team0Players.add(player);
                    team0Kills.add(0);
                    k0.add(0);
                    Bukkit.broadcastMessage(ChatColor.GREEN + player.getDisplayName() + " is fighting for " + team);
                    player.sendMessage(ChatColor.GREEN + "Successfully joined " + team + "!");
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user " + player.getDisplayName() + " meta setsuffix \"&f &1[&b&l" + team + "&1]\"");
                } else if (team.equalsIgnoreCase(teamNames[1])) {
                    team1Players.add(player);
                    team1Kills.add(0);
                    k1.add(0);
                    Bukkit.broadcastMessage(ChatColor.GREEN + player.getDisplayName() + " is fighting for " + team);
                    player.sendMessage(ChatColor.GREEN + "Successfully joined " + team + "!");
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user " + player.getDisplayName() + " meta setsuffix \"&f &4[&c&l" + team + "&4]\"");
                } else {
                    player.sendMessage(ChatColor.RED + "Error: That team does not exist.");
                }
            } else {
                Bukkit.broadcastMessage(ChatColor.RED + "Error: You are already in this war!");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Error: You cannot join the war after it has already started.");
        }
    }

    public void removePlayer(Player player) {
        boolean left=false;
        for (int i = 0; i < team0Players.size(); i+=1) {
            if (team0Players.get(i) == player) {
                team0Players.remove(i);
                team0Kills.remove(i);
                k0.remove(i);
                player.sendMessage(ChatColor.GREEN + "Successfully left the war!");
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user " + player.getDisplayName() + " meta setsuffix &f");
                left=true;
            }
        }
        if (left==false) {
            for (int i = 0; i < team1Players.size(); i+=1) {
                if (team1Players.get(i) == player) {
                    team1Players.remove(i);
                    team1Kills.remove(i);
                    k1.remove(i);
                    player.sendMessage(ChatColor.GREEN + "Successfully left the war!");
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user " + player.getDisplayName() + " meta setsuffix &f");
                    left=true;
                }
            }
        }
        if (left==false) {
            player.sendMessage(ChatColor.RED + "Error: You aren't in that war.");
        }
    }

    public void removePlayer(String pl, Player sender) {
        try {
            Player player = Bukkit.getPlayer(pl);
            boolean left=false;
            for (int i = 0; i < team0Players.size(); i+=1) {
                if (team0Players.get(i) == player) {
                    team0Players.remove(i);
                    team0Kills.remove(i);
                    k0.remove(i);
                    sender.sendMessage(ChatColor.GREEN + "Successfully kicked that player");
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user " + player.getDisplayName() + " meta setsuffix &f");
                    left=true;
                }
            }
            if (left==false) {
                for (int i = 0; i < team1Players.size(); i+=1) {
                    if (team1Players.get(i) == player) {
                        team1Players.remove(i);
                        team1Kills.remove(i);
                        k1.remove(i);
                        sender.sendMessage(ChatColor.GREEN + "Successfully kicked that player");
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user " + player.getDisplayName() + " meta setsuffix &f");
                        left=true;
                    }
                }
            }
            if (left==false) {
                player.sendMessage(ChatColor.RED + "Error: That player is not in the war.");
            }
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Error: That player is not online");
        }
    }

    public int getMultiplier() {
        if (checksElapsed>( (2*maxChecks)/3 )) {
            return 4;
        } else if (checksElapsed>(maxChecks/3)) {
            return 2;
        } else {
            return 1;
        }
    }

    public void broadMsg(String message, int recipient) {
        //0 = team 0
        //1 = team 1
        //2 = all teams
        if (recipient==0 || recipient==2) {
            for (int i = 0; i < team0Players.size(); i+=1) {
                team0Players.get(i).sendMessage(message);
            }
        }
        if (recipient==1 || recipient==2) {
            for (int i = 0; i < team1Players.size(); i+=1) {
                team1Players.get(i).sendMessage(message);
            }
        }
    }

    ArrayList<Integer> k0 = new ArrayList<Integer>();
    ArrayList<Integer> k1 = new ArrayList<Integer>();

    public void killspawn(Player player, int numSecs, int team, int index) {
        if (team==0) {
            //System.out.println("imp3");
            k0.set(index, numSecs+1);
            if (k0.get(index)<1) {
                k0.set(index,30);
            }
            player.sendMessage(ChatColor.GREEN + "You are dead! You will respawn soon.");
            player.setGameMode(SPECTATOR);
            player.teleport(team0Spawn);

            //System.out.println("[AWS] Debug >> timer triggered");
            new BukkitRunnable() {
                @Override
                public void run() {
                    //System.out.println("imp4");
                    k0.set(index,k0.get(index)-1);
                    player.sendMessage(ChatColor.GREEN + "Respawning in " + k0.get(index) + " seconds");
                    if (k0.get(index)<=0) {
                        //System.out.println("[AWS] Debug >> respawn triggered");
                        player.teleport(team0Spawn);
                        player.setGameMode(SURVIVAL);
                        player.sendMessage(ChatColor.GREEN + "You have respawned");
                        this.cancel();
                    }
                }
            }.runTaskTimer(system,0,20);
        } else {
            //System.out.println("imp3");
            k1.set(index, numSecs+1);
            if (k1.get(index)<1) {
                k1.set(index,30);
            }
            player.sendMessage(ChatColor.GREEN + "You are dead! You will respawn soon.");
            player.setGameMode(SPECTATOR);
            player.teleport(team1Spawn);
            //System.out.println("[AWS] Debug >> timer triggered");
            new BukkitRunnable() {
                @Override
                public void run() {
                    //System.out.println("imp4");
                    k1.set(index,k1.get(index)-1);
                    player.sendMessage(ChatColor.GREEN + "Respawning in " + k1.get(index) + " seconds");
                    if (k1.get(index)<=0) {
                        //System.out.println("[AWS] Debug >> respawn triggered");
                        player.teleport(team1Spawn);
                        player.setGameMode(SURVIVAL);
                        player.sendMessage(ChatColor.GREEN + "You have respawned");
                        this.cancel();
                    }
                }
            }.runTaskTimer(system,0,20);
        }
        /*//System.out.println("imp3");
        k.set(index, numSecs+1);
        if (k.get(index)<1) {
            k.set(index,30);
        }
        player.sendMessage(ChatColor.GREEN + "You are dead! You will respawn soon.");
        player.setGameMode(SPECTATOR);
        if (team==0) {
            player.teleport(team0Spawn);
        } else {
            player.teleport(team1Spawn);
        }
        //System.out.println("[AWS] Debug >> timer triggered");
        new BukkitRunnable() {
            @Override
            public void run() {
                //System.out.println("imp4");
                k.set(index,k.get(index)-1);
                player.sendMessage(ChatColor.GREEN + "Respawning in " + k + " seconds");
                if (k.get(index)<=0) {
                    //System.out.println("[AWS] Debug >> respawn triggered");
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
        }.runTaskTimer(system,0,20);*/
    }

    //int k = 0;
    /*public void killspawn(Player player, int numSecs, int team, int index) {
        //System.out.println("imp3");
        k = (numSecs+1);
        if (k<1) {
            k=30;
        }
        player.sendMessage(ChatColor.GREEN + "You are dead! You will respawn soon.");
        player.setGameMode(SPECTATOR);
        if (team==0) {
            player.teleport(team0Spawn);
        } else {
            player.teleport(team1Spawn);
        }
        //System.out.println("[AWS] Debug >> timer triggered");
        new BukkitRunnable() {
            @Override
            public void run() {
                //System.out.println("imp4");
                k-=1;
                player.sendMessage(ChatColor.GREEN + "Respawning in " + k + " seconds");
                if (k<=0) {
                    //System.out.println("[AWS] Debug >> respawn triggered");
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
    }*/

    /*public void setGlow(Player player, Player receiver) {
        World
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, player.getEntityId()); //Set packet's entity id
        WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
        watcher.setEntity(player); //Set the new data watcher's target
        watcher.setObject(0, (byte) (0x40)); //Set status to glowing, found on protocol page
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
        try {
            protocolManager.sendServerPacket(receiver, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }*///lp user Scar883 meta setsuffix " &4[&4&lJezolofin&4]"
}
