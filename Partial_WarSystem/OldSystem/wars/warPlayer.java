package me.voleia.asheawarsystem.wars;

import me.voleia.asheawarsystem.AsheaWarSystem;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class warPlayer implements CommandExecutor {

    AsheaWarSystem system = AsheaWarSystem.getSystem();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("warPlayer") || cmd.getName().equalsIgnoreCase("wp")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (sender.hasPermission("asheawars.player")) {
                    if (args.length==0 || args[0].equalsIgnoreCase("help") ) {
                        sender.sendMessage(ChatColor.GOLD + "Ashea War System Commands");
                        sender.sendMessage(ChatColor.GREEN + "- "+ ChatColor.GOLD +"/wp join <war> <team>"+ ChatColor.GREEN +" - Joins an ongoing siege war");
                        //sender.sendMessage(ChatColor.GREEN + "/warAdmin joinlives <war> <team> - Joins a lives war specifically");
                        sender.sendMessage(ChatColor.GREEN + "- "+ ChatColor.GOLD +"/wp leave <war> "+ ChatColor.GREEN +"- Leaves an ongoing war (Not reversible)");
                        sender.sendMessage(ChatColor.GREEN + "- "+ ChatColor.GOLD +"/wp list <war>"+ ChatColor.GREEN +" - Lists players engaged in a war.");
                        sender.sendMessage(ChatColor.GREEN + "- "+ ChatColor.GOLD +"/wp listwars"+ ChatColor.GREEN +" - Lists all ongoing wars");
                    } else if (args.length==2 && args[0].equalsIgnoreCase("list")) { //start player list command
                        for (int i = 0; i < system.getWars().size(); i+=1) {
                            if (system.getWars().get(i).warName.equalsIgnoreCase(args[1])) {
                                War war = system.getWars().get(i);
                                ArrayList<Player> t0p = war.team0Players;
                                ArrayList<Player> t1p = war.team1Players;
                                sender.sendMessage(ChatColor.GOLD + war.teamNames[0] + " players:");
                                for (int n = 0; n<t0p.size(); n+=1) {
                                    sender.sendMessage(ChatColor.GREEN + t0p.get(n).getName());
                                }
                                sender.sendMessage(ChatColor.GOLD + war.teamNames[1] + " players:");
                                for (int n = 0; n<t1p.size(); n+=1) {
                                    sender.sendMessage(ChatColor.GREEN + t1p.get(n).getName());
                                }
                                return true;
                            }
                        }
                        for (int i = 0; i < system.getLivesWars().size(); i+=1) {
                            if (system.getLivesWars().get(i).warName.equalsIgnoreCase(args[1])) {
                                LivesTracker war = system.getLivesWars().get(i);
                                ArrayList<Player> t0p = war.team0Players;
                                ArrayList<Player> t1p = war.team1Players;
                                sender.sendMessage(ChatColor.GOLD + war.team0Name + " players:");
                                for (int n = 0; n<t0p.size(); n+=1) {
                                    sender.sendMessage(ChatColor.GREEN + t0p.get(n).getName());
                                }
                                sender.sendMessage(ChatColor.GOLD + war.team1Name + " players:");
                                for (int n = 0; n<t1p.size(); n+=1) {
                                    sender.sendMessage(ChatColor.GREEN + t1p.get(n).getName());
                                }
                                return true;
                            }
                        }
                        sender.sendMessage(ChatColor.RED + "Error: Invalid war"); //end player list command
                        return false;
                    } else if ((args.length==3 && args[0].equalsIgnoreCase("joinsiege")) || (args[0].equalsIgnoreCase("join") && args.length==3)) {
                        for (int i = 0; i < system.getWars().size(); i+=1) {
                            if (system.getWars().get(i).warName.equalsIgnoreCase(args[1])) { //found the war
                                system.getWars().get(i).addPlayer((Player)sender, args[2]);
                                return true;
                            }
                        }
                        sender.sendMessage(ChatColor.RED + "Error: Invalid war");
                        return false;
                    } else if (args.length==2 && args[0].equalsIgnoreCase("leave")) {
                        for (int i = 0; i < system.getWars().size(); i+=1) {
                            if (system.getWars().get(i).warName.equalsIgnoreCase(args[1])) {
                                system.getWars().get(i).removePlayer((Player)sender);
                                return true;
                            }
                        }
                        sender.sendMessage(ChatColor.RED + "Error: Invalid war");
                        return false;
                    } else if (args.length==3 && args[0].equalsIgnoreCase("joinlives")) {
                        for (int i = 0; i < system.getLivesWars().size(); i+=1) {
                            if (system.getLivesWars().get(i).warName.equalsIgnoreCase(args[1])) {
                                system.getLivesWars().get(i).joinWar(args[2], player);
                                return true;
                            }
                        }
                        sender.sendMessage(ChatColor.RED + "Error: Invalid war");
                        return false;
                    } else if (args.length==1 && args[0].equalsIgnoreCase("listWars")) {
                        ArrayList<War> wars = system.wars;
                        sender.sendMessage(ChatColor.GOLD + "Ongoing siege wars:");
                        for (int i = 0; i < wars.size(); i += 1) {
                            sender.sendMessage(ChatColor.GOLD + "The Battle of " + wars.get(i).warName);
                            sender.sendMessage(ChatColor.GREEN + " - Team 0: [" + ChatColor.GOLD + wars.get(i).teamNames[0] + ChatColor.GREEN + "]");
                            sender.sendMessage(ChatColor.GREEN + " - Team 1: [" + ChatColor.GOLD + wars.get(i).teamNames[1] + ChatColor.GREEN + "]");
                            sender.sendMessage(ChatColor.GREEN + " - Time: [" + ChatColor.GOLD + Math.round(wars.get(i).checksElapsed / 4) + "/" + Math.round(wars.get(i).maxChecks / 4) + ChatColor.GREEN + "] minutes");
                            sender.sendMessage(ChatColor.GREEN + " - Battle Radius: [" + ChatColor.GOLD + wars.get(i).warRadius + ChatColor.GREEN + "] blocks");
                            sender.sendMessage(ChatColor.GREEN + " - Center Point: (" + (int)wars.get(i).centerPoint.getX() + ", " + (int)wars.get(i).centerPoint.getY() + ", " + (int)wars.get(i).centerPoint.getZ() + ")");
                        }
                        ArrayList<LivesTracker> livesWars = system.livesWars;
                        sender.sendMessage(ChatColor.GOLD + "Ongoing lives wars:");
                        for (int i = 0; i < livesWars.size(); i += 1) {
                            sender.sendMessage(ChatColor.GREEN + Integer.toString(i) + ". " + livesWars.get(i).warName + ": " + livesWars.get(i).team0Name + " vs. " + livesWars.get(i).team1Name);
                        }
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Error: Invalid command. try '/warPlayer help'");
                        return false;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Error: You do not have permission to join wars");
                    return false;
                }
            }
        }
        return false;
    }

    //join war
    //leave war
    //list players in war
    //help command
}
