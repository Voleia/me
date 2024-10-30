package me.voleia.progressionwar;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PHExpansion extends PlaceholderExpansion {

    private final ProgressionWar plugin;

    public PHExpansion(ProgressionWar plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "progwar";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Voleia";
    }

    @Override
    public @NotNull String getVersion() {
        return "9.28.24";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player p, @NotNull String identifier) {
        if (identifier.equalsIgnoreCase("team")) {
            for (WarFace war : plugin.getWars()) {
                Team team = war.getTeam(p);
                if (team!=null) {
                    if (team.getDeadPlayers().contains(p)) {
                        return ChatColor.translateAlternateColorCodes('&', "&4&lDEAD ");
                    } else {
                        return ChatColor.translateAlternateColorCodes('&', getColorFromTeamNum(team.teamIndex)+team.getName()+" ");
                    }
                }
            }
            return "";
        } else if (identifier.equalsIgnoreCase("kills")) {
            for (WarFace war : plugin.getWars()) {
                Team team = war.getTeam(p);
                if (team!=null) {
                    return " " + String.valueOf(team.getKills(p)) + " kills";
                }
            }
            return "";
        } else if (identifier.equalsIgnoreCase("progwarinfo")) {
            for (WarFace war : plugin.getWars()) {
                if (war instanceof WarProg) {
                    WarProg prog = (WarProg) war;
                    Team team = war.getTeam(p);
                    if (team!=null) {
                        return prog.placehold;
                    }
                }
            }
            return "";
        }
        else return "";
    }

    public String getColorFromTeamNum(int num) {
        switch (num) {
            case 0:
                return "&a&l";
            case 1:
                return "&c&l";
            case 2:
                return "&9&l";
            case 3:
                return "&e&l";
            case 4:
                return "&d&l";
            default:
                return "&1&l";
        }
    }
}