package com.spacebeaverstudios.sqspace.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.*;

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

    public static void createCircleMarker(String name, Location location, int radius, String color){
        MarkerAPI api = dapi.getMarkerAPI();
        MarkerSet planets = api.getMarkerSet("planets");

        if(planets == null){
            planets = api.createMarkerSet("planets", "Planets", null, false);
        }

        CircleMarker marker = planets.findCircleMarker(name);

        if(marker != null){
            marker.deleteMarker();
            marker = null;
        }

        marker = planets.createCircleMarker(name, name, false, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), radius, radius, false);

        marker.setFillStyle(0, 0);
        marker.setLineStyle(3, 1, Integer.parseInt(color, 16));
    }

    public static void createLineMarker(String markerSetName, String lineLabel, String lineName, World world, double[] xCoords, double[] yCoords, double[] zCoords, String description, int lineWidth, double lineOpacity, String lineColorHex){
        MarkerAPI markerAPI = dapi.getMarkerAPI();
        MarkerSet markerSet = markerAPI.getMarkerSet(markerSetName);

        if (markerSet == null) {

            markerSet = markerAPI.createMarkerSet(markerSetName, markerSetName, null, false);

        }

        PolyLineMarker polyLineMarker = markerSet.findPolyLineMarker(lineLabel);

        if (polyLineMarker != null) {

            polyLineMarker.deleteMarker();
            polyLineMarker = null;

        }

        polyLineMarker = markerSet.createPolyLineMarker(lineLabel, lineName, false, world.getName(), xCoords, yCoords, zCoords, false);
        polyLineMarker.setDescription(description);
        polyLineMarker.setLineStyle(lineWidth, lineOpacity, Integer.parseInt(lineColorHex, 16));

    }


}
