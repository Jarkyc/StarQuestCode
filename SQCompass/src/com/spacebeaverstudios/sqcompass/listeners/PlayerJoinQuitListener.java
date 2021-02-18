package com.spacebeaverstudios.sqcompass.listeners;

import com.spacebeaverstudios.sqcompass.utils.CompassUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        CompassUtils.playerJoin(event.getPlayer());
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        CompassUtils.playerLeave(event.getPlayer());
    }
}
