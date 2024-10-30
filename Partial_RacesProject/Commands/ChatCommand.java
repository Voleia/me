package me.voleia.volands.Commands;

import me.voleia.volands.Enums.ChatMode;
import me.voleia.volands.Listeners.ChatListener;
import me.voleia.volands.Volands;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatCommand implements CommandExecutor {

    Volands system = Volands.getSystem();
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(label.equalsIgnoreCase("chat") || label.equalsIgnoreCase("mode")) {
                if (args.length==1) {
                    if (args[0].equalsIgnoreCase("g") || args[0].equalsIgnoreCase("global") || args[0].equalsIgnoreCase("ooc")) {
                        system.getChat().setChatMode(player,ChatMode.GLOBAL);
                        return true;
                    } else if (args[0].equalsIgnoreCase("l") || args[0].equalsIgnoreCase("local")) {
                        system.getChat().setChatMode(player,ChatMode.LOCAL);
                        return true;
                    } else if (args[0].equalsIgnoreCase("r") || args[0].equalsIgnoreCase("races")) {
                        system.getChat().setChatMode(player,ChatMode.RACE);
                        return true;
                    } else if (args[0].equalsIgnoreCase("w") || args[0].equalsIgnoreCase("whisper")) {
                        system.getChat().setChatMode(player,ChatMode.WHISPER);
                        return true;
                    } else if (args[0].equalsIgnoreCase("rank")) {
                        ChatListener chat = system.getChat();
                        if (!chat.prefixMap.containsKey(player) || !chat.prefixMap.get(player)) {
                            chat.prefixMap.put(player,true);
                        } else {
                            chat.prefixMap.put(player,false);
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "==[ " + system.translate("Chat Commands") + " ]==");
                    sender.sendMessage(ChatColor.GREEN + "/"+label+" <global/local/race/whisper>");
                    sender.sendMessage(ChatColor.GREEN + "/"+label+" rank (switch between rank or race prefix in chat and above head)");
                    sender.sendMessage(ChatColor.GREEN + "/global");
                    sender.sendMessage(ChatColor.GREEN + "/local");
                    sender.sendMessage(ChatColor.GREEN + "/races");
                    sender.sendMessage(ChatColor.GREEN + "/muteglobal");
                }
            } else if (label.equalsIgnoreCase("global") || label.equalsIgnoreCase("ooc") || label.equalsIgnoreCase("g")) {
                system.getChat().setChatMode(player,ChatMode.GLOBAL);
                return true;
            } else if (label.equalsIgnoreCase("local")) {
                system.getChat().setChatMode(player,ChatMode.LOCAL);
                return true;
            } else if (label.equalsIgnoreCase("races")) {
                system.getChat().setChatMode(player, ChatMode.RACE);
                return true;
            } else if (label.equalsIgnoreCase("globalmute") || label.equalsIgnoreCase("muteglobal")) {
                system.getChat().toggleGlobalMute(player);
                return true;
            }
        }
        return false;
    }
}
