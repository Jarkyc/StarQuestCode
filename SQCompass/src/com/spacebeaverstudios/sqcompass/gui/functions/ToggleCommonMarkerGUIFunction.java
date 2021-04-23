package com.spacebeaverstudios.sqcompass.gui.functions;

import com.spacebeaverstudios.sqcompass.objects.StaticLocationMarker;
import com.spacebeaverstudios.sqcompass.utils.CompassUtils;
import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import org.bukkit.entity.Player;

public class ToggleCommonMarkerGUIFunction extends GUIFunction {
    private final String name;

    public ToggleCommonMarkerGUIFunction(String name) {
        this.name = name;
    }

    public void run(Player player) {
        StaticLocationMarker marker = CompassUtils.getCommonMarkers().get(name);
        if (CompassUtils.getMarkers().get(player).contains(marker)) {
            CompassUtils.getMarkers().get(player).remove(marker);
            CompassUtils.getCommonMarkersEnabled().get(player.getUniqueId()).remove(name);
        } else {
            CompassUtils.getMarkers().get(player).add(marker);
            CompassUtils.getCommonMarkersEnabled().get(player.getUniqueId()).add(name);
        }
        CompassUtils.render(player);
        GUI.getGuis().get(player).refreshInventory();
    }
}
