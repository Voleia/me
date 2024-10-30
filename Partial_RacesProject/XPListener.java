package me.voleia.volands;

import me.voleia.volands.Commands.ChatCommand;
import me.voleia.volands.CustomGUISystem.Funcs.InventoryFunc;
import me.voleia.volands.Enums.ExpType;
import me.voleia.volands.Enums.Race;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;

import java.util.ArrayList;
import java.util.Random;

public class XPListener implements Listener {

    ArrayList<Material> ores = new ArrayList<>();
    ArrayList<Material> fish = new ArrayList<>();
    Volands system = Volands.getSystem();
    Random rand = new Random();

    public XPListener() {
        ores.add(Material.COAL_ORE);
        ores.add(Material.COPPER_ORE);
        ores.add(Material.DIAMOND_ORE);
        ores.add(Material.IRON_ORE);
        ores.add(Material.EMERALD_ORE);
        ores.add(Material.GOLD_ORE);
        ores.add(Material.LAPIS_ORE);
        ores.add(Material.REDSTONE_ORE);
        ores.add(Material.DEEPSLATE_COAL_ORE);
        ores.add(Material.DEEPSLATE_COPPER_ORE);
        ores.add(Material.DEEPSLATE_DIAMOND_ORE);
        ores.add(Material.DEEPSLATE_IRON_ORE);
        ores.add(Material.DEEPSLATE_EMERALD_ORE);
        ores.add(Material.DEEPSLATE_GOLD_ORE);
        ores.add(Material.DEEPSLATE_LAPIS_ORE);
        ores.add(Material.DEEPSLATE_REDSTONE_ORE);

        fish.add(Material.SALMON);
        fish.add(Material.TROPICAL_FISH);
        fish.add(Material.COOKED_SALMON);
        fish.add(Material.COD);
        fish.add(Material.PUFFERFISH);
        fish.add(Material.COOKED_COD);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void CombatEvent(EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            if (e.getDamager() instanceof Player) {
                Player player = (Player) e.getDamager();
                OC oc = system.getOC(player);
                if (oc != null) {
                    if (e.getEntity() instanceof Monster) {
                        oc.addXP(ExpType.COMBAT, rand.nextDouble(1, 3));
                    } else if ((e.getEntity() instanceof Player && system.getOC((Player) e.getEntity()) != null) || e.getEntity() instanceof Animals) {
                        if (e.getEntity() instanceof Player) {
                            OC oc2 = system.getOC((Player) e.getEntity());
                            if (!oc2.getRace().equals(Race.HUMAN) && oc.getRace().equals(Race.HUMAN)) {
                                oc.addXP(ExpType.HUNTING, rand.nextDouble(1, 1.1 + e.getDamage()));
                            } else {
                                if (!oc.combatCooldown((Player) e.getEntity())) {
                                    oc.addXP(ExpType.COMBAT, rand.nextDouble(1, 1.1 + e.getDamage()));
                                }
                            }
                        } else {
                            oc.addXP(ExpType.HUNTING, rand.nextDouble(1, 1.1 + e.getDamage()));
                        }
                    }
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void KillEvent(EntityDeathEvent e) {
        if (!e.isCancelled()) {
            if (e.getEntity().getKiller()!=null) {
                Player player = e.getEntity().getKiller();
                OC oc = system.getOC(player);
                if (oc!=null) {
                    if (e.getEntity() instanceof Monster) {
                        oc.addXP(ExpType.COMBAT,rand.nextDouble(10,30));
                    } else if ((e.getEntity() instanceof Player && system.getOC((Player) e.getEntity())!=null) || e.getEntity() instanceof Animals){
                        if (e.getEntity() instanceof Player) {
                            if (!oc.combatCooldown((Player)e.getEntity())) {
                                OC oc2 = system.getOC((Player) e.getEntity());
                                if (!oc2.getRace().equals(Race.HUMAN) && oc.getRace().equals(Race.HUMAN)) {
                                    oc.addXP(ExpType.HUNTING,rand.nextDouble(10,30));
                                } else {
                                    oc.addXP(ExpType.COMBAT,rand.nextDouble(10,30));
                                }
                                oc.addCooldown((Player) e.getEntity(),180);
                            } else {
                                oc.sendRaceMessage(ChatColor.RED + "You didn't get any points for that kill because you've been killing that player a lot lately!");
                            }
                        } else {
                            oc.addXP(ExpType.HUNTING,rand.nextDouble(10,30));
                        }
                    }
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void FarmMineEvent(BlockBreakEvent e) {
        if (!e.isCancelled()) {
            Block block = e.getBlock();
            if (block.getBlockData() instanceof org.bukkit.block.data.Ageable && !block.getDrops().isEmpty() && ((Ageable) block.getBlockData()).getAge()>=7) {
                OC oc = system.getOC(e.getPlayer());
                if (oc!=null) {
                    oc.addXP(ExpType.FARMING, rand.nextDouble(5,10));
                }
            } else if (ores.contains(block.getType())) {
                OC oc = system.getOC(e.getPlayer());
                if (oc!=null) {
                    oc.addXP(ExpType.MINING, rand.nextDouble(4,8));
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void HarvestEvent(PlayerHarvestBlockEvent e) {
        if (!e.isCancelled()) {
            OC oc = system.getOC(e.getPlayer());
            if (oc!=null) {
                oc.addXP(ExpType.FARMING, rand.nextDouble(5,10));
            }
        }
    }
}
