package me.voleia.asheawarsystem.wars;

import me.voleia.asheawarsystem.AsheaWarSystem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class killEventListener implements Listener {

    private AsheaWarSystem system = AsheaWarSystem.getSystem();

    public void onEnable() {
        if (system!=AsheaWarSystem.getSystem()) {
            system = AsheaWarSystem.getSystem();
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e) { //disable for enchanted objects, disable for disallowed blocks
        System.out.println("[ASHEA WAR]: Event Stage 1 Triggered");
        if (e.getPlayer()!=null) {
            System.out.println("[ASHEA WAR]: Player not null");
            Player breaker = e.getPlayer();
            ArrayList<War> wars = system.getWars();
            boolean onTeam = false;
            System.out.println("[ASHEA WAR]: Getting wars");
            for (War war : wars) {
                System.out.println("[ASHEA WAR]: Found new war");
                for (Player player : war.team1Players) {
                    System.out.println("[ASHEA WAR]: Found new player (1)");
                    if (player.equals(breaker)) {
                        System.out.println("[ASHEA WAR]: Player is breaker");
                        onTeam=true;
                    }
                }
                if (!onTeam) {
                    for (Player player : war.team0Players) {
                        System.out.println("[ASHEA WAR]: Found new player (0)");
                        if (player.equals(breaker)) {
                            System.out.println("[ASHEA WAR]: Player is breaker");
                            onTeam=true;
                        }
                    }
                }
            }
            if (onTeam) {
                System.out.println("[ASHEA WAR]: Player is on team");
                breakChecker(e,e.getBlock(),breaker);
            }
        }
    }

    public void breakChecker(BlockBreakEvent event, Block block, Player player) {
        System.out.println("[ASHEA WAR]: Event Stage 2 Triggered");
        String mode = system.returnConfig().getString("mode"); //blocked, allowed, disabled
        System.out.println(mode);
        if (!mode.equalsIgnoreCase("disabled")) {
            System.out.println("[ASHEA WAR]: mode not disabled");
            List<String> mats = system.returnConfig().getStringList("allowedblocks");
            boolean onList = false;

            for (String matName : mats) {
                System.out.println("[ASHEA WAR]: For matName");
                try {
                    System.out.println("[ASHEA WAR]: Try start");
                    Material mat = Material.getMaterial(matName);
                    if (block.getType()==mat) {
                        onList=true;
                        System.out.println("[ASHEA WAR]: Block is of type");
                    }
                    System.out.println("[ASHEA WAR]: try succeed");
                } catch (Exception e) {
                    System.out.println("[ASHEA WAR]: Invalid block in conflict");
                }
            }

            if (mode.equalsIgnoreCase("allowed") && !onList) {
                event.setCancelled(true);
                System.out.println("[ASHEA WAR]: allowed");
            } else if (mode.equalsIgnoreCase("blocked") && onList) {
                event.setCancelled(true);
                System.out.println("[ASHEA WAR]: blocked");
            } else if (player.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.DIG_SPEED)) {
                event.setCancelled(true);
                System.out.println("[ASHEA WAR]: picks got enchants");
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        boolean alrDead=false;
        for (int i = 0; i < system.getLivesWars().size(); i+=1) {
            LivesTracker lvs = system.getLivesWars().get(i);
            for (int n = 0; n < lvs.team0Lives.size(); n+=1) {
                if (lvs.team0Lives.get(n)<=0 && lvs.team1Players.get(n).equals(player)) {
                    alrDead=true;
                    n=lvs.team0Lives.size();
                    i=system.getLivesWars().size();
                }
            }
            for (int n = 0; n < lvs.team1Lives.size(); n+=1) {
                if (lvs.team0Lives.get(n)<=0 && lvs.team1Players.get(n).equals(player)) {
                    alrDead=true;
                    n=lvs.team1Lives.size();
                    i=system.getLivesWars().size();
                }
            }
        }
        if (!alrDead) {
            player.setGameMode(GameMode.SURVIVAL);
        } else {
            player.setGameMode(GameMode.SPECTATOR);
        }
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user " + player.getDisplayName() + " meta setsuffix &f");
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getEntity().getKiller() != null) {
                Player killer = e.getEntity().getKiller(); //get killer
                Player dead = (Player) e.getEntity(); //get dead person
                ArrayList<War> wars = system.getWars(); //array of wars
                ArrayList<LivesTracker> livesWars=system.getLivesWars(); //array of lives war
                for (int i = 0; i < wars.size(); i+=1) {  //searching through all wars
                    boolean dt1 = false; //dead person is team 1
                    boolean dt0 = false; //dead person is team 0
                    boolean kt1 = false; //dead person is team 1
                    boolean kt0 = false; //dead person is team 0
                    int deadID=0;
                    int killerID=0;
                    ArrayList<Player> team0 = wars.get(i).team0Players; //players on team 0
                    ArrayList<Player> team1 = wars.get(i).team1Players; //players on team 1
                    for (int n = 0; n < team0.size(); n+=1) {
                        if (team0.get(n) == killer) { //if the killer is on team 0
                            kt0=true;
                            killerID=n;
                        } else if (team0.get(n) == dead) { //if the dead dude is on team 0
                            deadID=n;
                            dt0=true;
                        }
                    }
                    for (int n = 0; n < team1.size(); n+=1) {
                        if (team1.get(n) == killer) { //if killer is team 1
                            kt1=true;
                            killerID=n;
                        } else if (team1.get(n) == dead) { //if dead is team 1
                            deadID=n;
                            dt1=true;
                        }
                    }
                    if (kt1 && dt0) { //if killer from team 1 and dead guy from team 0
                        //team 1 killed team 0
                        wars.get(i).team1Points+=(1+(Double.valueOf(wars.get(i).team0Kills.get(deadID))/2));
                        wars.get(i).team1Kills.set(killerID, wars.get(i).team1Kills.get(killerID)+1);
                        //respawnTimer(dead,15,wars.get(i).team0Spawn);
                    } else if (kt0 && dt1) { //if killer from team 0 and dead guy from team 1
                        //team 0 killed team 1
                        wars.get(i).team0Points+=(1+(Double.valueOf(wars.get(i).team1Kills.get(deadID))/2));
                        wars.get(i).team0Kills.set(killerID, wars.get(i).team0Kills.get(killerID)+1);
                        //respawnTimer(dead,15,wars.get(i).team1Spawn);
                    }
                }
                for (int i = 0; i < livesWars.size(); i+=1) {

                    ArrayList<Player> team0 = livesWars.get(i).team0Players;
                    ArrayList<Player> team1 = livesWars.get(i).team1Players;
                    int deadID=0;
                    boolean fin=false;
                    int teamDead=0;
                    for (int n = 0; n < team0.size(); n+=1) {
                        if (team0.get(n) == dead) {
                            deadID=n;
                            fin=true;
                            teamDead=0;
                        }
                    }
                    for (int n = 0; n < team1.size(); n+=1) {
                        if (team1.get(n) == dead) {
                            deadID=n;
                            fin=true;
                            teamDead=1;
                        }
                    }
                    boolean respawn=false;

                    if (fin && teamDead==0) {
                        //team 1 killed team 0
                        //wars.get(i).team1Points+=( 1 + ( ((double)wars.get(i).team0Kills.get(deadID))/2 ) );
                        //wars.get(i).team1Kills.set(killerID, wars.get(i).team1Kills.get(killerID)+1);
                        LivesTracker lvs = livesWars.get(i);
                        lvs.team0Lives.set(deadID, lvs.team0Lives.get(deadID)-1);
                        if (lvs.team0Lives.get(deadID)>0) { //still alive
                            respawn=true;
                            Bukkit.broadcastMessage(dead.getDisplayName() + " has died! They are eliminated.");
                        } else { //dead
                            //dead.setGameMode(GameMode.SPECTATOR);
                            Bukkit.broadcastMessage(dead.getDisplayName() + " has died! They have " + lvs.team0Lives.get(deadID) + " lives remaining.");
                        }
                    } else if (fin && teamDead==1) {
                        LivesTracker lvs = livesWars.get(i);
                        lvs.team1Lives.set(deadID, lvs.team1Lives.get(deadID)-1);
                        if (lvs.team1Lives.get(deadID)>0) { //still alive
                            respawn=true;
                            Bukkit.broadcastMessage(dead.getDisplayName() + " has died! They are eliminated.");
                        } else { //dead
                            dead.setGameMode(GameMode.SPECTATOR);
                            Bukkit.broadcastMessage(dead.getDisplayName() + " has died! They have " + lvs.team1Lives.get(deadID) + " lives remaining");
                        }
                    }
                    Location t0s = livesWars.get(i).team0Spawn;
                    Location t1s = livesWars.get(i).team1Spawn;
                    if (respawn && teamDead==0) {
                        livesWars.get(i).killspawn(dead, 30, 0);
                        /*dead.sendMessage(ChatColor.GREEN + "YOU ARE DEAD! You will respawn in 15 seconds.");
                        dead.setGameMode(GameMode.SPECTATOR);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                dead.teleport(t0s);
                                dead.setGameMode(GameMode.SURVIVAL);
                                dead.sendMessage(ChatColor.GREEN + "You have respawned.");
                                this.cancel();
                            }
                        }.runTaskTimer(system, 300, 100);*/
                    } else if (respawn) {
                        livesWars.get(i).killspawn(dead, 30, 1);
                        /*dead.sendMessage(ChatColor.GREEN + "YOU ARE DEAD! You will respawn in 15 seconds.");
                        dead.setGameMode(GameMode.SPECTATOR);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                dead.teleport(t1s);
                                dead.setGameMode(GameMode.SURVIVAL);
                                dead.sendMessage(ChatColor.GREEN + "You have respawned.");
                                this.cancel();
                            }
                        }.runTaskTimer(system, 300, 100);*/
                    }
                    livesWars.get(i).checkVictor();
                }

                /*for (int i = 0; i < wars.size(); i+=1) {
                    War curWar = wars.get(i);
                    for (int n = 0; n < curWar.team0Players.size(); n+=1) {
                        if (dead.equals(curWar.team0Players.get(n))) {
                            curWar.killspawn(dead, 30, 0);
                            n=curWar.team0Players.size();
                            //System.out.println("imp2");
                        }
                    }
                    for (int n = 0; n < curWar.team1Players.size(); n+=1) {
                        if (dead.equals(curWar.team1Players.get(n))) {
                            curWar.killspawn(dead, 30, 1);
                            n=curWar.team1Players.size();
                            //System.out.println("imp1");
                        }
                    }
                }*/


                /*for (int i = 0; i < livesWars.size(); i+=1 ) {
                    LivesTracker curWar = livesWars.get(i);
                    for (int n = 0; n < curWar.team0Players.size(); n+=1) {
                        if (dead.equals(curWar.team0Players.get(n))) {
                            curWar.killspawn(dead, 30, 0);
                            n=curWar.team0Players.size();
                        }
                    }
                    for (int n = 0; n < curWar.team1Players.size(); n+=1) {
                        if (dead.equals(curWar.team1Players.get(n))) {
                            curWar.killspawn(dead, 30, 1);
                            n=curWar.team1Players.size();
                        }
                    }
                }*/
            }
            ArrayList<War> wars = system.getWars();
            Player dead = (Player) e.getEntity();
            for (int i = 0; i < wars.size(); i+=1) {
                War curWar = wars.get(i);
                for (int n = 0; n < curWar.team0Players.size(); n+=1) {
                    if (dead.equals(curWar.team0Players.get(n))) {
                        curWar.killspawn(dead, 30, 0, n);
                        n=curWar.team0Players.size();
                        //System.out.println("imp2");
                    }
                }
                for (int n = 0; n < curWar.team1Players.size(); n+=1) {
                    if (dead.equals(curWar.team1Players.get(n))) {
                        curWar.killspawn(dead, 30, 1, n);
                        n=curWar.team1Players.size();
                        //System.out.println("imp1");
                    }
                }
            }
        }
    }

    @EventHandler
    public void damageEvent(EntityDamageByEntityEvent e) { //prevenmts
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            ArrayList<War> wars = system.getWars();
            Player attacker = (Player) e.getDamager();
            Player reciever = (Player) e.getEntity();
            for (int i = 0; i < wars.size(); i += 1) {
                int a = -1;
                int r = -2;
                for (int n = 0; n < wars.get(i).team0Players.size(); n+=1) {
                    if (attacker == wars.get(i).team0Players.get(n)) {
                        a=0;
                    }
                    if (reciever == wars.get(i).team0Players.get(n)) {
                        r=0;
                    }
                }
                for (int n = 0; n < wars.get(i).team1Players.size(); n+=1) {
                    if (attacker == wars.get(i).team1Players.get(n)) {
                        a=1;
                    }
                    if (reciever == wars.get(i).team1Players.get(n)) {
                        r=1;
                    }
                }
                if (r==a) {
                    e.setCancelled(true);
                }
            }
        }
    }

    public void respawnTimer(Player player, int time, Location spawnpoint) {
        /*int t = time*20;
        player.sendMessage(ChatColor.GREEN + "YOU ARE DEAD! You will respawn in 15 seconds");
        System.out.println("timer triggered");
        player.setGameMode(GameMode.SPECTATOR);
        new BukkitRunnable() {
            @Override
            public void run() {
                System.out.println("respawn triggered");
                player.teleport(spawnpoint);
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(ChatColor.GREEN + "You have respawned.");
                this.cancel();
            }
        }.runTaskTimer(system, t,100);*/
    }
}
