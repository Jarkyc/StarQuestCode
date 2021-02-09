package com.spacebeaverstudios.sqsmoothcraft.Objects.Data;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Modules.Module;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class SolidShipData implements Serializable {

    private ArrayList<Module> modules;
    private Location location;
    private Player owner;

    public SolidShipData(Location location, ArrayList<Module> modules, Player owner){
        this.location = location;
        this.modules = modules;
        this.owner = owner;
    }

}
