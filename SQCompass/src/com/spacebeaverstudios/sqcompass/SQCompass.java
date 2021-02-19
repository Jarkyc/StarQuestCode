package com.spacebeaverstudios.sqcompass;

import com.spacebeaverstudios.sqcompass.commands.CompassCmd;
import com.spacebeaverstudios.sqcompass.listeners.*;
import com.spacebeaverstudios.sqcompass.utils.CompassUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SQCompass extends JavaPlugin {
    private static SQCompass instance;

    public static SQCompass getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        if (!(new File(getDataFolder().getAbsolutePath() + "/config.yml")).exists()) this.saveDefaultConfig();
        CompassUtils.loadCommonMarkers();
        CompassUtils.loadShowCompassHashMap();
        CompassUtils.loadCustomMarkers();
        CompassUtils.loadCommonMarkersEnabled();
        this.reloadConfig();

        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new WorldSaveListener(), this);

        CompassCmd compassCmd = new CompassCmd();
        getCommand("compass").setExecutor(compassCmd);
        getCommand("compass").setTabCompleter(compassCmd);
    }

    public void onDisable() {
        CompassUtils.saveShowCompassHashMap();
        CompassUtils.saveCustomMarkers();
        CompassUtils.saveCommonMarkersEnabled();
    }
}
