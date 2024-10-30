package me.voleia.volands.Commands;

import me.voleia.volands.CustomGUISystem.CustomGUI;
import me.voleia.volands.Enums.ExpType;
import me.voleia.volands.GUIs.OCSelectionGUI;
import me.voleia.volands.GUIs.TitleGUI;
import me.voleia.volands.OC;
import me.voleia.volands.Races.RaceClass;
import me.voleia.volands.SkillTree.Skill;
import me.voleia.volands.SkillTree.SkillTree;
import me.voleia.volands.SkillTree.SkillType;
import me.voleia.volands.SkillTree.TreeHuman;
import me.voleia.volands.Volands;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RaceCommand implements CommandExecutor {

    Volands system = Volands.getSystem();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (label.equalsIgnoreCase("oc") || label.equalsIgnoreCase("race")) {
                if (args.length==0 || (args.length==1 && args[0].equalsIgnoreCase("help"))) {
                    return helpMenu(player,label);
                } else if (args.length==1 && args[0].equalsIgnoreCase("menu")) {
                    return menu(player);
                } else if (args.length==1 && args[0].equalsIgnoreCase("info")) {
                    return info(player);
                } else if (args.length==1 && args[0].equalsIgnoreCase("stats")) {
                    return stats(player);
                } else if (args.length==3 && args[0].equalsIgnoreCase("delete")) {
                    return delete(player,args);
                } else if (args.length==2 && args[0].equalsIgnoreCase("rename")) {
                    return renameOC(player,args);
                } else if (args.length==1 && args[0].equalsIgnoreCase("title")) {
                    return titleSelection(player);
                } else if (args.length==1 && args[0].equalsIgnoreCase("togglemsg")) {
                    OC oc = system.getOC(player);
                    if (oc!=null) {
                        oc.toggleSpam();
                        if (oc.getSpamToggled()) {
                            sender.sendMessage(ChatColor.GREEN + "Chat messages have been disabled!");
                        } else {
                            sender.sendMessage(ChatColor.GREEN + "Chat messages have been enabled!");
                        }
                        system.getHandler().saveOC(oc);
                    }
                    return true;
                } else if (args.length==1 && args[0].equalsIgnoreCase("tree") || args[0].equalsIgnoreCase("skills") || args[0].equalsIgnoreCase("skilltree")) {
                    return constructTreeUI(player);
                }
                return helpMenu(player,label);
            } else {
                sender.sendMessage(ChatColor.RED + "There was an issue with that command!");
            }
            return false;
        } else {
            sender.sendMessage("Only players can use that command!");
            return false;
        }
    }

    boolean constructTreeUI(Player sender) {
        OC oc = system.getOC(sender);
        if (oc!=null) {
            CustomGUI ign = new CustomGUI(6, ChatColor.translateAlternateColorCodes('&',"&f&l"+oc.getRaceClass().getDisplayName() + " " + Volands.ptSkillTree), oc, oc.getTree());
            return true;
        }
        return false;
    }

    boolean renameOC(Player player, String args[]) {
        OC oc = system.getOC(player);
        if (oc!=null) {
            if (args[1].length()>=3 && args[1].length()<=16) {
                if (system.getChat().alphanumeric(args[1])) {
                    oc.rename(args[1]);
                    system.getHandler().saveOC(oc);
                    player.sendMessage(ChatColor.GREEN + "Created your OC!");
                    //valid message
                } else {
                    player.sendMessage(ChatColor.RED + "Your character name can only consist of letters!");
                    return false;
                }
            } else {
                player.sendMessage(ChatColor.RED + "Your name must be between 3 and 16 letters!");
                return false;
            }
        }
        return false;
    }

    boolean helpMenu(Player player, String label) {
        player.sendMessage(ChatColor.WHITE + " ");
        player.sendMessage(ChatColor.YELLOW+system.translate("==[ Races Command List ]=="));
        player.sendMessage(ChatColor.GREEN + "/"+label+" menu"+ChatColor.DARK_GREEN+" - Opens the OC selection menu, to switch races or create new OCs");
        player.sendMessage(ChatColor.GREEN + "/"+label+" delete <OCName> <OCName>"+ChatColor.DARK_GREEN+" - deletes an OC (irreversible)");
        player.sendMessage(ChatColor.GREEN + "/"+label+" rename <newName>"+ChatColor.DARK_GREEN+" - renames your current OC");
        player.sendMessage(ChatColor.GREEN + "/"+label+" info"+ChatColor.DARK_GREEN+" - displays info about your race");
        player.sendMessage(ChatColor.GREEN + "/"+label+" help"+ChatColor.DARK_GREEN+" - displays this help menu");
        player.sendMessage(ChatColor.GREEN + "/"+label+" stats"+ChatColor.DARK_GREEN+" - displays stats about your character");
        player.sendMessage(ChatColor.GREEN + "/"+label+" title"+ChatColor.DARK_GREEN+" - Customizes your title in chat");
        player.sendMessage(ChatColor.GREEN + "/"+label+" togglemsg"+ChatColor.DARK_GREEN+" - Toggles whether or not you recieve generic race messages (xp, increased ores, etc)");
        player.sendMessage(ChatColor.GREEN + "/"+label+" skills"+ChatColor.DARK_GREEN+" - Opens a skill tree");
        return true;
    }

    boolean titleSelection(Player sender) {
        system.putTitle(sender,new TitleGUI(sender));
        return true;
    }

    boolean menu(Player sender) {
        OCSelectionGUI ocgui = new OCSelectionGUI();
        sender.openInventory(ocgui.OCGUICreator(sender));
        return true;
    }

    boolean delete(Player sender, String[] args) {
        if (args[1].equals(args[2])) {
            String msg = system.getHandler().deleteOC(sender,args[1]);
            if (msg.equals("KICK")) {
                sender.kickPlayer(ChatColor.BOLD+"You may rejoin immediately!\n\n"+ChatColor.DARK_RED+"You deleted your current OC, so you have to rejoin the server to avoid issues.");
                return true;
            }
            sender.sendMessage(msg);
            return true;
        }
        sender.sendMessage(ChatColor.RED + "You must repeat the OC name twice in the command to confirm its deletion!");
        return false;
    }

    boolean info(Player sender) {
        OC oc = system.getOC(sender);
        if (oc!=null) {
            sender.sendMessage(ChatColor.WHITE + " ");
            sender.sendMessage(ChatColor.YELLOW+system.translate("==[ " + oc.getRace().toString() + " Race Information ]=="));
            RaceClass race = oc.getRaceClass();
            sender.sendMessage(ChatColor.DARK_GREEN+system.translate("==[Unlocked Abilities]=="));
            Skill tempTree = oc.getTree().constructTree();
            for (String str : tempTree.getUnlockedSkills(new ArrayList<>(), tempTree.children)) {
                sender.sendMessage(str);
            }
//            if (oc.getLevel()>=2) {
//                sender.sendMessage(ChatColor.DARK_GREEN+system.translate("==[Level 2 Abilities]=="));
//                for (String str : race.getInfo(2)) {
//                    sender.sendMessage(ChatColor.GREEN+"- "+str);
//                }
//            } else {
//                sender.sendMessage(ChatColor.DARK_RED+system.translate("==[Level 2 Abilities]=="));
//                for (String str : race.getInfo(2)) {
//                    sender.sendMessage(ChatColor.RED+"- "+str);
//                }
//            }
//            if (oc.getLevel()>=3) {
//                sender.sendMessage(ChatColor.DARK_GREEN+system.translate("==[Level 3 Abilities]=="));
//                for (String str : race.getInfo(3)) {
//                    sender.sendMessage(ChatColor.GREEN+"- "+str);
//                }
//            } else {
//                sender.sendMessage(ChatColor.DARK_RED+system.translate("==[Level 3 Abilities]=="));
//                for (String str : race.getInfo(3)) {
//                    sender.sendMessage(ChatColor.RED+"- "+str);
//                }
//            }
            sender.sendMessage(ChatColor.DARK_GREEN+system.translate("==[Race XP Gain Multipliers (all levels)]=="));
            sender.sendMessage(ChatColor.GREEN + "- HUNTING Multiplier: " + race.getMultiplier(ExpType.HUNTING));
            sender.sendMessage(ChatColor.GREEN + "- COMBAT Multiplier: " + race.getMultiplier(ExpType.COMBAT));
            sender.sendMessage(ChatColor.GREEN + "- MINING Multiplier: " + race.getMultiplier(ExpType.MINING));
            sender.sendMessage(ChatColor.GREEN + "- FARMING Multiplier: " + race.getMultiplier(ExpType.FARMING));
            sender.sendMessage(ChatColor.GOLD+"Remember, you can press 'Shift+F' and scroll to use active abilities");
            return true;
        }
        return false;
    }

    boolean stats(Player sender) {
        OC oc = system.getOC(sender);
        if (oc!=null) {
            sender.sendMessage(ChatColor.WHITE + " ");
            sender.sendMessage(ChatColor.YELLOW+system.translate("==[ Your OC Stats ]=="));
            sender.sendMessage(ChatColor.DARK_GREEN + system.translate("Name: ") + ChatColor.GREEN + oc.getName());
            sender.sendMessage(ChatColor.DARK_GREEN + system.translate("Race: ") + ChatColor.GREEN + oc.getRace().toString());
            sender.sendMessage(ChatColor.DARK_GREEN + system.translate("Level: ") + ChatColor.GREEN + oc.getLevel());
            sender.sendMessage(ChatColor.DARK_GREEN + system.translate("XP: ") + ChatColor.GREEN + (int)oc.getXP());
            sender.sendMessage(ChatColor.DARK_GREEN + system.translate("Time Played: ") + ChatColor.GREEN + secondsToTime(oc.getTimePlayed()));
            return true;
        }
        sender.sendMessage(ChatColor.RED + "You do not currently have an OC!");
        return false;
    }

    static String secondsToTime(int seconds) {
        seconds/=60;
        int s = seconds;
        int r = s/60;
        s-=(r*60);
        if (s==0) {
            return r+"h";
        }
        return r+"h "+s+"m";
    }
}
