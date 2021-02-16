package com.spacebeaverstudios.sqchat.listeners;

import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLogInOutListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ChatUtils.getPlayerChannels().put(event.getPlayer(), ChatUtils.Channel.GLOBAL);
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ChatUtils.getPlayerChannels().remove(event.getPlayer());
        ChatUtils.getReplies().remove(event.getPlayer());
        ChatUtils.getSpies().remove(event.getPlayer());
        ChatUtils.getSuperSpies().remove(event.getPlayer());
    }
}
