package com.spacebeaverstudios.sqsmoothcraft.Objects.Data;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Modules.Module;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class SolidShipData implements Serializable {

    public ArrayList<Module> modules;
    public int x;
    public int y;
    public int z;
    public String world;
    public String owner;

    public SolidShipData(Location location, ArrayList<Module> modules, String owner){
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.world = location.getWorld().getName();
        this.modules = modules;
        this.owner = owner;
    }

}
