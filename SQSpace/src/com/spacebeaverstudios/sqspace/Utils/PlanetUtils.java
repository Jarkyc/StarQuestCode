package com.spacebeaverstudios.sqspace.Utils;

import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.spacebeaverstudios.sqcore.objects.template.Template;
import com.spacebeaverstudios.sqcore.objects.template.TemplateBlock;
import com.spacebeaverstudios.sqspace.Generators.VoidGenerator;
import com.spacebeaverstudios.sqspace.Objects.Planet;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

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

            Location center = new Location(Bukkit.getWorld("space"), systemX, 100, systemZ);

            DynmapUtils.createMarker("sun", "system_" + systemName.toLowerCase(), systemName, systemName, "systems", center);

            Template template = Template.getTemplates().get("star");
            template.paste(center);

            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Bukkit.getWorld("space")));

            regionManager.removeRegion("star_" + systemName.toLowerCase());

            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int minZ = Integer.MAX_VALUE;

            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int maxZ = Integer.MIN_VALUE;

            for(TemplateBlock block : template.getBlocks()) {
                Vector vector = block.getVector();

                minX = Math.min(minX, vector.getBlockX());
                minY = Math.min(minY, vector.getBlockY());
                minZ = Math.min(minZ, vector.getBlockZ());

                maxX = Math.max(maxX, vector.getBlockX());
                maxY = Math.max(maxY, vector.getBlockY());
                maxZ = Math.max(maxZ, vector.getBlockZ());

            }

            ProtectedCuboidRegion protectedRegion = new ProtectedCuboidRegion(
                    "star_" + systemName.toLowerCase(),
                    BlockVector3.at(systemX + minX, 100 + minY, systemZ + minZ),
                    BlockVector3.at(systemX + maxX, 100 + maxY, systemZ + maxZ)
            );

            regionManager.addRegion(protectedRegion);

            for(String planetName : systemSection.getConfigurationSection(systemName + ".planets").getKeys(false)){
                loadPlanet(planetName, systemName, new Location(Bukkit.getWorld("space"), systemX, 100, systemZ), systemSection.getConfigurationSection(systemName + ".planets." + planetName));
            }
        }

    }

    public static void loadPlanet(String name, String system, Location systemOrigin, ConfigurationSection section){
        DynmapUtils.createCircleMarker(name + "'s Orbit", systemOrigin, section.getInt("orbitRadius"), "ffff00");

        int newAngle = section.getInt("storedAngle") - 20;

        //Making sure the angle never exceeds 360 for aesthetic purposes
        if(newAngle <= -360){
            newAngle += 360;
        }

        planets.add(new Planet(name, systemOrigin, section.getInt("radius"), section.getInt("orbitRadius"), system, newAngle));

        Template planetBody;

        if(Template.getTemplates().get(name) == null){
            planetBody = Template.getTemplates().get("blank_planet");
        } else {
            planetBody = Template.getTemplates().get(name);
        }

        int radius = section.getInt("orbitRadius");
        int angle = section.getInt("storedAngle");

        double currentX = Math.floor(radius * Math.cos(angle));
        double currentZ = Math.floor(radius * Math.sin(angle));

        currentX += systemOrigin.getX();
        currentZ += systemOrigin.getZ();

        planetBody.pasteAir(new Location(systemOrigin.getWorld(), currentX, 100, currentZ));

        double x = Math.floor(radius * Math.cos(newAngle));
        double z = Math.floor(radius * Math.sin(newAngle));

        x += systemOrigin.getX();
        z += systemOrigin.getZ();

        Location newLoc = new Location(systemOrigin.getWorld(), x, 100, z);

        DynmapUtils.createMarker("world", "planet_" + name + name.toLowerCase(), name, name, "planets", newLoc);
        planetBody.paste(newLoc);

        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Bukkit.getWorld("space")));

        regionManager.removeRegion("planet_" + name.toLowerCase());

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;

        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for(TemplateBlock block : planetBody.getBlocks()) {
            Vector vector = block.getVector();

            minX = Math.min(minX, vector.getBlockX());
            minY = Math.min(minY, vector.getBlockY());
            minZ = Math.min(minZ, vector.getBlockZ());

            maxX = Math.max(maxX, vector.getBlockX());
            maxY = Math.max(maxY, vector.getBlockY());
            maxZ = Math.max(maxZ, vector.getBlockZ());

        }

        ProtectedCuboidRegion protectedRegion = new ProtectedCuboidRegion(
                "planet_" + name.toLowerCase(),
                BlockVector3.at(x + minX, 100 + minY, z + minZ),
                BlockVector3.at(x + maxX, 100 + maxY, z + maxZ)
        );

        regionManager.addRegion(protectedRegion);

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
