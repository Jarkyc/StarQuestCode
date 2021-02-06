package com.spacebeaverstudios.sqtech;

import com.spacebeaverstudios.sqtech.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public class SQTech extends JavaPlugin {
    private static SQTech instance;

    public static SQTech getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new SignListener(), this);
    }
}
