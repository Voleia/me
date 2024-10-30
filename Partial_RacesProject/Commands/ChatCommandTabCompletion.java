package me.voleia.volands.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChatCommandTabCompletion implements TabCompleter {
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("mode") || label.equalsIgnoreCase("chat")) {
            List<String> str = new ArrayList<String>();
            str.add("global");
            str.add("local");
            str.add("races");
            str.add("whisper");
            str.add("rank");
            return str;
        }
        return null;
    }
}
