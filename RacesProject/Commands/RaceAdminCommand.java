package me.voleia.volands.Commands;

import me.voleia.volands.CustomGUISystem.CustomGUI;
import me.voleia.volands.Enums.ChatMode;
import me.voleia.volands.Enums.Race;
import me.voleia.volands.OC;
import me.voleia.volands.SkillTree.TreeHuman;
import me.voleia.volands.Volands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;
import static me.voleia.volands.Volands.getSystem;

public class RaceAdminCommand implements CommandExecutor, TabCompleter {

    Volands system = Volands.getSystem();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player && (sender.isOp() || sender.hasPermission("volands.admin"))) {
            if (args.length>1) {
                if (args[0].equalsIgnoreCase("mod")) {
                    if (args.length==2 && args[1].equalsIgnoreCase("globalmute")) {
                        boolean result = system.getChat().toggleMute();
                        if (result) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendMessage(ChatColor.RED+sender.getName() + " has muted public chat!");
                            }
                            return true;
                        } else {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendMessage(ChatColor.GREEN+sender.getName() + " has unmuted public chat!");
                            }
                            return true;
                        }
                    }
                    else if (args.length==3) {
                        if (args[1].equalsIgnoreCase("unban")) {
                            if (unban(args[2])) {
                                sender.sendMessage(ChatColor.GREEN + "That player was unbanned!");
                                return true;
                            }
                            sender.sendMessage(ChatColor.RED + "There was an error unbanning that player.");
                            return false;
                        } else if (args[1].equalsIgnoreCase("unmute")) {
                            if (unmute(args[2])) {
                                sender.sendMessage(ChatColor.GREEN + "That player was unmuted!");
                                return true;
                            }
                            sender.sendMessage(ChatColor.RED + "There was an error unmuting that player.");
                            return false;
                        }
                    }
                    else if (args.length>=5) {
                        String reason="";
                        for (int i = 4; i < args.length; i++) {
                            reason+=args[i]+" ";
                        }
                        long time = parseStringToLong(args[3]);
                        if (time!=-1) {
                            if (args[1].equalsIgnoreCase("tempmute") || args[1].equalsIgnoreCase("mute")) {
                                if (mute(args[2], time,reason,sender.getName())) {
                                    sender.sendMessage(ChatColor.GREEN + "Muted that player!");
                                } else {
                                    sender.sendMessage(ChatColor.RED + "There was an error muting that player.");
                                }
                                return true;
                            } else if (args[1].equalsIgnoreCase("tempban") || args[1].equalsIgnoreCase("ban")) {
                                if (ban(args[2], time,reason,sender.getName())) {
                                    sender.sendMessage(ChatColor.GREEN + "Banned that player!");
                                } else {
                                    sender.sendMessage(ChatColor.RED + "There was an error banning that player.");
                                }
                                return true;
                            }
                        }
                    }
                }
                else if (args[0].equalsIgnoreCase("oc")) {
                    if (args.length==4) {
                        if (args[1].equalsIgnoreCase("rename")) {
                            Player player = Bukkit.getPlayer(args[2]);
                            if (player!=null && player.isOnline()) {
                                OC oc = system.getOC(player);
                                if (oc!=null) {
                                    oc.rename(args[3]);
                                    system.getHandler().saveOC(oc);
                                    return true;
                                }
                            }
                        }
                        else if (args[1].equalsIgnoreCase("setlevel")) {
                            Player player = Bukkit.getPlayer(args[2]);
                            if (player!=null && player.isOnline()) {
                                OC oc = system.getOC(player);
                                if (oc!=null) {
                                    try {
                                        oc.setLevel(Integer.parseInt(args[3]));
                                        system.getHandler().saveOC(oc);
                                        return true;
                                    } catch (Exception ignored) { }
                                }
                            }
                        }
                        else if (args[1].equalsIgnoreCase("setxp")) {
                            Player player = Bukkit.getPlayer(args[2]);
                            if (player!=null && player.isOnline()) {
                                OC oc = system.getOC(player);
                                if (oc!=null) {
                                    try {
                                        oc.setXP(Integer.parseInt(args[3]));
                                        system.getHandler().saveOC(oc);
                                        return true;
                                    } catch (Exception ignored) { }
                                }
                            }
                        }
                        else if (args[1].equalsIgnoreCase("settime")) {
                            Player player = Bukkit.getPlayer(args[2]);
                            if (player!=null && player.isOnline()) {
                                OC oc = system.getOC(player);
                                if (oc!=null) {
                                    int tmult;
                                    try {
                                        oc.setTimePlayed(Integer.parseInt(args[3]));
                                        system.getHandler().saveOC(oc);
                                        return true;
                                    } catch (Exception ignored) { }
                                }
                            }
                        }
                    }
                }
                else if (args[0].equalsIgnoreCase("debug")) {
                    if (args[1].equalsIgnoreCase("fixchat")) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            system.getRaceListener().ocSelectionMenu((player));
                            system.getChat().setChatMode(player, ChatMode.GLOBAL);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.sendMessage(ChatColor.RED + "Remember, you can use /chat <local/global> to switch chat modes!");
                                    cancel();
                                }
                            }.runTaskTimer(system,60,20); //repeat every second;
                        }
                    } else if (args[1].equalsIgnoreCase("reload")) {
                        system.reloadConfig();
                    } else if (args[1].equalsIgnoreCase("testtree")) {
                        CustomGUI test = new CustomGUI(6, ChatColor.WHITE + "SKILL TREE", Volands.getSystem().getOC((Player) sender), new TreeHuman(Volands.getSystem().getOC((Player) sender), new ArrayList<>()));
                    }
                }
            }
        }
        sender.sendMessage(ChatColor.RED+"You can't do that!");
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
       List<String> toReturn = new ArrayList<>();
       if (strings.length==1) {
           toReturn.add("oc");
           toReturn.add("mod");
           toReturn.add("debug");
           return toReturn;
       }
       else if (strings[0].equalsIgnoreCase("oc")) {
           if (strings.length==2) {
               toReturn.add("rename");
               toReturn.add("setlevel");
               toReturn.add("setxp");
               toReturn.add("settime");
               return toReturn;
           } else if (strings.length==3) {
               return listOfAllPlayers();
           } else if (strings.length==4) {
               toReturn.add("{value}");
               return toReturn;
           }
           return toReturn;
       }
       else if (strings[0].equalsIgnoreCase("mod")) {
           if (strings.length==2) {
               toReturn.add("globalmute");
               toReturn.add("tempmute");
               toReturn.add("tempban");
               toReturn.add("unban");
               toReturn.add("unmute");
               return toReturn;
           } else if (strings[1].equalsIgnoreCase("globalmute")) {
                return toReturn;
           } else if (strings.length==3) {
               return listOfAllPlayers();
           } else if (strings.length==4) {
                if (strings[1].equalsIgnoreCase("unban") || strings[1].equalsIgnoreCase("unmute")) {
                    return toReturn;
                } else if (strings[1].contains("temp")) {
                    toReturn.add("{time}{s/m/h/d}");
                    return toReturn;
                }
           } else {
               toReturn.add("{reason}");
               return toReturn;
           }
       }
       else if (strings[0].equalsIgnoreCase("debug")) {
           toReturn.add("fixchat");
           toReturn.add("reload");
           toReturn.add("testtree");
           return toReturn;
       }
       return toReturn;
    }

    List<String> listOfAllPlayers() {
        List<String> playernames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            playernames.add(player.getName());
        }
        return playernames;
    }
    public long parseStringToLong(String str) {
        try {
            if (str.contains("s")) {
                return Integer.parseInt(str.substring(0, str.indexOf("s"))) * 1000L;
            } else if (str.contains("m")) {
                return (long) Integer.parseInt(str.substring(0, str.indexOf("m"))) * 1000 * 60;
            } else if (str.contains("h")) {
                return (long) Integer.parseInt(str.substring(0, str.indexOf("h"))) * 1000 * 60 * 60;
            } else if (str.contains("d")) {
                return (long) Integer.parseInt(str.substring(0, str.indexOf("d"))) * 1000 * 60 * 60 * 24;
            } else if (str.contains("w")) {
                return (long) Integer.parseInt(str.substring(0, str.indexOf("d"))) * 1000 * 60 * 60 * 24 * 7;
            } else if (str.contains("y")) {
                return (long) Integer.parseInt(str.substring(0, str.indexOf("y"))) * 1000 * 60 * 60 * 24 * 365;
            } else if (str.contains("f")) {
                return Long.MAX_VALUE;
            }
        } catch (Exception ignored) {}
        return -1;
    }

    File folder;
    File bansfile;
    File mutesfile;

    public RaceAdminCommand() {
        folder = system.getDataFolder();
        createFiles();
    }

    void createFiles() {
        File t_bans = new File(folder, "bans.yml");
        if (!t_bans.exists()) {
            try {
                t_bans.createNewFile();
            } catch (Exception igored) {}
        }
        bansfile=t_bans;

        File t_mutes = new File(folder, "mutes.yml");
        if (!t_mutes.exists()) {
            try {
                t_mutes.createNewFile();
            } catch (Exception igored) {}
        }
        mutesfile=t_mutes;
    }

    boolean unban(String name) {
        FileConfiguration bans = YamlConfiguration.loadConfiguration(bansfile);
        bans.set(name,null);
        try {
            bans.save(bansfile);
            system.reloadConfig();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    boolean unmute(String name) {
        FileConfiguration mutes = YamlConfiguration.loadConfiguration(mutesfile);
        mutes.set(name,null);
        try {
            mutes.save(mutesfile);
            system.reloadConfig();
        } catch (Exception ignored) { }
        Player player = Bukkit.getPlayer(name);
        if (player!=null && player.isOnline()) {
            player.sendMessage(ChatColor.GREEN + "You have been unmuted!");
        }
        return true;
    }
    boolean ban(String person, long incTime, String reason, String banner) {
        if (incTime==-1) {
            return false;
        } else {
            long endTime=System.currentTimeMillis()+incTime;
            Player player = Bukkit.getPlayer(person);
            if (player!=null) {
                FileConfiguration bans = YamlConfiguration.loadConfiguration(bansfile);
                List<String> ban = new ArrayList<>();
                ban.add(String.valueOf(endTime));
                ban.add(reason);
                ban.add(banner);
                bans.set(player.getUniqueId().toString(),ban);
                try {
                    bans.save(bansfile);
                } catch (Exception ignored) {}
                system.reloadConfig();
                player.kickPlayer(isPlayerBanned(player));
                return true;
            } else {
                return false;
            }
        }
    }
    public boolean mute(String person, long incTime, String reason, String banner) {
        if (incTime==-1) {
            return false;
        } else {
            long endTime=System.currentTimeMillis()+incTime;
            Player player = Bukkit.getPlayer(person);
            if (player!=null) {
                FileConfiguration bans = YamlConfiguration.loadConfiguration(mutesfile);
                List<String> ban = new ArrayList<>();
                ban.add(String.valueOf(endTime));
                ban.add(reason);
                ban.add(banner);
                bans.set(person,ban);
                try {
                    bans.save(mutesfile);
                } catch (Exception ignored) {}
                system.reloadConfig();
                if (player.isOnline()) {
                    player.sendMessage(ChatColor.RED + isPlayerMuted(player));
                }
                return true;
            } else {
                return false;
            }
        }
    }

    String parseLongRemainingToString(long time) {
        return parseLongToString(time-System.currentTimeMillis());

    }
    String parseLongToString(long time) {
        long timeInSeconds=time/1000;
        if (timeInSeconds<60) {
            return timeInSeconds + " second"+singular(timeInSeconds);
        } else {
            long timeInMinutes=timeInSeconds/60;
            if (timeInMinutes<60) {
                return timeInMinutes + " minute"+singular(timeInMinutes);
            } else {
                long timeInHours=timeInMinutes/60;
                if (timeInHours<24) {
                    return timeInHours+" hour"+singular(timeInHours);
                } else {
                    long timeInDays=timeInHours/24;
                    return timeInDays+" day"+singular(timeInDays);
                }
            }
        }
    }
    String singular(long time) {
        if (time==1) {
            return "";
        }
        return "s";
    }

    public String isPlayerBanned(Player player) {
        FileConfiguration bans = YamlConfiguration.loadConfiguration(bansfile);
        Object t_ban = bans.get(player.getUniqueId().toString());
        if (t_ban==null) {
            return null;
        } else {
            List<String> ban = bans.getStringList(player.getUniqueId().toString());
            Long banTime = Long.parseLong(ban.get(0));
            if (banTime<=System.currentTimeMillis()) {
                unban(player.getName());
                return null;
            } else {
                String kickMessage = ChatColor.DARK_RED+"You are banned from Ashea!\n\n";
                kickMessage+=ChatColor.RED+"Your ban will end in " + parseLongRemainingToString(Long.valueOf(ban.get(0)));
                kickMessage+="\nYou were banned by " + ChatColor.DARK_RED + ban.get(2) + ChatColor.RED + " for " + ChatColor.DARK_RED + ban.get(1);
                kickMessage+=ChatColor.RED+"\n\nYou can appeal at our discord, "+ChatColor.BLUE+"discord.gg/ashea";
                return kickMessage;
            }
        }
    }

    public String isPlayerMuted(Player player) {
        FileConfiguration mutes = YamlConfiguration.loadConfiguration(mutesfile);
        Object t_mute = mutes.get(player.getName());
        if (t_mute==null) {
            return null;
        } else {
            List<String> mute = mutes.getStringList(player.getName());
            Long muteTime = Long.parseLong(mute.get(0));
            if (muteTime<=System.currentTimeMillis()) {
                unmute(player.getName());
                return null;
            }
            return ChatColor.RED+"You were muted by " + ChatColor.DARK_RED+ mute.get(2) +ChatColor.RED+ " due to " +ChatColor.DARK_RED+ mute.get(1)+ChatColor.RED + ". Your mute will end in about " + ChatColor.DARK_RED+parseLongRemainingToString(Long.valueOf(mute.get(0)));
        }
    }
}
