package com.spacebeaverstudios.sqstaffutils.listeners;

import com.spacebeaverstudios.sqstaffutils.objects.infraction.Infraction;
import com.spacebeaverstudios.sqstaffutils.objects.infraction.Warning;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Infraction infraction : Infraction.infractionsToPlayer(event.getPlayer().getUniqueId())) {
            if (infraction instanceof Warning) {
                Warning warning = (Warning) infraction;
                if (!warning.delivered) {
                    event.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.UNDERLINE + "While you were away, "
                            + warning.sender.getName() + " warned you:" + ChatColor.WHITE + " " + warning.message);
                    warning.delivered = true;
                }
            }
        }
    }
}
