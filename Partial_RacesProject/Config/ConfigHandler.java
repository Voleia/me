package me.voleia.volands.Config;

import com.sun.org.apache.xerces.internal.impl.dv.xs.AnyURIDV;
import jdk.internal.joptsimple.util.KeyValuePair;
import me.voleia.volands.Enums.Race;
import me.voleia.volands.OC;
import me.voleia.volands.SkillTree.SkillType;
import me.voleia.volands.Volands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConfigHandler {

    Volands system;
    File folder;

    public ConfigHandler() {
        system = Volands.getSystem();
        folder = system.getDataFolder();
        if (!new File(folder, "OCs").exists()) {
            new File(folder, "OCs").mkdir();
        }
    }

    public ArrayList<OC> loadOCs(Player player) {
        File file = new File(folder, "OCs/"+player.getUniqueId().toString()+"/handler.yml");
        FileConfiguration handler = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            return new ArrayList<OC>();
        }
        ArrayList<OC> ocs = new ArrayList<OC>();
        for (int i : handler.getIntegerList("OCs")) {
            File oc = new File(folder, "OCs/" + player.getUniqueId().toString() + "/" + i + ".yml");
            if (oc.exists()) {
                FileConfiguration ocConfig = YamlConfiguration.loadConfiguration(oc);
                OC newOC = new OC(Race.valueOf(ocConfig.getString("r")),player,ocConfig.getInt("l"),ocConfig.getDouble("x"),ocConfig.getString("n"),ocConfig.getInt("t"),ocConfig.getInt("i"),ocConfig.getString("prefix"),ocConfig.getString("suffix"),ocConfig.getBoolean("s"), ocConfig.getStringList("sk"));
                ocs.add(newOC);
            }
        }
        return ocs;
    }

    /*public ItemStack[] GrabOCContents(OC oc) {
        ArrayList<ItemStack> items = new ArrayList<>();
        File file = new File(folder, "OCs/" + oc.getUUID() + "/" + oc.getId() + ".yml");
        if (!file.exists()) {
            return null;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.getList("v")!=null) {
            for (Object obj : config.getList("v")) {
                if (obj==null) {
                    items.add(null);
                } else {
                    items.add((ItemStack) obj);
                }
            }
            return items.toArray(new ItemStack[items.size()]);
        }
        return null;
    }*/

    void m(String msg) {
        System.out.println(msg);
    }

    public String deleteOC(Player player, String ocName) {
        ArrayList<OC> OCList = loadOCs(player);
        m("Array of OC's gotten. Searching for ocName: " + ocName + " of player " + player.getName());
        if (!OCList.isEmpty()) {
            m("OCList not empty");
            for (OC o : OCList) {
                m("Checking OC: " + o.getName()+", "+o.getId());
                if (o.getName().equals(ocName)) {
                    m("Checked Name " + o.getName() + " equal to " + ocName);
                    int id = o.getId();
                    m("id is " + id);
                    if (id==-1) {
                        m("id does not exist");
                        return ChatColor.RED + "There was an issue deleting that OC! (ErrorCode: 0)";
                    }
                    m("boutta check file");
                    File file = new File(folder, "OCs/"+player.getUniqueId()+"/handler.yml");
                    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                    m("checked files");
                    List<Integer> OCs = config.getIntegerList("OCs");
                    m("found integer list");
                    for (int i : OCs) {
                        m("Item: " + i);
                    }
                    OCs.remove(Integer.valueOf(id));
                    m("removed item");
                    config.set("OCs",OCs);
                    m("set config");
                    try {
                        m("1");
                        system.saveConfig();
                        m("2");
                        config.save(file);
                        m("3");
                        system.saveConfig();
                    } catch (Exception ignored) {}
                    system.reloadConfig();
                    if (o.getId()==system.getOC(player).getId()) {
                        system.removeFromMap(player);
                        return "KICK";
                    } else {
                        m("ids differ");
                        return ChatColor.GREEN+"Deleted your OC";
                    }
                }
            }
            return ChatColor.RED + "That OC does not exist!";
        }
        return ChatColor.RED + "You have no OCs!";
        /*ArrayList<OC> OCs = loadOCs(player);
        if (!OCs.isEmpty()) {
            for (OC oc : OCs) {
                if (oc.getName().equals(ocName)) {
                    File handler = new File(folder, "OCs/"+oc.getUUID()+"/handler.yml");
                    FileConfiguration handlerConfig = YamlConfiguration.loadConfiguration(handler);
                    List<Integer> OCids = handlerConfig.getIntegerList("OCs");
                    for (int i = 0; i < OCids.size(); i+=1) {
                        if (OCids.get(i)==oc.getId()) {
                            OCids.remove(i);
                        }
                    }
                    //OCids.remove(Integer.valueOf(oc.getId()));
                    handlerConfig.set("OCs",OCids);
                    try {
                        handlerConfig.save(handler);
                        system.reloadConfig();
                    } catch (Exception e) {
                        return ChatColor.RED + "There was an issue deleting your OC! Contact an admin.";
                    }
                    File file = new File(folder, "OCs/"+oc.getUUID()+"/"+oc.getId()+".yml");
                    file.delete();
                    OC newOC = system.getOC(player);
                    if (newOC != null && oc.getId()==newOC.getId()) {
                        newOC.markForDeletion();
                        system.reloadConfig();
                        return "KICK";
                    }
                    system.reloadConfig();
                    return ChatColor.GREEN + "Successfully deleted your OC!";
                }
            }
            return ChatColor.RED + "That OC does not exist!";
        }
        return ChatColor.RED + "You have no OCs to delete!";*/
    }

    public void saveOC(OC oc) {
        File userHandler = new File(folder, "OCs/"+oc.getUUID()+"/handler.yml");
        if (!userHandler.exists()) {
            try {
                userHandler.createNewFile();
                FileConfiguration uH = YamlConfiguration.loadConfiguration(userHandler);
                uH.set("i",0);
                uH.save(userHandler);
                system.reloadConfig();
                userHandler = new File(folder, "OCs/"+oc.getUUID()+"/handler.yml");
            } catch (Exception e) {}
        }

        int id = oc.getId();
        File file;
        if (id==-1) {
            //oc needs to be created
            FileConfiguration uH = YamlConfiguration.loadConfiguration(userHandler);
            int curID = uH.getInt("i");
            file = new File(folder,"OCs/"+oc.getUUID()+"/"+curID+".yml");
            oc.setID(curID);
            try {
                file.createNewFile();
            } catch (Exception ignored) {}
            uH.set("i",curID+1);
            try {
                uH.save(userHandler);
                system.reloadConfig();
                userHandler = new File(folder, "OCs/"+oc.getUUID()+"/handler.yml");
            } catch (Exception ignored) {}
        } else {
            file = new File(folder, "OCs/"+oc.getUUID()+"/"+id+".yml");
            //oc already exists
        }
        /*if (!file.exists()) {
            try {
                file.createNewFile();
                uH.set("i",curId+1); //unique OC id per player
                uH.save(userHandler);
                system.reloadConfig();
            } catch (Exception e) {
                return;
            }
        }*/

        FileConfiguration ocConfig = YamlConfiguration.loadConfiguration(file);
        ocConfig.set("n",oc.getName());
        ocConfig.set("l",oc.getLevel());
        ocConfig.set("x",oc.getXP());
        ocConfig.set("r",oc.getRace().toString());
        ocConfig.set("t",oc.getTimePlayed());
        ocConfig.set("i",oc.getId());
        ocConfig.set("s",oc.getSpamToggled());
        ocConfig.set("prefix",oc.getPrefix());
        ocConfig.set("suffix",oc.getSuffix());
        ocConfig.set("ti",oc.getGrantedTitles());

        List<String> sk = new ArrayList<>();
        for (SkillType type : oc.getTree().getMap().keySet()) {
            if (oc.getTree().hasSkill(type)) {
                sk.add(type.toString());
            }
        }
        ocConfig.set("sk", sk);

        /*if (saveInventory) {
            List<Object> items = new ArrayList<>();
            items.addAll(Arrays.asList(oc.getPlayer().getInventory().getContents()));
            ocConfig.set("v",items);
        }*/
        //save inventory

        //unsave inventory

        FileConfiguration handler = YamlConfiguration.loadConfiguration(userHandler);
        List<Integer> OCs = handler.getIntegerList("OCs");
        if (!OCs.contains(oc.getId())) {
            OCs.add(oc.getId());
            handler.set("OCs",OCs);
        }

        try {
            ocConfig.save(file);
            handler.save(userHandler);
        } catch (Exception e) {}
        system.reloadConfig();

        //update config timeplayed
        //updateTimePlayed(oc.getPlayer());
    }

    /*public int getTimePlayed(Player player) {
        File handler = new File(folder, "OCs/"+player.getUniqueId().toString()+"/handler.yml");
        FileConfiguration handlerConfig = YamlConfiguration.loadConfiguration(handler);
        return handlerConfig.getInt("t");
    }*/

    public int getTimePlayed(Player player) {
        File[] OCs = new File(folder, "OCs/"+player.getUniqueId().toString()).listFiles();
        int totalTime = 0;
        for (File f : OCs) {
            if (!f.getName().contains("handler.yml")) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(f);
                totalTime+=config.getInt("t");
            }
        }
        return totalTime;
    }

    /*void updateTimePlayed(Player player) {
        File handler = new File(folder, "OCs/"+player.getUniqueId().toString()+"/handler.yml");
        FileConfiguration handlerConfig = YamlConfiguration.loadConfiguration(handler);
        File[] OCs = new File(folder, "OCs/"+player.getUniqueId().toString()).listFiles();
        int totalTime = 0;
        for (File f : OCs) {
            if (!f.getName().contains("handler.yml")) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(f);
                totalTime+=config.getInt("t");
            }
        }
        handlerConfig.set("t",totalTime);
        try {
            handlerConfig.save(handler);
            system.reloadConfig();
        } catch (Exception ignored) {}
    }*/

    public void setLastOC(OC oc) {
        File handlerFile = new File(folder, "OCs/"+oc.getUUID()+"/handler.yml");
        FileConfiguration handlerC = YamlConfiguration.loadConfiguration(handlerFile);
        handlerC.set("l",oc.getId());
        try { handlerC.save(handlerFile); } catch (Exception ignored) {}
        system.reloadConfig();
    }
}