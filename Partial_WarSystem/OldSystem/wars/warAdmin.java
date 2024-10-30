package me.voleia.asheawarsystem.wars;

import me.voleia.asheawarsystem.AsheaWarSystem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class warAdmin implements CommandExecutor {

    AsheaWarSystem system = AsheaWarSystem.getSystem();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sender.hasPermission("asheawar.admin")) {
                if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(ChatColor.GOLD + "ASHEA WAR SYSTEM");
                    sender.sendMessage(ChatColor.GOLD + "Setup commands");
                    sender.sendMessage(ChatColor.GREEN + "- " + ChatColor.GOLD + "/wa siege <war> <nation0> <nation1> <radius> <checks (15 seconds each)>" + ChatColor.GREEN + " - starts a CAPTURE war between nations");
                    sender.sendMessage(ChatColor.GREEN + "- " + ChatColor.GOLD + "/wa start <war>"+ ChatColor.GREEN +" - Starts a war");
                    sender.sendMessage(ChatColor.GREEN + "- "+ChatColor.GOLD+"/wa control <war> <name> <worth> <radius> "+ ChatColor.GREEN +"- Creates a new capture point");
                    sender.sendMessage(ChatColor.GREEN + "- "+ChatColor.GOLD+"/wa spawn <war> <nationID> "+ ChatColor.GREEN +"- Sets the spawnpoint for a nation");
                    sender.sendMessage(ChatColor.GOLD + "Configuration Commands");
                    sender.sendMessage(ChatColor.GREEN + "- "+ChatColor.GOLD+"/wa maxEnemies <war> <num> "+ ChatColor.GREEN +"- sets the maximum number of enemies that can be on a point in order to capture it");
                    sender.sendMessage(ChatColor.GREEN + "- "+ChatColor.GOLD+"/wa minEnemies <war> <num> "+ ChatColor.GREEN +"- Sets the minimum number of players you need to capture a point");
                    sender.sendMessage(ChatColor.GREEN + "- "+ChatColor.GOLD+"/wa delcontrol <war> <control point name> "+ ChatColor.GREEN +"- Changes the name of a control point");
                    sender.sendMessage(ChatColor.GREEN + "- "+ChatColor.GOLD+"/wa testadd "+ ChatColor.GREEN +"- Adds a dirt block to allowed list as a test");
                    sender.sendMessage(ChatColor.GOLD + "Moderation Commands");
                    sender.sendMessage(ChatColor.GREEN + "- "+ChatColor.GOLD+"/wa setPoints <war> <nationID> <points> "+ ChatColor.GREEN +"- changes the points of an ongoing war");
                    sender.sendMessage(ChatColor.GREEN + "- "+ChatColor.GOLD+"/wa setElapsed <war> <checks (15 seconds each)> "+ ChatColor.GREEN +"- Sets the specific amount of checks that have passed in a war");
                    sender.sendMessage(ChatColor.GREEN + "- "+ChatColor.GOLD+"/wa list"+ ChatColor.GREEN +" - lists all current wars");
                    sender.sendMessage(ChatColor.GREEN + "- " + ChatColor.GOLD + "/wa end <war>"+ ChatColor.GREEN +" - Ends a war early");
                    sender.sendMessage(ChatColor.GREEN + "- "+ChatColor.GOLD+"/wa pause <war>"+ ChatColor.GREEN +" - Pauses capture point points in the war");
                    sender.sendMessage(ChatColor.GREEN + "- "+ChatColor.GOLD+"/wa setElapsed <war> <checks (15 seconds each)> "+ ChatColor.GREEN +"- Sets the specific amount of checks that have passed in a war");
                    sender.sendMessage(ChatColor.GREEN + "- "+ChatColor.GOLD+"/wa kick <war> <player> "+ ChatColor.GREEN +"- kicks a player from a war");

                    //sender.sendMessage(ChatColor.GREEN + "/wa lives <war> <nation0> <nation1> <livesNum> - starts a LIVES war between nations");
                    //sender.sendMessage(ChatColor.GREEN + "/wa livesspawn <war> <nationID> - Sets the spawnpoint of a war");
                    //sender.sendMessage(ChatColor.GREEN + "/wa endlives <war> - Ends the war early");
                    //sender.sendMessage(ChatColor.GREEN + "/wa setlives <war> <player> <lives> - Sets a players lives");
                    return true;
                } else if (args.length==3 && args[0].equalsIgnoreCase("kick")) {
                    ArrayList<War> wars = system.wars;
                    for (int i = 0; i < wars.size(); i += 1) {
                        if (wars.get(i).warName.equalsIgnoreCase(args[1])) {
                            wars.get(i).removePlayer(args[2], player);
                            return true;
                        }
                    }
                    return false;
                } else if (args.length==1 && args[0].equalsIgnoreCase("testadd")) {
                    List<String> mats = system.returnConfig().getStringList("allowedblocks");
                    String mat = Material.DEEPSLATE_BRICK_SLAB.name();
                    mats.add(mat);
                    system.returnConfig().set("allowedblocks",mats);
                    return true;
                } else if (args[0].equalsIgnoreCase("list")) {
                    ArrayList<War> wars = system.wars;
                    sender.sendMessage(ChatColor.GOLD + "Ongoing siege wars:");
                    for (int i = 0; i < wars.size(); i += 1) {
                        sender.sendMessage(ChatColor.GREEN + Integer.toString(i) + ". " + wars.get(i).warName + ": " + wars.get(i).teamNames[0] + " vs. " + wars.get(i).teamNames[1]);
                    }
                    ArrayList<LivesTracker> livesWars = system.livesWars;
                    sender.sendMessage(ChatColor.GOLD + "Ongoing lives wars:");
                    for (int i = 0; i < livesWars.size(); i += 1) {
                        sender.sendMessage(ChatColor.GREEN + Integer.toString(i) + ". " + livesWars.get(i).warName + ": " + livesWars.get(i).team0Name + " vs. " + livesWars.get(i).team1Name);
                    }
                    return true;
                } else if (args.length==2 && args[0].equalsIgnoreCase("start")) {
                    ArrayList<War> wars = system.wars;
                    for (int i = 0; i < wars.size(); i += 1) {
                        if (wars.get(i).warName.equalsIgnoreCase(args[1])) {
                            wars.get(i).startWar(player);
                            return true;
                        }
                    }
                    sender.sendMessage(ChatColor.RED + "Error: That war does not exist");
                    return false;
                } else if (args.length == 2 && args[0].equalsIgnoreCase("pause")) {
                    ArrayList<War> wars = system.wars;
                    for (int i = 0; i < wars.size(); i += 1) {
                        if (wars.get(i).warName.equalsIgnoreCase(args[1])) {
                            if (wars.get(i).paused) {
                                wars.get(i).paused = false;
                                sender.sendMessage(ChatColor.GREEN + "Unpaused the war!");
                            } else {
                                wars.get(i).paused = true;
                                sender.sendMessage(ChatColor.GREEN + "Paused the war! This does not effect kill points.");
                            }
                            return true;
                        }
                    }
                    sender.sendMessage(ChatColor.RED + "Error: That war does not exist");
                    return false;
                } else if (args.length==3 && args[0].equalsIgnoreCase("delcontrol")){
                    ArrayList<War> wars = system.getWars();
                    for (int i = 0; i < wars.size(); i+=1) {
                        if (wars.get(i).warName.equalsIgnoreCase(args[1])) {
                            wars.get(i).deleteCapturePoint(args[2], player);
                            return true;
                        }
                    }
                    return false;
                } else if (args.length == 6 && args[0].equalsIgnoreCase("siege")) {
                    try {
                        int radius = Integer.valueOf(args[4]);
                        int checks = Integer.valueOf(args[5]);
                        War newWar = new War(args[1], args[2], args[3], checks, radius, player.getLocation());
                        system.getWars().add(newWar);
                        sender.sendMessage(ChatColor.GREEN + "Created war with the following settings:");
                        sender.sendMessage(ChatColor.GREEN + "Name: [" + ChatColor.GOLD + args[1] + ChatColor.GREEN + "]");
                        sender.sendMessage(ChatColor.GREEN + "Team 0: [" + ChatColor.GOLD + args[2] + ChatColor.GREEN + "]");
                        sender.sendMessage(ChatColor.GREEN + "Team 1: [" + ChatColor.GOLD + args[3] + ChatColor.GREEN + "]");
                        sender.sendMessage(ChatColor.GREEN + "Time: [" + ChatColor.GOLD + Math.round(checks / 4) + ChatColor.GREEN + "] minutes");
                        sender.sendMessage(ChatColor.GREEN + "Battle Radius: [" + ChatColor.GOLD + radius + ChatColor.GREEN + "] blocks");
                        sender.sendMessage(ChatColor.GREEN + "Center Point: (" + (int) player.getLocation().getX() + ", " + (int) player.getLocation().getY() + ", " + (int) player.getLocation().getZ() + ")");
                        return true;
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "Error: Invalid numbers");
                        return false;
                    }
                } else if (args.length == 2 && args[0].equalsIgnoreCase("end")) {
                    ArrayList<War> wars = system.getWars();
                    for (int i = 0; i < wars.size(); i += 1) {
                        if (wars.get(i).warName.equalsIgnoreCase(args[1])) {
                            wars.get(i).endWar();
                            sender.sendMessage(ChatColor.GREEN + "Ending the war");
                            return true;
                        }
                    }
                    sender.sendMessage(ChatColor.RED + "Error: War does not exist");
                    return false;
                } else if (args.length == 3 && args[0].equalsIgnoreCase("spawn")) {
                    //spawn war nation
                    ArrayList<War> wars = system.getWars();
                    for (int i = 0; i < wars.size(); i += 1) {
                        if (wars.get(i).warName.equalsIgnoreCase(args[1])) {
                            if (args[2].equalsIgnoreCase("0")) {
                                wars.get(i).team0Spawn = player.getLocation();
                                sender.sendMessage(ChatColor.GREEN + "Team 0 spawn set");
                                return true;
                            } else if (args[2].equalsIgnoreCase("1")) {
                                wars.get(i).team1Spawn = player.getLocation();
                                sender.sendMessage(ChatColor.GREEN + "Team 1 spawn set");
                                return true;
                            }
                        }
                    }
                    sender.sendMessage(ChatColor.RED + "Error: That war does not exist.");
                    return false;
                } else if (args.length == 3 && args[0].equalsIgnoreCase("maxEnemies")) {
                    ArrayList<War> wars = system.getWars();
                    for (int i = 0; i < wars.size(); i += 1) {
                        if (wars.get(i).warName.equalsIgnoreCase(args[1])) {
                            try {
                                int val = Integer.valueOf(args[2]);
                                wars.get(i).maxEnemies=val;
                                sender.sendMessage(ChatColor.GREEN + "Set max enemies");
                                return true;
                            } catch (Exception e) {
                                sender.sendMessage(ChatColor.RED + "Invalid number");
                                return false;
                            }
                        }
                    }
                    sender.sendMessage(ChatColor.RED + "Error: That war does not exist.");
                    return false;
                } else if (args.length==3 && args[0].equalsIgnoreCase("minEnemies")) {
                    ArrayList<War> wars = system.getWars();
                    for (int i = 0; i < wars.size(); i+=1) {
                        if(wars.get(i).warName.equalsIgnoreCase(args[1])) {
                            try {
                                int val = Integer.valueOf(args[2]);
                                wars.get(i).minEnemies=val;
                                sender.sendMessage(ChatColor.GREEN + "Set min enemies");
                                return true;
                            } catch (Exception e) {
                                sender.sendMessage(ChatColor.RED + "Invalid number");
                                return false;
                            }
                        }
                    }
                    sender.sendMessage(ChatColor.RED + "Error: That war does not exist.");
                    return false;
                } else if (args.length==4 && args[0].equalsIgnoreCase("setPoints")) {
                    ArrayList<War> wars = system.getWars();
                    for (int i = 0; i < wars.size(); i+=1) {
                        if(wars.get(i).warName.equalsIgnoreCase(args[1])) {
                            wars.get(i).setPoints(args[2],args[3],player);
                            sender.sendMessage(ChatColor.GREEN + "Set points");
                            return true;
                        }
                    }
                    sender.sendMessage(ChatColor.RED + "Error: That war does not exist.");
                    return false;
                } else if (args.length==3 && args[0].equalsIgnoreCase("setElapsed")) {
                    ArrayList<War> wars = system.getWars();
                    for (int i = 0; i < wars.size(); i+=1) {
                        if(wars.get(i).warName.equalsIgnoreCase(args[1])) {
                            try {
                                int checks = Integer.valueOf(args[2]);
                                wars.get(i).checksElapsed=checks;
                                sender.sendMessage(ChatColor.GREEN + "Updated time elapsed");
                                return true;
                            } catch (Exception e) {
                                sender.sendMessage(ChatColor.RED + "Error: Invalid number. Must be a whole Integer");
                                return false;
                            }
                        }
                    }
                    sender.sendMessage(ChatColor.RED + "Error: That war does not exist.");
                    return false;
                } else if (args.length==5 && args[0].equalsIgnoreCase("control")) {
                    ArrayList<War> wars = system.getWars();
                    for (int i = 0; i < wars.size(); i+=1) {
                        if(wars.get(i).warName.equalsIgnoreCase(args[1])) {
                            try {
                                int rad = Integer.valueOf(args[4]);
                                double wor = Double.parseDouble(args[3]);
                                wars.get(i).createCapturePoint(player.getLocation(),rad,args[2],wor);
                                sender.sendMessage(ChatColor.GREEN + "Created your capture point");
                                return true;
                            } catch (Exception e) {
                                sender.sendMessage(ChatColor.RED + "Error: Some of the numbers were invalid");
                                return false;
                            }

                        }
                    }
                    sender.sendMessage(ChatColor.RED + "Error: That war does not exist.");
                    return false;
                } else if (args.length==5 && args[0].equalsIgnoreCase("lives")) {
                    try {
                        LivesTracker newWar = new LivesTracker(args[1],args[2],args[3],Integer.valueOf(args[4]));
                        system.getLivesWars().add(newWar);
                        sender.sendMessage(ChatColor.GREEN + "Created war " + args[1]);
                        return true;
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "Error: failed to create war");
                        return false;
                    }
                } else if (args.length==3 && args[0].equalsIgnoreCase("livesspawn")) {
                    ArrayList<LivesTracker> livesWars = system.getLivesWars();
                    for (int i = 0; i < livesWars.size(); i+=1) {
                        if(livesWars.get(i).warName.equalsIgnoreCase(args[1])) {
                            if (args[2].equalsIgnoreCase("0")) {
                                livesWars.get(i).setSpawn(1,player);
                                return true;
                            } else if (args[2].equalsIgnoreCase("1")) {
                                livesWars.get(i).setSpawn(1,player);
                                return true;
                            } else {
                                sender.sendMessage(ChatColor.RED + "Error: that team does not exist");
                                return false;
                            }
                        }
                    }
                    sender.sendMessage(ChatColor.RED + "Error: That war does not exist.");
                    return false;
                } else if (args.length==2 && args[0].equalsIgnoreCase("endlives")) {
                    ArrayList<LivesTracker> livesWars = system.getLivesWars();
                    for (int i = 0; i < livesWars.size(); i+=1) {
                        if(livesWars.get(i).warName.equalsIgnoreCase(args[1])) {
                            livesWars.get(i).endWar();
                            return true;
                        }
                    }
                    sender.sendMessage(ChatColor.RED + "Error: That war does not exist.");
                    return false;
                } else if (args.length==4 && args[0].equalsIgnoreCase("setlives")) {
                    ArrayList<LivesTracker> livesWars = system.getLivesWars();
                    for (int i = 0; i < livesWars.size(); i+=1) {
                        if(livesWars.get(i).warName.equalsIgnoreCase(args[1])) {
                            try {
                                int newLives = Integer.valueOf(args[3]);
                                livesWars.get(i).setLives(player, args[2], newLives);
                                return true;
                            } catch (Exception e) {
                                sender.sendMessage(ChatColor.RED + "Error: Invalid number");
                                return false;
                            }
                        }
                    }
                    sender.sendMessage(ChatColor.RED + "Error: That war does not exist.");
                    return false;
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Error: Invalid Command");
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Error: You need to be an admin to use this!");
                return false;
            }
        } else {
            sender.sendMessage("You must be a player to use war admin commands");
            return false;
        }
    }

    //create war
    //start war
    //pause war
    //end war
    //help command

    //left to do: Admin commands and particles
}
