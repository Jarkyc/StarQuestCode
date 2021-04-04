package com.spacebeaverstudios.sqchat.utils;

import com.spacebeaverstudios.sqchat.SQChat;
import com.spacebeaverstudios.sqchat.events.JaneMessageEvent;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class JaneUtils {
    private static final ArrayList<UUID> bannedPlayers = new ArrayList<>();
    private static final HashMap<String, String> responses = new HashMap<>();
    private static final ArrayList<String> announcements = new ArrayList<>();
    private static int announcementsIndex = 0;

    public static ArrayList<UUID> getBannedPlayers() {
        return bannedPlayers;
    }

    public static void loadBannedPlayers() {
        try {
            File file = new File(SQChat.getInstance().getDataFolder().getAbsolutePath() + "/janebans.txt");
            file.getParentFile().mkdirs();
            if (!file.createNewFile()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    bannedPlayers.add(UUID.fromString(scanner.nextLine()));
                }
                scanner.close();
            }
            SQChat.getInstance().getLogger().info("Loaded SQChat/janebans.txt");
        } catch (IOException e) {
            SQChat.getInstance().getLogger().warning(DiscordUtils.tag("blankman"));
            e.printStackTrace();
        }
    }
    public static void saveBannedPlayers() {
        try {
            File file = new File(SQChat.getInstance().getDataFolder().getAbsolutePath() + "/janebans.txt");
            FileWriter writer = new FileWriter(file);
            for (UUID uuid : bannedPlayers) {
                writer.write(uuid.toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            SQChat.getInstance().getLogger().warning(DiscordUtils.tag("blankman"));
            e.printStackTrace();
        }
    }

    public static void loadConfig() {
        SQChat.getInstance().reloadConfig();
        responses.clear();
        announcements.clear();
        ConfigurationSection configSection = SQChat.getInstance().getConfig().getConfigurationSection("jane.responses");
        for (String key : configSection.getKeys(false)) {
            responses.put(key.toLowerCase(), configSection.getString(key));
        }
        announcements.addAll(SQChat.getInstance().getConfig().getStringList("jane.announcements"));
        SQChat.getInstance().getLogger().info("Jane's config has been reloaded!");
    }

    public static void say(String message, boolean toDiscord) {
        Bukkit.broadcastMessage(ChatColor.BLUE + "JANE> " + ChatColor.WHITE + message);
        if (toDiscord) {
            Bukkit.getPluginManager().callEvent(new JaneMessageEvent(message));
        }
    }

    public static void maybeReply(String message, Player sender) {
        for (String key : responses.keySet()) {
            if (message.toLowerCase().startsWith("jane " + key)) {
                if (bannedPlayers.contains(sender.getUniqueId())) {
                    sender.sendMessage(ChatColor.RED + "You have been banned from speaking to Jane!");
                } else {
                    say(responses.get(key), true);
                }
                return;
            }
        }
    }

    public static void beginAnnouncing() {
        // runs every 5 minutes
        SQChat.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(SQChat.getInstance(), () -> {
            if (announcements.size() == 0) {
                return;
            }
            if (announcements.size() <= announcementsIndex) {
                announcementsIndex = 0;
            }
            say(announcements.get(announcementsIndex), false);
            announcementsIndex++;
        }, 6000, 6000);
    }
}
