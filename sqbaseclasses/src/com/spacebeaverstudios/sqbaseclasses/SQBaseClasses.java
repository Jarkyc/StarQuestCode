package com.spacebeaverstudios.sqbaseclasses;

import com.spacebeaverstudios.sqbaseclasses.listeners.InventoryListener;
import org.bukkit.plugin.java.JavaPlugin;

public class SQBaseClasses extends JavaPlugin {
    // TODO: contraband system

    private static SQBaseClasses instance;

    public static SQBaseClasses getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
    }
}
