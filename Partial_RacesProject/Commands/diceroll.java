package me.voleia.volands.Commands;

import me.voleia.volands.Volands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class diceroll implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player && strings.length==1 && strings[0].contains("d")) {
            try {
                Random rand = new Random();
                int numDice = Integer.valueOf(strings[0].substring(0,strings[0].indexOf("d")));
                int numFaces = Integer.valueOf(strings[0].substring(strings[0].indexOf("d")+1));
                if (numDice>=1 && numFaces>1) {
                    List<String> rollResults = new ArrayList<String>();
                    for (int i = 0; i < numDice; i+=1) {
                        rollResults.add(String.valueOf(rand.nextInt(1,numFaces+1)));
                    }
                    String message;
                    if (numDice==1) {
                        message = commandSender.getName()+" ("+Volands.getSystem().getOC((Player) commandSender).getName()+") has rolled a die with " + numFaces + " sides. This is the result: ";
                    } else {
                        message = commandSender.getName()+" ("+Volands.getSystem().getOC((Player) commandSender).getName()+") has rolled " + numDice + " dice with " + numFaces + " sides each. These are the results: ";
                    }
                    for (int i = 0; i < rollResults.size()-1; i+=1) {
                        message+=rollResults.get(i)+", ";
                    }
                    message+=rollResults.get(rollResults.size()-1)+".";

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getLocation().distance(((Player) commandSender).getLocation()) <= 48) {
                            player.sendMessage(ChatColor.GREEN+message);
                        }
                    }
                    return true;
                }
            } catch (Exception ignored) {}
        }
        commandSender.sendMessage("Format: /diceroll <numberOfDice>d<numberOfSides> - For example, '/diceroll 1d20'");
        return false;
    }
}
