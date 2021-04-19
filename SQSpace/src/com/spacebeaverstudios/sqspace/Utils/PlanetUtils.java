package com.spacebeaverstudios.sqspace.Utils;

import com.spacebeaverstudios.sqspace.Objects.Planet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;

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

            DynmapUtils.createMarker("sun", "system_" + systemName.toLowerCase(), systemName, systemName, "systems", new Location(Bukkit.getWorld("world"), systemX, 100, systemZ));

            for(String planetName : systemSection.getConfigurationSection(systemName + ".planets").getKeys(false)){
                loadPlanet(planetName, systemName, new Location(Bukkit.getWorld("world"), systemX, 100, systemZ), systemSection.getConfigurationSection(systemName + ".planets." + planetName));
            }
        }

    }

    public static void loadPlanet(String name, String system, Location systemOrigin, ConfigurationSection section){
        DynmapUtils.createCircleMarker(name, systemOrigin, section.getInt("orbitRadius"));
        planets.add(new Planet(name, systemOrigin, section.getInt("radius"), section.getInt("orbitRadius"), system, section.getInt("storedAngle") + 20));

        int radius = section.getInt("orbitRadius");
        int angle = section.getInt("storedAngle") + 20;

        double x = radius * Math.cos(angle);
        double z = radius * Math.sin(angle);

        x += systemOrigin.getX();
        z += systemOrigin.getZ();

        DynmapUtils.createMarker("world", "planet_name" + name.toLowerCase(), name, name, "planets", new Location(systemOrigin.getWorld(), Math.floor(x), 100, Math.floor(z)));

    }


}
