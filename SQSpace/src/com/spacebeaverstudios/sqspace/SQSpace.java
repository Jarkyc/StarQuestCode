package com.spacebeaverstudios.sqspace;

import com.spacebeaverstudios.sqspace.Listeners.PlayerEvents;
import com.spacebeaverstudios.sqspace.Objects.Planet;
import com.spacebeaverstudios.sqspace.Utils.PlanetUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SQSpace extends JavaPlugin {

    public static SQSpace instance;

    public void onEnable(){
        System.out.println("SQSpace enabled");
        instance = this;
        this.getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        loadSystems();
    }

    public void onDisable(){
        for(Planet planet : PlanetUtils.planets){
            FileConfiguration config = new YamlConfiguration();
            try {
                File file = new File(getDataFolder().getAbsolutePath() + "/systems.yml");
                config.load(file);
                config.getConfigurationSection("systems." + planet.system + ".planets." + planet.name).set("storedAngle", planet.orbitDegree);
                config.save(file);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void loadSystems(){
        File configFile = new File(getDataFolder().getAbsolutePath() + "/systems.yml");

        if(!configFile.exists()){
            this.saveResource("systems.yml", false);
        }
        PlanetUtils.load(configFile);
    }

}
