package com.spacebeaverstudios.sqchat.utils;

import com.spacebeaverstudios.sqchat.SQChat;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class ChatUtils {
    public enum Channel {
        GLOBAL,
        PLANET,
        LOCAL,
        TOWN,
        NATION,
        STAFF
    }

    private static final HashMap<Player, ChatUtils.Channel> playerChannels = new HashMap<>();
    private static final HashMap<UUID, Integer> mutedPlayers = new HashMap<>();
    private static final HashMap<Player, Player> replies = new HashMap<>();
    private static final ArrayList<Player> channelSpies = new ArrayList<>();
    private static final ArrayList<Player> msgSpies = new ArrayList<>();
    private static Chat chat = null;

    public static HashMap<Player, Channel> getPlayerChannels() {
        return playerChannels;
    }
    public static HashMap<UUID, Integer> getMutedPlayers() {
        return mutedPlayers;
    }
    public static HashMap<Player, Player> getReplies() {
        return replies;
    }
    public static ArrayList<Player> getChannelSpies() {
        return channelSpies;
    }
    public static ArrayList<Player> getMsgSpies() {
        return msgSpies;
    }

    public static String getRankString(Player player) {
        if (chat == null) {
            SQChat.getInstance().getLogger().warning(DiscordUtils.tag("blankman") + " chat == null");
            return "";
        } else if (chat.getGroupPrefix("world", chat.getPrimaryGroup(player)).equals("")) {
            return "";
        } else {
            return ChatColor.translateAlternateColorCodes('&', chat.getGroupPrefix("world", chat.getPrimaryGroup(player)));
        }
    }

    public static void loadMutedPlayers() {
        File file = new File(SQChat.getInstance().getDataFolder().getAbsolutePath() + "/muted.txt");
        try {
            if (!file.createNewFile()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    mutedPlayers.put(UUID.fromString(line.split(",")[0]), Integer.parseInt(line.split(",")[1]));
                }
                scanner.close();
            }
        } catch (IOException e) {
            SQChat.getInstance().getLogger().warning(DiscordUtils.tag("blankman") + " File loading error from SQChat!");
            e.printStackTrace();
        }

        SQChat.getInstance().getLogger().info("Loaded muted players");
    }
    public static void saveMutedPlayers() {
        File file = new File(SQChat.getInstance().getDataFolder().getAbsolutePath() + "/muted.txt");
        try {
            FileWriter writer = new FileWriter(file);
            for (UUID uuid : mutedPlayers.keySet()) {
                writer.write(uuid.toString() + "," + mutedPlayers.get(uuid).toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            SQChat.getInstance().getLogger().warning(DiscordUtils.tag("blankman") + " File saving error from SQChat!");
            e.printStackTrace();
        }
    }

    public static void setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider == null) {
            SQChat.getInstance().getLogger().warning(DiscordUtils.tag("blankman") + " chatProvider == null");
        } else {
            chat = chatProvider.getProvider();
        }
    }

    public static String getSocialSpyPrefix() {
        return ChatColor.DARK_BLUE + "[" + ChatColor.BLUE + "SocialSpy" + ChatColor.DARK_BLUE + "] ";
    }
}
