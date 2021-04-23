package com.spacebeaverstudios.sqcompass.gui;

import com.spacebeaverstudios.sqcompass.gui.functions.ToggleCommonMarkerGUIFunction;
import com.spacebeaverstudios.sqcompass.objects.StaticLocationMarker;
import com.spacebeaverstudios.sqcompass.utils.CompassUtils;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.gui.ListGUI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class CommonMarkersGUI extends ListGUI {
    public CommonMarkersGUI() {
        super("Common Compass Markers", 4);
    }

    public List<Object> getObjectList() {
        return new ArrayList<>(CompassUtils.getCommonMarkers().keySet());
    }

    public GUIItem getObjectItem(Object obj) {
        String markerName = (String) obj;
        StaticLocationMarker marker = CompassUtils.getCommonMarkers().get(markerName);
        Location location = marker.getLocation();
        boolean enabled = CompassUtils.getCommonMarkersEnabled().get(getPlayer().getUniqueId()).contains(markerName);
        return new GUIItem((enabled ? ChatColor.GREEN : ChatColor.RED) + markerName,
                ChatColor.GOLD + "Marker: " + marker.getColor() + marker.getMarker() + "\n " + ChatColor.GOLD + "Location: "
                        + ChatColor.AQUA + "(" + location.getWorld().getName() + ") x: " + location.getBlockX() + ", z: " + location.getBlockZ(),
                (enabled ? Material.GREEN_TERRACOTTA : Material.RED_TERRACOTTA),
                new ToggleCommonMarkerGUIFunction(markerName));
    }
}
