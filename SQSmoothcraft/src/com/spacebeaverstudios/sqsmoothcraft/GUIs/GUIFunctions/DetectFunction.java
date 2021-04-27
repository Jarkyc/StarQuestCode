package com.spacebeaverstudios.sqsmoothcraft.GUIs.GUIFunctions;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Pilot;
import com.spacebeaverstudios.sqsmoothcraft.Objects.ShipClass;
import com.spacebeaverstudios.sqsmoothcraft.Tasks.DetectionTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DetectFunction extends GUIFunction {

    private ShipClass clazz;
    private Location location;

    public DetectFunction(ShipClass clazz, Location location){
        this.clazz = clazz;
        this.location = location;
    }

    @Override
    public void run(Player player){
        new DetectionTask(location, new Pilot(player), clazz);
        player.closeInventory();
    }
}
