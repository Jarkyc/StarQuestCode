package com.spacebeaverstudios.sqcore.objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.util.HashMap;

public class Template implements Serializable {

    public String name;
    private Vector origin;
    private HashMap<Vector, TemplateBlock> blocks = new HashMap<>();

    public Template(String name, Location origin){
        this.name = name;
        this.origin = new Vector(origin.getBlockX(), origin.getBlockY(), origin.getBlockZ());
    }

    public void addBlock(Location loc, TemplateBlock block){
        blocks.put(new Vector(loc.getBlockX() - origin.getBlockX(), loc.getBlockY() - origin.getBlockY(), loc.getBlockZ() - origin.getBlockZ()), block);
    }

    public void paste(Location loc){
        for(Vector vec : blocks.keySet()){
            Location pasteLoc = new Location(loc.getWorld(), loc.getX() + vec.getX(), loc.getY() + vec.getY(), loc.getZ() + vec.getZ());
            blocks.get(vec).paste(pasteLoc);
        }
    }

    public void pasteAir(Location loc){
        for(Vector vec : blocks.keySet()){
            Location pasteLoc = new Location(loc.getWorld(), loc.getX() - vec.getX(), loc.getY() - vec.getY(), loc.getZ() - vec.getZ());
            pasteLoc.getBlock().setType(Material.AIR);
        }
    }


}
