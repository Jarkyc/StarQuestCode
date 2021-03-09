package com.spacebeaverstudios.sqtech;

import com.spacebeaverstudios.sqtech.guis.MachineInventoryGUI;
import com.spacebeaverstudios.sqtech.listeners.*;
import com.spacebeaverstudios.sqtech.objects.CanCheckIntact;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import com.spacebeaverstudios.sqtech.objects.machines.SmelterMachine;
import com.spacebeaverstudios.sqtech.objects.pipes.ItemPipe;
import com.spacebeaverstudios.sqtech.objects.pipes.PowerPipe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class SQTech extends JavaPlugin {
	private static SQTech instance;

    public static SQTech getInstance() {
        return instance;
    }

    private Integer checkIntactsIndex = 0;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new CraftingListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new SignListener(), this);

        SmelterMachine.initializeRecipes();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Machine machine : Machine.getMachines()) {
                Location sign = machine.getSign();
                if (sign.getWorld().isChunkLoaded((int) Math.floor(sign.getBlockX()/16f), (int) Math.floor(sign.getBlockZ()/16f))) {
                    machine.tick();
                }
            }
            MachineInventoryGUI.refreshAll();

            // check 3 machines/pipes to see if intact, just in case the listeners missed something
            ArrayList<CanCheckIntact> checkIntacts = new ArrayList<>();
            checkIntacts.addAll(Machine.getMachines());
            checkIntacts.addAll(ItemPipe.getAllPipes());
            checkIntacts.addAll(PowerPipe.getAllPipes());
            if (checkIntacts.size() != 0) {
                for (int i = 0; i < 3; i++) {
                    if (checkIntactsIndex >= checkIntacts.size() - 1) {
                        checkIntactsIndex = 0;
                    } else {
                        checkIntactsIndex++;
                    }
                    checkIntacts.get(checkIntactsIndex).checkIntact();
                }
            }
        }, 20, 20);
    }
}
