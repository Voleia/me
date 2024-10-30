package me.voleia.asheawarsystem.wars;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public class CapturePoint {

    private War war;
    Location pos;
    int radius;
    String name;
    double worth;
    int owner=-1;

    public String getName() {
        return name;
    }
    public CapturePoint(Location pos_, int radius_, String name_, double worth_, War war_) {
        war = war_;
        pos = pos_;
        radius = radius_;
        name = name_;
        worth = worth_;
    }

    public void updateControl() {
        ArrayList<Player> t0p = war.team0Players;
        ArrayList<Player> t1p = war.team1Players;
        int num0 = 0;
        int num1 = 0;
        for (int i = 0; i < t0p.size(); i+=1) {
            Player player = t0p.get(i);
            if (player.isOnline() && inRadius(player.getLocation()) && player.getGameMode().equals(GameMode.SURVIVAL)) {
                num0+=1;
            }
        }
        for (int i = 0; i < t1p.size(); i+=1) {
            Player player = t1p.get(i);
            if (player.isOnline() && inRadius(player.getLocation()) && player.getGameMode().equals(GameMode.SURVIVAL)) {
                num1+=1;
            }
        }

        if (num0 <= war.maxEnemies && num1>num0 && num1>=war.minEnemies) {
            owner=1;
        } else if (num0>num1 && num1 <= war.maxEnemies && num0>=war.minEnemies) {
            owner=0;
        }

        if (owner==1) {
            war.broadMsg(ChatColor.GOLD + name + ChatColor.RED + " is controlled by " + war.teamNames[1],2);
            war.broadMsg(ChatColor.RED + "+" + (Math.round((worth*war.getMultiplier())*10)/10) + " points",2);
            war.team1Points+=(worth*war.getMultiplier());
            placeWool(Material.RED_WOOL);
        } else if (owner == 0) {
            war.broadMsg(ChatColor.GOLD + name + ChatColor.BLUE + " is controlled by " + war.teamNames[0],2);
            war.broadMsg(ChatColor.BLUE + "+" + (Math.round((worth*war.getMultiplier())*10)/10) + " points",2);
            war.team0Points+=(worth*war.getMultiplier());
            placeWool(Material.BLUE_WOOL);
        } else {
            war.broadMsg(ChatColor.GREEN + "Neither side controls " + name,2);
            placeWool(Material.WHITE_WOOL);
        }
    }

    private void placeWool(Material mat) {
        //pos.getBlock().setType(mat);
        Location l2 = new Location(pos.getWorld(),pos.getX(),pos.getY(),pos.getZ());
        if (l2.getBlock().getType().equals(Material.WHITE_WOOL) || l2.getBlock().getType().equals(Material.AIR) || l2.getBlock().getType().equals(null) || l2.getBlock().getType().equals(Material.BLUE_WOOL) || l2.getBlock().getType().equals(Material.RED_WOOL)) {
            l2.getBlock().setType(mat);
        }
        l2.setX(pos.getX()-radius);
        l2.setZ(pos.getZ()-radius);
        if (l2.getBlock().getType().equals(Material.WHITE_WOOL) || l2.getBlock().getType().equals(Material.AIR) || l2.getBlock().getType().equals(null) || l2.getBlock().getType().equals(Material.BLUE_WOOL) || l2.getBlock().getType().equals(Material.RED_WOOL)) {
            l2.getBlock().setType(mat);
        }
        l2.setX(pos.getX()-radius);
        l2.setZ(pos.getZ()+radius);
        if (l2.getBlock().getType().equals(Material.WHITE_WOOL) || l2.getBlock().getType().equals(Material.AIR) || l2.getBlock().getType().equals(null) || l2.getBlock().getType().equals(Material.BLUE_WOOL) || l2.getBlock().getType().equals(Material.RED_WOOL)) {
            l2.getBlock().setType(mat);
        }
        l2.setX(pos.getX()+radius);
        l2.setZ(pos.getZ()+radius);
        if (l2.getBlock().getType().equals(Material.WHITE_WOOL) || l2.getBlock().getType().equals(Material.AIR) || l2.getBlock().getType().equals(null) || l2.getBlock().getType().equals(Material.BLUE_WOOL) || l2.getBlock().getType().equals(Material.RED_WOOL)) {
            l2.getBlock().setType(mat);
        }
        l2.setX(pos.getX()+radius);
        l2.setZ(pos.getZ()-radius);
        if (l2.getBlock().getType().equals(Material.WHITE_WOOL) || l2.getBlock().getType().equals(Material.AIR) || l2.getBlock().getType().equals(null) || l2.getBlock().getType().equals(Material.BLUE_WOOL) || l2.getBlock().getType().equals(Material.RED_WOOL)) {
            l2.getBlock().setType(mat);
        }
    }
    private boolean inRadius(Location plPos) {
        /*double dist = Math.sqrt( Math.pow(loc1.getX()-loc2.getX(),2) + Math.pow(loc1.getY()-loc2.getY(),2) + Math.pow(loc1.getZ()-loc2.getZ(),2));
        if (dist>radius) {
            return false;
        }
        return true;*/
        //loc1 = cap point
        //loc2 = player

        /*double dist = Math.sqrt(Math.pow(loc1.getX()-loc2.getX(),2) + Math.pow(loc1.getZ()-loc2.getZ(),2));
        if (dist<radius && loc2.getY() > (loc1.getY()-2) && loc2.getY()<(loc1.getY()+8)) {
            return true;
        }
        return false;*/

        if (plPos.getX() < pos.getX()+radius && plPos.getX() > pos.getX()-radius && plPos.getZ() < pos.getZ()+radius && plPos.getZ() > pos.getZ()-radius && plPos.getY() > pos.getY()-2 && pos.getY() < pos.getY()+6) {
            return true;
        }
        return false;
    }

}
