package me.voleia.volands;

import me.voleia.volands.Abilities.Ability;
import me.voleia.volands.Abilities.ToggleableAbility;
import me.voleia.volands.Abilities.TriggerClimb;
import me.voleia.volands.Enums.ExpType;
import me.voleia.volands.Enums.Race;
import me.voleia.volands.Races.*;
import me.voleia.volands.SkillTree.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class OC {

    //saved features
    Player player;
    Race race;
    int level;
    double xp;
    RaceClass handler;
    String name;
    int id;

    public int getId() {
        return id;
    }

    public void setID(int id_) {
        id=id_;
    }

    public void setLevel(int sl) {
        level=sl;
    }
    //instanced features
    boolean menuOpen = false;
    int abilitySlot = 0;
    int cooldown=0; //1 per second
    Volands system;
    Ability currentAbility;
    int timePlayed=0;

    public String getName() {
        return name;
    }

    public String getUUID() {
        return player.getUniqueId().toString();
    }

    public int getLevel() {
        return level;
    }

    public double getXP() {
        return xp;
    }

    public Race getRace() {
        return race;
    }

    int curFrame=0;

    String prefix;
    String suffix;

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setPrefix(String pref) {
        prefix=pref;
    }

    public void setSuffix(String suf) {
        suffix=suf;
    }

    HashMap<UUID, Long> expCooldown = new HashMap<>();

    public void addCooldown(Player player_, int seconds) {
        long time_ = seconds*1000L;
        expCooldown.put(player_.getUniqueId(),time_);
    }

    public boolean combatCooldown(Player player_) {
        if (!expCooldown.containsKey(player_.getUniqueId()) || System.currentTimeMillis()>expCooldown.get(player_.getUniqueId())) {
            return false;
        }
        return true;
    }

    public Location lastPos;
    public Location newPos;
    double speed;

    public Double getSpeed() {
        return speed;
    }

    boolean spamToggled = false;

    public void toggleSpam() {
        spamToggled=!spamToggled;
    }

    public boolean getSpamToggled() {
        return spamToggled;
    }

    HashMap<String, Player> grantedTitles;

    public HashMap<String, Player> getGrantedTitles() {
        return grantedTitles;
    }

    public void addTitle(String title, Player granter) {
        grantedTitles.put(title,granter);
        system.getHandler().saveOC(this);
    }

    public OC (Race r, Player p, int lvl, double exp, String nm, int tp, int id_, String prefix_, String suffix_, boolean spam, List<String> myTree){// HashMap<String, Player>titles) {
        grantedTitles=new HashMap<>();//=titles;
        spamToggled=spam;
        lastPos=p.getLocation();
        newPos=p.getLocation();
        prefix=prefix_;
        suffix=suffix_;
        for (PotionEffect pot : p.getActivePotionEffects()) {
            p.removePotionEffect(pot.getType());
        }
        id=id_;
        race = r;
        system = Volands.getSystem();
        timePlayed=tp;
        switch (race) {
            case AMBYSTOMA:
                handler = new Ambystoma();
                tree = new TreeAmbystoma();
                break;
            case DEMON:
                handler = new Demon();
                tree = new TreeDemon();
                break;
            case DRAGONBORN:
                handler = new Dragonborn();
                tree = new TreeDragonborn();
                break;
            case DWARF:
                handler = new Dwarf();
                tree = new TreeDwarf();
                break;
            case ELF:
                handler = new Elf();
                tree = new TreeElf();
                break;
            case HUMAN:
                handler = new Human();
                tree = new TreeHuman(this,myTree);
                break;
            case NEKO:
                handler = new Neko();
                tree = new TreeNeko();
                break;
            case DARKELF:
                handler = new Shadowkin();
                tree = new TreeShadowkin();
                break;
            case WRAITH:
                handler = new Wraith();
                tree = new TreeWraith();
                break;
        }
        player = p;
        level = lvl;
        xp = exp;
        name = nm;

        if (handler.getAbilities().length>0) {
            currentAbility=handler.getAbilities()[0];
        } else {
            currentAbility=null;
        }

        system.getHandler().saveOC(this);

        /*if (r.equals(Race.DRAGONBORN)) { //dragon wings
            ItemStack elytra = new ItemStack(Material.ELYTRA);
            elytra.addEnchantment(Enchantment.BINDING_CURSE, 1);
            player.getInventory().setItem(38,elytra);
        } else {
            if (player.getInventory().getItem(38)!=null && player.getInventory().getItem(38).getType().equals(Material.ELYTRA)) {
                player.getInventory().clear(38);
            }
        }*/

        oldXP=xp;
        exType=ExpType.FARMING;

        int biggestCooldownTime = 0;
        for (Ability ability : getRaceClass().getAbilities()) {
            if (ability.getCooldown()>biggestCooldownTime) {
                biggestCooldownTime=ability.getCooldown();
            }
        }
        cooldown = biggestCooldownTime;
    } //initialize new OC

    public void setXP(int newXP) {
        xp = (double) newXP;
    }

    public int getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(int tim) {
        timePlayed=tim;
    }

    int curTick=0;

    public void onTick() { //this happens every tick
        if (race.equals(Race.DRAGONBORN)) {
            newPos=player.getLocation();
            speed=newPos.distance(lastPos);
            lastPos=newPos;
        }
        curTick+=1;
        if (cooldown>0) {
            cooldown-=1;
        }
        if (menuOpen) {
            renderMenu();
        }
        timePlayed+=1;
        if (curTick>=180) {
            system.getHandler().saveOC(this);
        }
        handler.onTick(player, this);

        curFrame+=1;
        if (curFrame>5) {
            curFrame=0;
            if (xp!=oldXP) {
                sendRaceMessage(ChatColor.YELLOW + "You gained " + ChatColor.GREEN + (Double.valueOf(((int)((xp-oldXP)*10)))/10) + ChatColor.YELLOW + " Race XP from " + ChatColor.GREEN + exType.toString() + ChatColor.YELLOW + "\nLevel " + level + " (" + ((int)xp) + "xp) - " + ((int)(5000.0-(xp-(level*5000.0))))+" xp until next level up");
                if (oldLevel!=level) {
                    if (level==3) {
                        player.sendMessage(ChatColor.GOLD + "You have reached Race Level 3! No more abilities will be granted after this level, and if any are added in the future then your level may be reset to back to level 3- However you can still level up after this point for purely cosmetic purposes.");
                    }
                    if (level<=3) {
                        player.sendMessage(ChatColor.GOLD + "Congratulations! You have levelled up to Race Level " + level + ". Use the command "+ChatColor.YELLOW+"'/race info'"+ChatColor.GOLD+" to view your abilities!");
                    } else {
                        player.sendMessage(ChatColor.GOLD + "Congratulations! You have levelled up to Race Level " + level + ".");
                    }
                }
            }
            oldXP=xp;
            oldLevel=level;
        }
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            if (!system.hasArmorStack(player)) {
                system.saveArmor(player);
            }
        } else {
            if (system.hasArmorStack(player)) {
                system.loadArmor(player);
                system.removeArmor(player);
            }
        }
    }

    int oldLevel;
    //Event
    public void abilityMenuEvent(PlayerSwapHandItemsEvent e) {
        if (handler.getAbilities().length!=0) {
            if (menuOpen) {
                menuOpen=false;
                player.sendTitle("Closing Menu", "...", 0,3,5);
            } else {
                menuOpen=true;
                renderMenu();
            }
        } else {
            player.sendMessage(ChatColor.RED + race.toString() + " does not have any active abilities!");
        }
    }

    public RaceClass getRaceClass() {
        return handler;
    }

    //scroll event
    public boolean scrollEvent(PlayerItemHeldEvent e) {
        if (menuOpen) {
            if (e.getNewSlot()<e.getPreviousSlot() || (e.getNewSlot()==8 && e.getPreviousSlot()==0)) {
                abilitySlot+=1;
            } else {
                abilitySlot-=1;
            }
            if (abilitySlot<0) {
                abilitySlot=handler.getAbilities().length-1;
            } else if (abilitySlot>=handler.getAbilities().length) {
                abilitySlot=0;
            }
            e.getPlayer().getInventory().setHeldItemSlot(e.getPreviousSlot());

            currentAbility=handler.getAbilities()[abilitySlot];

            renderMenu();
            e.setCancelled(true);
        }
        return menuOpen;
    }

    public void activateAbility() {
        if (menuOpen) {
            Ability ability = handler.getAbilities()[abilitySlot];
            if (cooldown<=0 || ability.getCooldown() == 0) {
                if (level>=ability.getLevel()) {
                    if (ability.trigger(this,player,level)) {
                        if (ability.getCooldown()!=0) {
                            cooldown = ability.getCooldown();
                        }
                        renderMenu();
                    } else {
                        player.sendMessage(ChatColor.RED + ability.failureMessage());
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "That ability requires you to be level " + ability.getLevel());
                }
            } else {
                player.sendMessage(ChatColor.RED + "Your abilities are on cooldown for " + cooldown + " more seconds.");
            }
        } else if (handler instanceof Dragonborn){// && (player.getItemInHand()==null || player.getItemInHand().equals(ItemStack.empty()) || player.getItemInHand().getType().equals(Material.AIR) || player.getItemInHand().getAmount()==0)) {
            Ability ability = handler.getAbilities()[0];
            if (cooldown<=0 || ability.getCooldown() == 0) {
                if (level>=ability.getLevel()) {
                    if (ability.trigger(this,player,level)) {
                        if (ability.getCooldown()!=0) {
                            cooldown = ability.getCooldown();
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + ability.failureMessage());
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "That ability requires you to be level " + ability.getLevel());
                }
            } else {
                player.sendMessage(ChatColor.RED + "Your abilities are on cooldown for " + cooldown + " more seconds.");
            }
        }
    }
    //Renders the menu
    String dmn = Volands.getSystem().translate("Dragon Mount");

    public void rename(String newname) {
        name=newname;
    }

    public Player getPlayer() {
        return player;
    }
    public void renderMenu() {
        if (currentAbility instanceof ToggleableAbility) {
            if (currentAbility instanceof TriggerClimb) {
                if (currentAbility.getLevel()>level) {
                    player.sendTitle(ChatColor.RED + currentAbility.getName(), ChatColor.DARK_RED + "will unlock at level " + currentAbility.getLevel(),0,40,5);
                } else {
                    if (((Neko)handler).getCanClimb()) {
                        player.sendTitle(ChatColor.RED + currentAbility.getName(), ChatColor.WHITE + "Crouch + Punch to Disable",0,40,5);
                    } else {
                        player.sendTitle(ChatColor.RED + currentAbility.getName(), ChatColor.WHITE + "Crouch + Punch to Enable",0,40,5);
                    }
                }
            } else {
                if (currentAbility.getLevel()>level) {
                    player.sendTitle(ChatColor.RED + currentAbility.getName(), ChatColor.DARK_RED + "will unlock at level " + currentAbility.getLevel(),0,40,5);
                } else {
                    if (handler.getToggledAbility()) {
                        player.sendTitle(ChatColor.RED + currentAbility.getName(), ChatColor.WHITE + "Crouch + Punch to Disable",0,40,5);
                    } else {
                        player.sendTitle(ChatColor.RED + currentAbility.getName(), ChatColor.WHITE + "Crouch + Punch to Enable",0,40,5);
                    }
                }
            }
        } else {
            if (currentAbility.getLevel()>level) {
                player.sendTitle(ChatColor.RED + currentAbility.getName(), ChatColor.DARK_RED + "will unlock at level " + currentAbility.getLevel(),0,40,5);
            } else {
                if (cooldown>0) {
                    player.sendTitle(ChatColor.RED + currentAbility.getName(), ChatColor.WHITE + "Cooldown: " + cooldown + " seconds",0,40,5);
                } else {
                    player.sendTitle(ChatColor.RED + currentAbility.getName(), ChatColor.WHITE + "Crouch + Punch to Activate",0,40,5);
                }
            }
        }
    }

    double oldXP;
    ExpType exType;
    public void addXP(ExpType type, double amount) {
        xp+=amount*handler.getMultiplier(type);
        level=((int)(xp/5000.0));
        exType=type;
    }

    public void sendRaceMessage(String message) {
        if (!spamToggled) {
            player.sendMessage(message);
        }
    }

    SkillTree tree;
    public SkillTree getTree() {
        return tree;
    }
}
