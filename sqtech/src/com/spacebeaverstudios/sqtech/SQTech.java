package com.spacebeaverstudios.sqtech;

import com.spacebeaverstudios.sqtech.guis.MachineInventoryGUI;
import com.spacebeaverstudios.sqtech.listeners.*;
import com.spacebeaverstudios.sqtech.machines.Machine;
import com.spacebeaverstudios.sqtech.machines.SmelterMachine;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SQTech extends JavaPlugin {
	private static SQTech instance;

    public static SQTech getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new SignListener(), this);

        SmelterMachine.initializeRecipes();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            // TODO loop through all machiens & pipes, check a couple for intact every second
            for (Machine machine : Machine.getMachines()) machine.tick();
            MachineInventoryGUI.refreshAll();
        }, 20, 20);
    }
}
