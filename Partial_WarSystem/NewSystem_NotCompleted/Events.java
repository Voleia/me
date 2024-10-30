package me.voleia.progressionwar;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Events implements Listener {

    ProgressionWar system = ProgressionWar.getSystem();

    @EventHandler
    public void onPlayerHitByPlayer(EntityDamageByEntityEvent e) { //prevent friendly fire. Prevent external players from attacking internal players.
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player reciever = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            Team recieverTeam = null;
            Team damagerTeam = null;
            for (WarFace war : system.getWars()) {
                for (Team t : war.getTeams()) {
                    if (t.playerIsOnTeam(reciever)) {
                        recieverTeam=t;
                    }
                    if (t.playerIsOnTeam(damager)) {
                        damagerTeam=t;
                    }
                }
            }
            if (!(recieverTeam==null && damagerTeam==null)) {
                if (recieverTeam!=null && damagerTeam==null) { //teamless player hits teamed player
                    e.setCancelled(true);
                    if (!damager.isOp()) {
                        damager.kickPlayer("Do not interfere in wars!\n\nYou may rejoin.");
                    }
                } else if (recieverTeam==null || recieverTeam.equals(damagerTeam)) { //reciever is teamless but damager is on team, or they are the same
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player dead = e.getEntity();
        Player killer = null;

        try {
            killer = e.getEntity().getKiller();
        } catch (Exception ex) { }
        for (WarFace war : system.getWars()) {
            boolean deadIsInWar = false;
            boolean killerIsInWar = false;
            for (Team t : war.getTeams()) {
                if (t.playerIsOnTeam(dead)) {
                    deadIsInWar=true;
                }
                if (killer!=null && t.playerIsOnTeam(killer)) {
                    killerIsInWar = true;
                }
            }
            if (deadIsInWar) {
                if (killerIsInWar) {
                    war.playerKilled(dead,killer);
                } else {
                    war.playerKilled(dead,null);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) { //if it is iron or beacon, cancel it. If it is a blacklist, cancel anything on the list. If it is a whitelist, llow it.
        if (e.getPlayer().isOp()) {
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            try {
                if (item.getType().equals(Material.CLOCK) && item.getItemMeta().getDisplayName().equals("BLOCKLIST ADDER")) {
                    List<String> curBlocks = system.getConfig().getStringList("blocks");
                    Block block = e.getBlock();
                    if (curBlocks.contains(block.getType().toString())) {
                        curBlocks.remove(block.getType().toString());
                        e.getPlayer().sendMessage(ChatColor.RED + "Removed " + block.getType().toString() + " from the list");
                    } else {
                        curBlocks.add(block.getType().toString());
                        e.getPlayer().sendMessage(ChatColor.GREEN + "Added " + block.getType().toString() + " to the list");
                    }
                    system.getConfig().set("blocks",curBlocks);
                    system.saveConfig();
                    system.reloadConfig();
                    e.setCancelled(true);
                    return;
                }
            } catch (Exception ignored) {}
        }
        if (system.isInWar(e.getPlayer())) {
            if (e.getBlock().getType().equals(Material.IRON_BLOCK) || e.getBlock().getType().equals(Material.BEACON)) {
                e.setCancelled(true);
                return;
            }
            if (system.getConfig().getBoolean("listIsBlacklist")) {
                if (system.getConfig().getStringList("blocks").contains(e.getBlock().getType().toString())) {
                    e.setCancelled(true);
                }
                return;
            }
            if (!system.getConfig().getStringList("blocks").contains(e.getBlock().getType().toString())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
       for (WarFace war : system.getWars()) {
           Team team = war.getTeam(e.getPlayer());
           if (team!=null) {
               team.forceRemovePlayer(e.getPlayer());
           }
       }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        for (WarFace war : system.getWars()) {
            if (war instanceof WarProg) {
                for (Team team : war.getTeams()) {
                    if (team.getPlayers().contains(player)) {
                        player.setGameMode(GameMode.SPECTATOR);
                        new HandleRespawns().onProgWarPlayerKilled(player,(WarProg) war,team);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBeaconClick(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getType().equals(Material.BEACON) || e.getClickedBlock().getType().equals(Material.IRON_BLOCK)) {
                if (system.isInWar(e.getPlayer())) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
