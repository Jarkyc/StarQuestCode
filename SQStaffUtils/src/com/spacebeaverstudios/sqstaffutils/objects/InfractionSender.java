package com.spacebeaverstudios.sqstaffutils.objects;

import org.bukkit.Bukkit;

import java.util.UUID;

public class InfractionSender {
    // do this instead of just UUID because of InfractionSenderArgument#parse
    public final UUID uuid;

    public InfractionSender(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return uuid == null ? "Console" : Bukkit.getOfflinePlayer(uuid).getName();
    }
}
