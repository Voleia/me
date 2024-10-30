package me.voleia.volands.Abilities;

import me.voleia.volands.OC;
import me.voleia.volands.Volands;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.util.BlockIterator;

public class ForwardTeleport implements Ability{
    public boolean trigger(OC o, Player player, int level) {
        /*Location oldLocation = player.getLocation().toBlockLocation();
        Location location = player.getEyeLocation().toBlockLocation();
        Block block = player.getWorld().getBlockAt(location);
        if (block.getType().equals(Material.AIR) || location.distance(player.getLocation()) > 20) {
            //tp 20 forward - there isn't a block in the way or u would have hit it already.
            Vector travelVector = player.getLocation().getDirection().multiply(20);
            Location newLoc = player.getLocation().add(travelVector);
            player.teleport(newLoc);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
            System.out.println(newLoc.getBlock().getType().toString());
            System.out.println("method2");
            oldLocation.getWorld().spawnParticle(Particle.PORTAL,oldLocation,300,0.7,1.2,0.7);
            newLoc.getWorld().spawnParticle(Particle.PORTAL,oldLocation,100,0.7,1.2,0.7);
            return true;
        } else {
            if (block.getLightLevel()<=7) {
                Location tpLoc = new Location(location.getWorld(),location.getBlockX(),location.getBlockY()+1,location.getBlockZ());
                if (tpLoc.getBlock().getType()!=Material.AIR) {
                    tpLoc = new Location(location.getWorld(),location.getBlockX()+1,location.getBlockY()+1,location.getBlockZ());
                    if (tpLoc.getBlock().getType()!=Material.AIR) {
                        tpLoc = new Location(location.getWorld(),location.getBlockX()-1,location.getBlockY()+1,location.getBlockZ());
                        if (tpLoc.getBlock().getType()!=Material.AIR) {
                            tpLoc = new Location(location.getWorld(),location.getBlockX(),location.getBlockY()+1,location.getBlockZ()+1);
                            if (tpLoc.getBlock().getType()!=Material.AIR) {
                                tpLoc = new Location(location.getWorld(),location.getBlockX(),location.getBlockY()+1,location.getBlockZ()-1);
                            }
                        }
                    }
                }
                Location newLocation = tpLoc.toBlockLocation();
                System.out.println(newLocation.getBlock().getType().toString());
                if (!newLocation.getBlock().getType().equals(Material.AIR)) {
                    for (int i = newLocation.getBlockY();i<320;i+=1) {
                        Location loc = new Location(newLocation.getWorld(), newLocation.getBlockX(),i,newLocation.getBlockZ());
                        loc=loc.toBlockLocation();
                        if (loc.getBlock().getType().equals(Material.AIR)) {
                            tpLoc=loc;
                            i=999;
                        }
                    }
                }
                player.teleport(tpLoc);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                oldLocation.getWorld().spawnParticle(Particle.PORTAL,oldLocation,300,0.7,1.2,0.7);
                newLocation.getWorld().spawnParticle(Particle.PORTAL,oldLocation,100,0.7,1.2,0.7);
                return true;
            }
        }
        return false;*/
        World world = player.getWorld();
        BlockIterator iter = new BlockIterator(player, 32);
        Block block = iter.next();
        Location newLoc = null;
        while (iter.hasNext()) {
            block = iter.next();
            if (block.getType().equals(Material.AIR)) {
                if (block.getLightLevel()<8) {
                    newLoc = block.getLocation();
                }
                continue;
            }
            break;
        }
        if (newLoc==null) {
            return false;
        } else {
            Location oldLoc = player.getLocation();
            player.teleport(newLoc);
            player.getWorld().playSound(newLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
            player.getWorld().playSound(oldLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
            world.spawnParticle(Particle.PORTAL,oldLoc,1500,0.3,1,0.3);
            world.spawnParticle(Particle.PORTAL,newLoc,1500,0.3,1,0.3);
            return true;
        }
    }

    public int getCooldown() {
        return 35;
    }
    String name = Volands.getSystem().translate("Teleportation");

    public String getName() {
        return name;
    }

    public int getLevel() {
        return 3;
    }

    public String failureMessage() {
        return "There were no dark empty spaces ahead of you! The light level of your destination must be below 8.";
    }
}
