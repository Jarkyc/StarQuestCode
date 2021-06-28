package com.spacebeaverstudios.sqstaffutils.listeners;

import com.spacebeaverstudios.sqstaffutils.objects.infraction.Kick;
import net.essentialsx.api.v2.events.UserKickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class KickListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onUserKick(UserKickEvent event) {
        new Kick(event);
    }
}
