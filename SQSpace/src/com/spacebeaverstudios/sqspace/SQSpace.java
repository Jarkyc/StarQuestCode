package com.spacebeaverstudios.sqspace;

import com.spacebeaverstudios.sqspace.Listeners.PlayerEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SQSpace extends JavaPlugin {

    public static SQSpace instance;

    public void onEnable(){
        System.out.println("SQSpace enabled");
        instance = this;
        this.getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
    }

    public void onDisable(){
        System.out.println("SQSpace disabled");
    }

}
