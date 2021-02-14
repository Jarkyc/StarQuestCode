package com.spacebeaverstudios.sqtech;

import com.spacebeaverstudios.sqtech.listeners.*;
import com.spacebeaverstudios.sqtech.machines.Machine;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SQTech extends JavaPlugin {
	// TODO: cap power generation by chunk?

    private static SQTech instance;

    public static SQTech getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new SignListener(), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, Machine::tickMachines, 1, 1);
    }
}
