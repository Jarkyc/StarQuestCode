package com.spacebeaverstudios.sqchat;

import com.spacebeaverstudios.sqchat.commands.*;
import com.spacebeaverstudios.sqchat.listeners.*;
import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class SQChat extends JavaPlugin { // TODO: implement social spy
    private static SQChat instance;

    public static SQChat getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        ChatUtils.setupChat();
        ChatUtils.loadMutedPlayers();

        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new WorldSaveListener(), this);

        // register commands
        getCommand("g").setExecutor(new GlobalChatCmd());
        getCommand("l").setExecutor(new LocalChatCmd());
        getCommand("nc").setExecutor(new NationChatCmd());
        getCommand("p").setExecutor(new PlanetChatCmd());
        getCommand("s").setExecutor(new StaffChatCmd());
        getCommand("tc").setExecutor(new TownChatCmd());

        MessageCmd messageCmd = new MessageCmd();
        getCommand("message").setExecutor(messageCmd);
        getCommand("message").setTabCompleter(messageCmd);

        ReplyCmd replyCmd = new ReplyCmd();
        getCommand("reply").setExecutor(replyCmd);
        getCommand("reply").setTabCompleter(replyCmd);

        MuteCmd muteCmd = new MuteCmd();
        getCommand("mute").setExecutor(muteCmd);
        getCommand("mute").setTabCompleter(muteCmd);

        UnmuteCmd unmuteCmd = new UnmuteCmd();
        getCommand("unmute").setExecutor(unmuteCmd);
        getCommand("unmute").setTabCompleter(unmuteCmd);

        // script to unmute people
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (UUID uuid : ChatUtils.getMutedPlayers().keySet()) {
                int newValue = ChatUtils.getMutedPlayers().get(uuid)-1;
                if (newValue == 0) {
                    ChatUtils.getMutedPlayers().remove(uuid);
                    Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "You have been unmuted!");
                } else ChatUtils.getMutedPlayers().put(uuid, newValue);
            }
        }, 1200, 1200);
    }

    @Override
    public void onDisable() {
        ChatUtils.saveMutedPlayers();
    }
}
