package com.spacebeaverstudios.sqstaffutils.objects;

import com.earth2me.essentials.IEssentials;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import com.spacebeaverstudios.sqstaffutils.SQStaffUtils;
import com.spacebeaverstudios.sqstaffutils.objects.infraction.Ban;
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
import java.util.regex.Pattern;

public class LogListener extends AbstractAppender {
    private static final IEssentials iEssentials;
    private static final PatternLayout PATTERN_LAYOUT;

    static {
        iEssentials = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");

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
        String message = ChatColor.stripColor(event.getMessage().getFormattedMessage());
        if (message.matches("Player (.*?) banned (.*?) for:")) {
            logBan(Pattern.compile("kicked (.*?) for").matcher(message).group(1));
        }
    }

    private void logBan(String bannedName) {
        SQStaffUtils.getInstance().getLogger().info("bannedNamed == " + bannedName);
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
}
