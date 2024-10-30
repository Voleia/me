package me.voleia.progressionwar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class AdminCommands implements CommandExecutor {

    ProgressionWar system = ProgressionWar.getSystem();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (label.equalsIgnoreCase("wa") && sender.isOp()) {
            if (args.length == 2 && args[0].equalsIgnoreCase("end")) {
                if (endWar(player,args)) {
                    return true;
                }
            } else if (args.length == 4 && args[0].equalsIgnoreCase("force")) {
                if (forcePlayerJoin(player,args)) {
                    return true;
                }
                return forcePlayerJoin(player, args);
            } else if (args.length == 3 && args[0].equalsIgnoreCase("kick")) {
                if (forcePlayerLeave(player,args)) {
                    sender.sendMessage(ChatColor.GREEN+"Successfully kicked the player!");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED+"Failed to kick the player.");
                }
            } else if (args[0].equalsIgnoreCase("create") && args[1].equalsIgnoreCase("lives") && args.length >= 5) {
                if (createWar(player,args)) {
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("start") && args.length == 2) {
                if (startWar(player,args)) {
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("create") && args[1].equalsIgnoreCase("prog") && args.length == 6) {
                if (createProgWar(player,args)) {
                    return true;
                }
            } else if (args[0].equals("getadder")) {
                if (getListAdder(player)) {
                    return true;
                }
            } else if (args[0].equals("control")){
                if (createControlPoint(player,args)) {
                    return true;
                }
            }
        }
        return sendHelpMessage(player);
    }

    boolean sendHelpMessage(Player player) {
        player.sendMessage(ChatColor.GOLD + "[WAR] War Admin Commands");
        player.sendMessage(ChatColor.GREEN + "[LIVES] - /wa create lives <warname> teams[]");
        player.sendMessage(ChatColor.GREEN + "[PROG] - /wa create prog <warname> <team1name> <team2name> <end time>");
        player.sendMessage(ChatColor.GREEN + "[PROG] - /wa control <warname> <name> <radius>");
        player.sendMessage(ChatColor.GREEN + "[WAR] - /wa setspawn <warname> <teamname>");
        player.sendMessage(ChatColor.GREEN + "[WAR] - /wa start <warname>");
        player.sendMessage(ChatColor.GREEN + "[WAR] - /wa force player <warname> <teamname>");
        player.sendMessage(ChatColor.GREEN + "[WAR] - /wa kick player <warname>");
        player.sendMessage(ChatColor.GREEN + "[WAR] - /wa end <warname>");
        player.sendMessage(ChatColor.GREEN + "[ADMIN] - /wa getadder");
        return true;
    }

    boolean createControlPoint(Player sender, String[] args) {
        if (args.length==4) {
            try {
                WarProg war = (WarProg)system.getWarByName(args[1]);
                if (war!=null) {
                    try {
                        Location loc = sender.getLocation();
                        String name = args[2];
                        int radius = Integer.parseInt(args[3]);
                        for (CapturePoint point : war.getCapturePoints()) {
                            if (point.name.equals(name)) {
                                VolUtilities.CreateDelayedTask(new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        sender.sendMessage(ChatColor.RED+"That war already has a control point by that name.");
                                    }
                                }, system, 0.1);
                                return false;
                            }
                        }
                        war.addCapturePoint(loc,radius,name,1);
                    } catch (Exception e) {
                        VolUtilities.CreateDelayedTask(new BukkitRunnable() {
                            @Override
                            public void run() {
                                sender.sendMessage(ChatColor.RED+"One of your arguments was invalid.");
                            }
                        }, system, 0.1);
                        return false;
                    }
                }
                VolUtilities.CreateDelayedTask(new BukkitRunnable() {
                    @Override
                    public void run() {
                        sender.sendMessage(ChatColor.RED+"That war does not exist!");
                    }
                }, system, 0.1);
                return false;
            } catch (Exception ignored) {
                VolUtilities.CreateDelayedTask(new BukkitRunnable() {
                    @Override
                    public void run() {
                        sender.sendMessage(ChatColor.RED+"That war is not progression based!");
                    }
                }, system, 0.1);
            return false;
            }
        }
        return false;
    }

    boolean getListAdder(Player sender) {
        ItemStack adder = new ItemStack(Material.STICK);
        ItemMeta meta = adder.getItemMeta();
        meta.setDisplayName("BLOCKLIST ADDER");
        adder.setItemMeta(meta);
        sender.getInventory().addItem(adder);
        return true;
    }

    //___, ___, name, team 1, team 2, time
    boolean createProgWar(Player sender, String[] args) {
        if (!args[3].equals(args[4])) {
            try {
                int time = Integer.valueOf(args[5]);
                WarFace war = new WarProg(args[2], new String[] {args[3],args[4]}, time);
                sender.sendMessage(ChatColor.GREEN+"Creating War");
                return true;
            } catch (Exception e) {
                VolUtilities.CreateDelayedTask(new BukkitRunnable() {
                    @Override
                    public void run() {
                        sender.sendMessage(ChatColor.RED+"Failed, are you sure the last argument was an amount of time?");
                    }
                }, system, 0.1);
                return false;
            }
        }
        return false;
    }

    boolean endWar(Player sender, String[] args) {
        WarFace war = system.getWarByName(args[1]);
        if (war!=null) {
            war.endWar();
            sender.sendMessage("Ending war: " + args[1]);
            return true;
        }
        return false;
    }

    boolean forcePlayerJoin(Player sender, String[] args) {
        WarFace war = system.getWarByName(args[2]);
        if (war!=null) {
            Player player = Bukkit.getPlayer(args[1]);
            if (player!=null && player.isOnline()) {
                if (war.adminPlayerJoin(player, args[3])) {
                    sender.sendMessage("Forcing player " + player.getName() + " to join war " + args[2] + " on behalf of " + args[3]);
                    return true;
                }
            }
        }
        return false;
    }

    boolean forcePlayerLeave(Player sender, String[] args) {
        WarFace war = system.getWarByName(args[2]);
        if (war!=null) {
            Player player = Bukkit.getPlayer(args[1]);
            if (player!=null && player.isOnline()) {
                sender.sendMessage("Attempting to kick the player...");
                return war.adminPlayerLeave(player);
            }
        }
        sender.sendMessage("nah lol");
        return false;
    }

    boolean createWar(Player sender, String[] args) { //lives war
        String[] teams = new String[args.length-3];
        for (int i = 3; i<args.length; i+=1) {
            teams[i-3]=args[i];
        }
        WarFace war = new WarLives(args[2],teams);
        sender.sendMessage("Created your war");
        return true;
    }

    boolean startWar(Player sender, String[] args) {
        WarFace war = system.getWarByName(args[1]);
        if (war!=null) {
            war.startWar();
            sender.sendMessage("started war");
            return true;
        }
        sender.sendMessage("nah lol");
        return false;
    }
}
