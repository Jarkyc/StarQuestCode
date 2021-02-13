package com.spacebeaverstudios.sqcore.utils.discord;

import com.spacebeaverstudios.sqcore.SQCore;
import org.bukkit.Bukkit;

public class DiscordUtils {
    public static String tag(String name) {
        return "<@" + SQCore.getInstance().getConfig().getString("discord-tags." + name.toLowerCase()) + ">";
    }

    public static void reportError(String message) {
        DiscordBugReportEvent event = new DiscordBugReportEvent(message);
        Bukkit.getPluginManager().callEvent(event);
    }
}
