package me.voleia.volands.Commands;

import me.voleia.volands.OC;
import me.voleia.volands.Volands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RaceCommandTabCompletion implements TabCompleter {
    Volands system = Volands.getSystem();
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player && (label.equals("oc") || label.equals("race"))) {
            if (args.length==1) {
                List<String> parts = new ArrayList<String>() {};
                parts.add("menu");
                parts.add("delete");
                parts.add("info");
                parts.add("help");
                parts.add("stats");
                parts.add("rename");
                parts.add("title");
                parts.add("togglemsg");
                parts.add("skills");
                return parts;
            } else if ((args.length==2 || args.length==3) && args[0].equals("delete")) {
                List<String> parts = new ArrayList<String>() {};
                ArrayList<OC> OCs = system.getHandler().loadOCs((Player) sender);
                for (OC oc : OCs) {
                    parts.add(oc.getName());
                }
                return parts;
            }
        }
        return null;
    }
}
