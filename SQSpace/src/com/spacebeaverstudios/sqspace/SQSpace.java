package com.spacebeaverstudios.sqspace;

import com.spacebeaverstudios.sqspace.Generators.SpaceGenerator;
import com.spacebeaverstudios.sqspace.Listeners.PlayerEvents;
import com.spacebeaverstudios.sqspace.Objects.Planet;
import com.spacebeaverstudios.sqspace.Tasks.WorldBorderTask;
import com.spacebeaverstudios.sqspace.Utils.DynmapUtils;
import com.spacebeaverstudios.sqspace.Utils.PlanetUtils;
import com.spacebeaverstudios.sqspace.Utils.WorldBorderUtils;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
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
        loadSpace();
        loadSystems();

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new WorldBorderTask(), 0l, 1l);
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

    public void loadSpace(){
        WorldCreator worldCreator = new WorldCreator("space");
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.generator(new SpaceGenerator());
        worldCreator.generateStructures(false);

        if(this.getServer().getWorld("space") != null){
            this.getServer().unloadWorld("space", false);
        }

        World space = this.getServer().createWorld(worldCreator);
        space.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        space.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        space.setGameRule(GameRule.DO_FIRE_TICK, false);
        space.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        space.setKeepSpawnInMemory(false);
        space.setTime(18000);

        DynmapUtils.createLineMarker("Worldborders", "Space Worldborder", "Space Worldborder", space, new double[] {0, 10000, 10000, 0, 0}, new double[] {100, 100, 100, 100, 100}, new double[] {0, 0, 10000, 10000, 0}, "", 3, 1, "ff0000");
        WorldBorderUtils.setWorldBorder(new WorldBorderUtils.SquareWorldBorder(space, 0, 0, 10000, 10000));

    }

}
