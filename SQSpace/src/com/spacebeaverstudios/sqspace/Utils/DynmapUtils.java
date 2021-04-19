package com.spacebeaverstudios.sqspace.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.CircleMarker;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

public class DynmapUtils {

    public static DynmapAPI dapi = (DynmapAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");

    public static void createMarker(String markerImg, String name, String label, String description, String setName, Location location){
        MarkerAPI markerAPI = dapi.getMarkerAPI();
        MarkerSet set = markerAPI.getMarkerSet(setName);

        if(set == null){
            set = markerAPI.createMarkerSet(setName, setName, null, false);
        }

        Marker marker = set.findMarker(label);

        if(marker != null){
            marker.deleteMarker();
            marker = null;
        }

        marker = set.createMarker(name, label, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), markerAPI.getMarkerIcon(markerImg), false);
        marker.setDescription(description);

    }

    public static void createCircleMarker(String name, Location location, int radius){
        MarkerAPI api = dapi.getMarkerAPI();
        MarkerSet planets = api.getMarkerSet("planets");

        if(planets == null){
            planets = api.createMarkerSet("planets", "Planets", null, false);
        }

        CircleMarker marker = planets.findCircleMarker(name + "'s Orbit");

        if(marker != null){
            marker.deleteMarker();
            marker = null;
        }

        marker = planets.createCircleMarker(name + "'s Orbit", name + "'s Orbit", false, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), radius, radius, false);

        marker.setFillStyle(0, 0);
        marker.setLineStyle(3, 1, Integer.parseInt("ffff00", 16));
    }


}
