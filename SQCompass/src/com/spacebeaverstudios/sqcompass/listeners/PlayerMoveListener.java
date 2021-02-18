package com.spacebeaverstudios.sqcompass.listeners;

import com.spacebeaverstudios.sqcompass.utils.CompassUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        CompassUtils.render(event.getPlayer());
    }
}
