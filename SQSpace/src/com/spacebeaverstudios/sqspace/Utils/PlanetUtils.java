package com.spacebeaverstudios.sqspace.Utils;

import com.spacebeaverstudios.sqspace.Generators.VoidGenerator;
import com.spacebeaverstudios.sqspace.Objects.Planet;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class PlanetUtils {

    public static ArrayList<Planet> planets = new ArrayList<>();

    public static void load(File configFile){
        FileConfiguration config = new YamlConfiguration();
        try{
            config.load(configFile);
        } catch(Exception e){
            System.out.println("ERROR LOADING PLANETS. STACK TRACE BELOW");
            e.printStackTrace();
            return;
        }

        ConfigurationSection systemSection = config.getConfigurationSection("systems");

        for(String systemName : systemSection.getKeys(false)){
            int systemX = systemSection.getInt(systemName + ".x");
            int systemZ = systemSection.getInt(systemName + ".z");

            DynmapUtils.createMarker("sun", "system_" + systemName.toLowerCase(), systemName, systemName, "systems", new Location(Bukkit.getWorld("space"), systemX, 100, systemZ));

            for(String planetName : systemSection.getConfigurationSection(systemName + ".planets").getKeys(false)){
                loadPlanet(planetName, systemName, new Location(Bukkit.getWorld("space"), systemX, 100, systemZ), systemSection.getConfigurationSection(systemName + ".planets." + planetName));
            }
        }

    }

    public static void loadPlanet(String name, String system, Location systemOrigin, ConfigurationSection section){
        DynmapUtils.createCircleMarker(name + "'s Orbit", systemOrigin, section.getInt("orbitRadius"), "ffff00");
        planets.add(new Planet(name, systemOrigin, section.getInt("radius"), section.getInt("orbitRadius"), system, section.getInt("storedAngle") - 20));

        int radius = section.getInt("orbitRadius");
        int angle = section.getInt("storedAngle") - 20;

        int x = (int) (radius * Math.cos(angle));
        int z = (int) (radius * Math.sin(angle));

        x += systemOrigin.getX();
        z += systemOrigin.getZ();

        DynmapUtils.createMarker("world", "planet_" + name + name.toLowerCase(), name, name, "planets", new Location(systemOrigin.getWorld(), Math.floor(x), 100, Math.floor(z)));

        WorldCreator worldCreator = new WorldCreator(name.toLowerCase());
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.generator(new VoidGenerator());
        worldCreator.generateStructures(false);

        if(Bukkit.getWorld(name.toLowerCase()) != null){
            Bukkit.unloadWorld(name.toLowerCase(), false);
        }

        World world = Bukkit.createWorld(worldCreator);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.DO_FIRE_TICK, false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);

        world.setKeepSpawnInMemory(false);

        WorldBorderUtils.setWorldBorder(new WorldBorderUtils.CircularWorldBorder(world, 0, 0, section.getInt("radius")));
        DynmapUtils.createCircleMarker(name + " world border", new Location(world, 0, 100, 0), section.getInt("radius"), "ff0000");

    }


}
