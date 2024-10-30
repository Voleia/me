package me.voleia.volands.Config;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.voleia.volands.OC;
import me.voleia.volands.Volands;
import org.apache.commons.lang3.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PHExpansion extends PlaceholderExpansion {

    private final Volands plugin;

    public PHExpansion(Volands plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "volands";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Voleia";
    }

    @Override
    public @NotNull String getVersion() {
        return "7.24";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player p, @NotNull String identifier) {
        OC oc = plugin.getOC(p);
        if (oc!=null) {
            if (identifier.equalsIgnoreCase("level")) {
                return String.valueOf(oc.getLevel());
            } else if (identifier.equalsIgnoreCase("xp")) {
                return String.valueOf((int)oc.getXP());
            } else if (identifier.equalsIgnoreCase("race")) {
                return oc.getRace().toString();
            } else if (identifier.equalsIgnoreCase("ocname")) {
                return oc.getName();
            } else if (identifier.equalsIgnoreCase("ocprefix")) {
                if (oc.getPrefix().equals("Blank")) {
                    return "";
                } else {
                    return oc.getPrefix();
                }
            } else if (identifier.equalsIgnoreCase("ocsuffix")) {
                if (oc.getSuffix().equals("Blank")) {
                    return "";
                } else {
                    return oc.getSuffix();
                }
            } else if (identifier.equalsIgnoreCase("ocprefixspace")) {
                if (oc.getPrefix().equals("Blank")) {
                    return "";
                } else {
                    return oc.getPrefix()+" ";
                }
            } else if (identifier.equalsIgnoreCase("ocsuffixspace")) {
                if (oc.getSuffix().equals("Blank")) {
                    return "";
                } else {
                    return " "+oc.getSuffix();
                }
            } else if (identifier.equalsIgnoreCase("racetag")) {
                return plugin.getRaceIcon(oc.getRace());
            } else if (identifier.equalsIgnoreCase("racecolor")) {
                return ""+plugin.getChatColor(oc.getRace());
            } else if (identifier.equalsIgnoreCase("genprefix")) {
                if (plugin.getChat().prefixMap.containsKey(p) && plugin.getChat().prefixMap.get(p)) {
                    return PlaceholderAPI.setPlaceholders(p, "%luckperms_prefix%");
                } else {
                    return plugin.getRaceIcon(oc.getRace());
                }
            } else if (identifier.equalsIgnoreCase("genprefixcolor")) {
                if (plugin.getChat().prefixMap.containsKey(p) && plugin.getChat().prefixMap.get(p)) {
                    return ""+plugin.getChat().getColor(PlaceholderAPI.setPlaceholders(p, "%luckperms_prefix%"));
                } else {
                    return ""+plugin.getChatColor(oc.getRace());
                }
            }
        } else {
            return "No OC";
        }
        return null;
    }
}
