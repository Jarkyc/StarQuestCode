package com.spacebeaverstudios.sqsmoothcraft.GUIs;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.gui.ListGUI;
import com.spacebeaverstudios.sqsmoothcraft.GUIs.GUIFunctions.DetectFunction;
import com.spacebeaverstudios.sqsmoothcraft.Objects.ShipClass;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;


import java.util.Arrays;
import java.util.List;

public class ClassSelectionGUI extends ListGUI {

    private Location loc;

    public ClassSelectionGUI(Location location) {
        super(ChatColor.BLUE + "Select your ship class", 3);
        this.loc = location;
    }


    @Override
    public List<Object> getObjectList() {
        return Arrays.asList(ShipClass.values());
    }

    @Override
    public GUIItem getObjectItem(Object o) {
        ShipClass clazz = (ShipClass) o;
        GUIItem item = new GUIItem(clazz.name(), ChatColor.GRAY + "Max Size: " + clazz.getMaxSize(), Material.PAPER, new DetectFunction(clazz, loc));
        return item;
    }
}
