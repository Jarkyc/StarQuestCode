package com.spacebeaverstudios.sqstaffutils.objects;

import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import com.spacebeaverstudios.sqstaffutils.SQStaffUtils;
import com.spacebeaverstudios.sqstaffutils.objects.infraction.Ban;
import com.spacebeaverstudios.sqstaffutils.objects.infraction.Infraction;
import com.spacebeaverstudios.sqstaffutils.objects.infraction.Unban;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LogListener extends AbstractAppender {
    private static final PatternLayout PATTERN_LAYOUT;

    static {
        // tbh I copy-pasted this from DiscordSRV so I don't really know why it works
        Method createLayoutMethod = null;
        for (Method method : PatternLayout.class.getMethods()) {
            if (method.getName().equals("createLayout")) {
                createLayoutMethod = method;
            }
        }
        if (createLayoutMethod == null) {
            SQStaffUtils.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                    + " Failed to reflectively find the Log4j createLayout method because createLayoutMethod == null.");
            PATTERN_LAYOUT = null;
        } else {
            Object[] args = new Object[createLayoutMethod.getParameterCount()];
            args[0] = "[%d{HH:mm:ss} %level]: %msg";
            if (args.length == 9) {
                // log4j 2.1
                args[5] = true;
                args[6] = true;
            }

            PatternLayout createdLayout = null;
            try {
                createdLayout = (PatternLayout) createLayoutMethod.invoke(null, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                SQStaffUtils.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                        + " Failed to reflectively find the Log4j createLayout method.");
                e.printStackTrace();
            }
            PATTERN_LAYOUT = createdLayout;
        }
    }

    public LogListener() {
        super("SQStaffUtils-LogReader", null, PATTERN_LAYOUT, true);
        Logger rootLogger = (Logger) LogManager.getRootLogger();
        rootLogger.addAppender(this);
    }

    public void append(LogEvent event) {
        String[] message = ChatColor.stripColor(event.getMessage().getFormattedMessage()).split(" ");
        if (!message[0].equals("Player")) {
            return; // prevent false positives from chat
        }
        if (message[2].equals("banned")) {
            logBan(message[3]);
        } else if (message[2].equals("temporarily") && message[3].equals("banned")) {
            logBan(message[4]);
        } else if (message[2].equals("unbanned")) {
            logUnban(message[1], message[3]);
        }
    }

    private void logBan(String bannedName) {
        BanEntry banEntry = null;

        for (final BanEntry banEnt : SQStaffUtils.getInstance().getServer().getBanList(BanList.Type.NAME).getBanEntries()) {
            if (banEnt.getTarget().equals(bannedName)) {
                banEntry = banEnt;
                break;
            }
        }

        if (banEntry == null) {
            SQStaffUtils.getInstance().getLogger().warning(DiscordUtils.tag("blankman") + " banEntry == null");
        } else {
            new Ban(banEntry);
        }
    }

    private void logUnban(String unbannerName, String unbannedName) {
        new Unban(Infraction.senderFromString(unbannerName), Bukkit.getPlayerUniqueId(unbannedName));
    }
}
