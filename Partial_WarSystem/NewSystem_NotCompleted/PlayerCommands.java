package me.voleia.progressionwar;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerCommands implements CommandExecutor, TabCompleter {

    ProgressionWar system = ProgressionWar.getSystem();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (s.equalsIgnoreCase("wp")) {
                if (args.length==1 && args[0].equalsIgnoreCase("list")) {
                    return listWars(player,args);
                } else if (args.length==2 && args[0].equalsIgnoreCase("list")) {
                    return listIndividual(player,args);
                } else if (args.length==2 && args[0].equalsIgnoreCase("leave")) {
                    return leave(player,args);
                } else if (args.length==3 && args[0].equalsIgnoreCase("join")) {
                    return join(player,args);
                } else {
                    player.sendMessage(ChatColor.GOLD + "[WAR] War System Commands");
                    player.sendMessage(ChatColor.GREEN + "[WAR] - /wp join <warname> <teamname> : lets you fight in a war");
                    player.sendMessage(ChatColor.GREEN + "[WAR] - /wp leave <warname> : lets you leave a war");
                    player.sendMessage(ChatColor.GREEN + "[WAR] - /wp list <warname> : lists details of a specific war");
                    player.sendMessage(ChatColor.GREEN + "[WAR] - /wp list : lists all wars");
                    return true;
                }
            }
        }
        return false;
    }

    boolean listWars(Player sender, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "[WAR] Ongoing Wars");
        for (WarFace war : system.getWars()) {
            if (war instanceof WarLives) {
                sender.sendMessage(ChatColor.GOLD + "Lives War - " + war.getWarName());
                for (Team t : war.getTeams()) {
                    sender.sendMessage(ChatColor.GREEN + " - Team " + t.getName() + " (" + t.getPlayers().size() + " players remaining)");
                }
            } else if (war instanceof WarProg) {
                sender.sendMessage(ChatColor.GOLD + "Progression War - " + war.getWarName());
                for (Team t : war.getTeams()) {
                    sender.sendMessage(ChatColor.GREEN + " - Team " + t.getName() + "("+t.getPlayers().size() + " players)");
                }
            }
        }
        return true;
    }

    boolean join(Player sender, String[] args) {
        WarFace war = system.getWarByName(args[1]);
        if (war!=null) {
            return war.playerJoin(sender, args[2]);
        }
        return false;
    }

    boolean leave(Player sender, String[] args) {
        WarFace war = system.getWarByName(args[1]);
        if (war!=null) {
            if (war instanceof WarLives) {
                return ((WarLives)war).playerQuit(sender);
            } else {
                return war.playerLeave(sender);
            }
        }
        return false;
    }

    boolean listIndividual(Player sender, String[] args) {
        WarFace war = system.getWarByName(args[1]);
        if (war!=null) {
            if (war instanceof WarLives) {
                sender.sendMessage(ChatColor.GOLD + "Lives War - " + war.getWarName());
                for (Team t : war.getTeams()) {
                    sender.sendMessage(ChatColor.YELLOW + "  Team: " + t.getName() + " (" + t.getPlayers().size() + " alive)");
                    for (Player p : t.getPlayers()) {
                        sender.sendMessage(ChatColor.GREEN + "   - " + p.getName());
                    }
                    for (Player p : t.getDeadPlayers()) {
                        sender.sendMessage(ChatColor.RED + "   - " + p.getName());
                    }
                }
            } else if (war instanceof WarProg) {
                sender.sendMessage(ChatColor.GOLD + "Progression War - " + war.getWarName());
                for (Team t : war.getTeams()) {
                    sender.sendMessage(ChatColor.YELLOW + "  Team: " + t.getName() + " (" + t.getPlayers().size() + " players)");
                    for (Player p : t.getPlayers()) {
                        sender.sendMessage(ChatColor.GREEN + "   - " + p.getName());
                    }
                    for (Player p : t.getDeadPlayers()) {
                        sender.sendMessage(ChatColor.RED + "   - " + p.getName());
                    }
                }
                WarProg prog = (WarProg) war;
                sender.sendMessage(ChatColor.YELLOW + "Capture Points:");
                for (CapturePoint point : prog.getCapturePoints()) {
                    if (point.controllingTeam==1) {
                        sender.sendMessage(ChatColor.BLUE + "- " + point.name + "(" + point.control + "%)");
                    } else {
                        sender.sendMessage(ChatColor.RED + "- " + point.name + "(" + point.control + "%)");
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (s.equalsIgnoreCase("wp")) {
            if (args.length==1) {
                return List.of("join", "leave", "list");
            } else if (args.length==2) {
                List<String> ret = new ArrayList<>();
                for (WarFace war : system.getWars()) {
                    ret.add(war.getWarName());
                }
                return ret;
            } else if (args.length==3) {
                WarFace war = system.getWarByName(args[1]);
                if (war!=null) {
                    List<String> ret = new ArrayList<>();
                    for (Team t : war.getTeams()) {
                        ret.add(t.getName());
                    }
                    return ret;
                } else {
                    return List.of("INVALID_WAR");
                }
            }
        }
        return new ArrayList<>();
    }
}
