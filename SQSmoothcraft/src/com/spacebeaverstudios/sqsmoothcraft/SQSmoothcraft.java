package com.spacebeaverstudios.sqsmoothcraft;

import com.spacebeaverstudios.sqsmoothcraft.Commands.UnpilotCmd;
import com.spacebeaverstudios.sqsmoothcraft.Listeners.PlayerListeners;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.ArrayList;

public class SQSmoothcraft extends JavaPlugin {

    public ArrayList<Ship> allShips = new ArrayList<>();
    public static SQSmoothcraft instance;
    public ArrayList<Material> shipBlocks;


    public void onEnable(){
        System.out.println("SQSmoothcraft enabled");

        instance = this;
        loadConfig();

        System.out.println();

        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        this.getCommand("unpilot").setExecutor(new UnpilotCmd());

        for(Material material : shipBlocks){
            System.out.println(material);
        }

        tick();

    }

    public void onDisable(){
        while(!allShips.isEmpty()){
            Ship ship = allShips.get(0);
            ship.buildSolid();
        }
    }

    public void loadConfig(){

        File configFile = new File(getDataFolder().getAbsolutePath() + "/config.yml");

        if(!configFile.exists())
            this.saveDefaultConfig();

        this.reloadConfig();

        ArrayList<Material> mats = new ArrayList<>();

        for(String string : getConfig().getStringList("blocks")){
            Material mat = Material.getMaterial(string.toUpperCase());
            mats.add(mat);
        }
        shipBlocks = mats;
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
// Check if blocks are in the way of building.
// Create command system - SQCore
// Ship movement
// Modules



}
