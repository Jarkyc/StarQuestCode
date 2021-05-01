package com.spacebeaverstudios.sqcorporations.listeners;

import com.spacebeaverstudios.sqcorporations.objects.facility.Facility;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        for (Facility facility : Facility.getFacilities()) {
            if (facility.getBoundingBox().contains(event.getTo().getX(), event.getTo().getY(), event.getTo().getZ())
                    && !facility.getBoundingBox().contains(event.getFrom().getX(), event.getFrom().getY(), event.getFrom().getZ())) {
                facility.playerEnter(event.getPlayer());
            }
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Facility facility : Facility.getFacilities()) {
            Location location = event.getPlayer().getLocation();
            if (facility.getBoundingBox().contains(location.getX(), location.getY(), location.getZ())) {
                facility.playerEnter(event.getPlayer());
            }
        }
    }
}
