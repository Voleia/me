package me.voleia.volands.Races;

import io.papermc.paper.event.player.AsyncChatCommandDecorateEvent;
import me.voleia.volands.Abilities.Ability;
import me.voleia.volands.Enums.ExpType;
import me.voleia.volands.OC;
import me.voleia.volands.SkillTree.Skill;
import me.voleia.volands.SkillTree.SkillTree;
import me.voleia.volands.Volands;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Shadowkin implements RaceClass {

    PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false,false,true);
    int sneakTime=0;
    Ability[] abilities = new Ability[2];
    Player cur;

    public Shadowkin() {
        abilities[1]=Volands.getSystem().getAbilities()[6];
        abilities[0]=Volands.getSystem().getAbilities()[2];
        multipliers.put(ExpType.COMBAT, 1.7);
        multipliers.put(ExpType.FARMING, 0.6);
        multipliers.put(ExpType.MINING, 0.8);
        multipliers.put(ExpType.HUNTING,0.8);
    }

    HashMap<ExpType, Double> multipliers = new HashMap<>();

    public double getMultiplier(ExpType type) {
        return multipliers.get(type);
    }

    public List<String> getInfo() {
        return Arrays.asList(new String[]{"TBA"});
    }

    public void loadTree(SkillTree tree) {

    }

    public void updateTree(Skill skill, boolean value) {

    }

    public void onTick(Player p, int level) {
        cur = p;
        if (level>=2) {
            if (p.isSneaking()) {
                sneakTime+=1;
                if (sneakTime>3 && p.getWorld().getBlockAt(p.getLocation()).getLightLevel()<=7) {
                    p.addPotionEffect(invisibility);
                    if (sneakTime==4) {
                        p.sendMessage(ChatColor.GOLD + "You are now invisible!");
                    }
                } else {
                    if (removeArtificialPotion(p,PotionEffectType.INVISIBILITY)) {
                        p.sendMessage(ChatColor.RED + "Your invisibility has turned off!");
                    }
                }
            } else {
                if (removeArtificialPotion(p,PotionEffectType.INVISIBILITY)) {
                    p.sendMessage(ChatColor.RED + "Your invisibility has turned off!");
                }
            }
        }
    }

    boolean removeArtificialPotion(Player p, PotionEffectType pot) {
        if (p.hasPotionEffect(pot) && p.getPotionEffect(pot).getDuration()>500000) {
            p.removePotionEffect(pot);
            return true;
        }
        return false;
    }

    @Override
    public boolean getToggledAbility() {
        return cur.hasPotionEffect(PotionEffectType.NIGHT_VISION);
    }

    public Ability[] getAbilities() {
        return abilities;
    }

    String name = Volands.getSystem().translate("Shadowkin");

    public String getDisplayName() {
        return name;
    }

    public void setFlag(String flag, Object value) {
        if (flag.equals("sneakingTime")) {
            sneakTime=(int)value;
        }
    }

    public Object getFlag(String flag) {
        if (flag.equals("sneakingTime")) {
            return sneakTime;
        }
        return null;
    }
}
