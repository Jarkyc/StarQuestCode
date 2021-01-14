package com.spacebeaverstudios.sqsmoothcraft;

import com.spacebeaverstudios.sqsmoothcraft.Commands.UnpilotCmd;
import com.spacebeaverstudios.sqsmoothcraft.Listeners.PlayerListeners;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;

public class SQSmoothcraft extends JavaPlugin {

    public ArrayList<Ship> allShips = new ArrayList<>();
    public static SQSmoothcraft instance;


    public void onEnable(){
        System.out.println("SQSmoothcraft enabled");

        instance = this;

        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        this.getCommand("unpilot").setExecutor(new UnpilotCmd());
        tick();

    }

    public void onDisable(){
        while(!allShips.isEmpty()){
            Ship ship = allShips.get(0);
            ship.buildSolid();
        }
    }

    public void tick(){
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for(Ship ship : allShips){
                    ship.onTick();
                }
            }
        }, 0L, 1);
    }


//TODO
// Find out why ships spawn facing south
// Support slabs
// Make ship building into block form not decrease in y axis. Check if blocks are in the way.
// Create command system - SQCore
// Ship movement
// Modules
// Setup ship block config



}
