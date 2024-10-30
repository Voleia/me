package me.voleia.progressionwar;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CapturePoint {

    final Location center;
    int radius;
    int control;
    int controllingTeam;
    WarProg parent;
    int tickCount = 0;
    String name;

    public CapturePoint(WarProg parent_, Location center_, int radius_, String name_) {
        control = 100;
        radius = radius_;
        center = center_;
        parent = parent_;
        name = name_;
    }

    public void setStartingTeam(int t) {
        control=t;
    }

    public void onTick() { //if team A has a higher percentage of people than team B,
        tickCount+=1;
        if (tickCount>=20) {
            tickCount=0;
        }

        int opposition;
        if (controllingTeam==0) {
            opposition=1;
        } else {
            opposition=0;
        }

        double conPercent = percentInPoint(parent.getTeams().get(controllingTeam));
        double oppPercent = percentInPoint(parent.getTeams().get(opposition));
        Team oppTeam = parent.teams.get(opposition);
        Team conTeam = parent.teams.get(controllingTeam);

        if (numInPoint(conTeam) == 1 && numInPoint(oppTeam) == 1) {
            //t0Points=t1Points capture will not occur at all if both teams have exactly 1 player
            return;
        }

        if (conPercent > oppPercent) {
            control += 1;
        } else if (oppPercent > conPercent) {
            control-=2;
        }

        if (control>100) {
            control=100;
        } else if (control <= 0) {
            parent.controlSwitch(opposition);
            int oldCon=controllingTeam;
            controllingTeam=opposition;
            opposition=oldCon;
            controllingTeam = 100;
        }

        placeBlocksAtRadius();
    }

    double percentInPoint(Team t) {
        double tp = 0;
        for (Player p : t.getPlayers()) {
            tp+=playerInPoint(p);
        }
        return tp/Double.valueOf(t.getPlayers().size());
    }

    int numInPoint(Team t) {
        int tp = 0;
        for (Player p : t.getPlayers()) {
            tp+=playerInPoint(p);
        }
        return tp;
    }

    int playerInPoint(Player player) {
        Location playerLoc = player.getLocation();
        if (playerLoc.getWorld()==center.getWorld()) {
            if (playerLoc.getBlockX()>=center.getBlockX()-radius && playerLoc.getBlockX()<=center.getBlockX()+radius &&
                    playerLoc.getBlockZ()>=center.getBlockZ()-radius && playerLoc.getBlockZ()<=center.getBlockZ()+radius &&
            playerLoc.getBlockY()>=center.getBlockY()-2 && playerLoc.getBlockY()<=center.getBlockY()+6) {
                return 1;
            }
        }
        return 0; //write something here to detect if a player is within point boundaries
    }

    HashMap<Location, Material> oldBlocks = new HashMap<>();
    void placeBeacon() {
        Location bloc = center.clone();;
        bloc.setY(bloc.getY()+7);
        setBlock(bloc,Material.BEACON);
        bloc.setY(bloc.getY()-1);
        //3 x's
        bloc.setX(bloc.getX()-1);
        setBlock(bloc,Material.IRON_BLOCK);
        bloc.setZ(bloc.getZ()-1);
        setBlock(bloc,Material.IRON_BLOCK);
        bloc.setZ(bloc.getZ()+2);
        setBlock(bloc,Material.IRON_BLOCK);
        bloc.setZ(bloc.getZ()-1);
        //middles
        bloc.setX(bloc.getX()+1);
        setBlock(bloc,Material.IRON_BLOCK);
        bloc.setZ(bloc.getZ()-1);
        setBlock(bloc,Material.IRON_BLOCK);
        bloc.setZ(bloc.getZ()+2);
        setBlock(bloc,Material.IRON_BLOCK);
        bloc.setZ(bloc.getZ()-1);
        //ends
        bloc.setX(bloc.getX()+1);
        setBlock(bloc,Material.IRON_BLOCK);
        bloc.setZ(bloc.getZ()-1);
        setBlock(bloc,Material.IRON_BLOCK);
        bloc.setZ(bloc.getZ()+2);
        setBlock(bloc,Material.IRON_BLOCK);

        bloc = center.clone();;
        bloc.setY(bloc.getY()+8);
        while (bloc.getY()<320) {
            if (!bloc.getBlock().getType().equals(Material.AIR)) {
                setBlock(bloc,Material.AIR);
            }
            bloc.setY(bloc.getY()+1);
        }
    }

    void setBlock(Location loc, Material newBlock) {
        oldBlocks.put(new Location(loc.getWorld(),loc.getBlockX(),loc.getBlockY(),loc.getBlockZ()), loc.getBlock().getType());
        loc.getBlock().setType(newBlock);
    }

    public void reverseBlocks() {
        for (Location loc : oldBlocks.keySet()) {
            loc.getBlock().setType(oldBlocks.get(loc));
        }
        oldBlocks.clear();
    }

    void placeBlocksAtRadius() {
        Material b;
        Material a;
        Location glass = center;
        glass.setY(glass.getY()+8);
        if (controllingTeam==0) {
            b = Material.GREEN_WOOL;
            a = Material.RED_WOOL;
            glass.getBlock().setType(Material.GREEN_STAINED_GLASS);
        } else {
            b = Material.RED_WOOL;
            a = Material.GREEN_WOOL;
            glass.getBlock().setType(Material.RED_STAINED_GLASS);
        }
        int totalBlocks = (radius+radius+1)*4-4;
        int numBlocksA = (100-control)/totalBlocks;
        for (int x = center.getBlockX()-radius; x<=center.getBlockX()+radius; x+=1) {
            Location loc = new Location(center.getWorld(), x, center.getBlockY()-1, center.getBlockZ()-radius);
            if (numBlocksA>0) {
                loc.getBlock().setType(a);
                numBlocksA--;
            } else {
                loc.getBlock().setType(b);
            }
        }
        for (int x = center.getBlockX()-radius; x<=center.getBlockX()+radius; x+=1) {
            Location loc = new Location(center.getWorld(), x, center.getBlockY()-1, center.getBlockZ()+radius);
            loc.getBlock().setType(b);
            if (numBlocksA>0) {
                loc.getBlock().setType(a);
                numBlocksA--;
            } else {
                loc.getBlock().setType(b);
            }
        }
        for (int z = center.getBlockZ()-radius+1; z<=center.getBlockZ()+radius; z+=1) {
            Location loc = new Location(center.getWorld(), center.getBlockX()+radius, center.getBlockY()-1, z);
            loc.getBlock().setType(b);
            if (numBlocksA>0) {
                loc.getBlock().setType(a);
                numBlocksA--;
            } else {
                loc.getBlock().setType(b);
            }
        }
        for (int z = center.getBlockZ()+radius-1; z<=center.getBlockZ()+radius; z+=1) {
            Location loc = new Location(center.getWorld(), center.getBlockX()-radius, center.getBlockY()-1, z);
            loc.getBlock().setType(b);
            if (numBlocksA>0) {
                loc.getBlock().setType(a);
                numBlocksA--;
            } else {
                loc.getBlock().setType(b);
            }
        }
    }

    public void reset() {
        control = 100;
        tickCount=0;
        reverseBlocks();
        placeBeacon();
    }

    //a b c d
    //b controlled by team 1 (100)
    //c controlled by team 2 (100)
    //battle shifts in favor of whichever one drops to zero first
    //attacking is easier than defending
    //capturing based on whichever side has a higher proportion of their players in the center
}
