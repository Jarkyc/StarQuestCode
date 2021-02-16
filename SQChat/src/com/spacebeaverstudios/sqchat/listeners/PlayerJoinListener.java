package com.spacebeaverstudios.sqchat.listeners;

import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ChatUtils.getPlayerChannels().put(event.getPlayer(), ChatUtils.Channel.GLOBAL);
    }
}
