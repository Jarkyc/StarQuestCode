package com.spacebeaverstudios.sqsmoothcraft;

import com.spacebeaverstudios.sqsmoothcraft.Commands.ShipCmd;
import com.spacebeaverstudios.sqsmoothcraft.Commands.TestCmd;
import com.spacebeaverstudios.sqsmoothcraft.Listeners.PlayerListeners;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Data.SolidShipData;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.*;
import java.util.ArrayList;

public class SQSmoothcraft extends JavaPlugin {

    public ArrayList<Ship> allShips = new ArrayList<>();
    public ArrayList<SolidShipData> solidShips = new ArrayList<>();
    public static SQSmoothcraft instance;
    public ArrayList<Material> shipBlocks;


    public void onEnable(){
        System.out.println("SQSmoothcraft enabled");

        instance = this;
        loadConfig();

        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        getCommand("ship").setExecutor(new ShipCmd());

        getCommand("test").setExecutor(new TestCmd());

        loadData();

        tick();

    }

    public void onDisable(){
        while(!allShips.isEmpty()){
            Ship ship = allShips.get(0);
            ship.buildSolid();
        }
        saveData();
    }

    public void saveData() {

        try {
            FileOutputStream fos = new FileOutputStream("data.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.solidShips);
            oos.close();
            fos.close();
        } catch (IOException i){
            i.printStackTrace();
        }
     }


    public void loadData(){
        try{
            FileInputStream fis = new FileInputStream("data.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.solidShips = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
        } catch (ClassNotFoundException i){
            i.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadConfig(){

        File configFile = new File(getDataFolder().getAbsolutePath() + "/config.yml");

        if(!configFile.exists())
            this.saveDefaultConfig();

        this.reloadConfig();

        ArrayList<Material> mats = new ArrayList<>();

        for(String string : getConfig().getStringList("blocks")) {
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
        }, 1L, 0);
    }


}
