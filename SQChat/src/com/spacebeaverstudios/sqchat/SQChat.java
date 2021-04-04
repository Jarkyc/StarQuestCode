package com.spacebeaverstudios.sqchat;

import com.spacebeaverstudios.sqchat.commands.channel.*;
import com.spacebeaverstudios.sqchat.commands.moderation.*;
import com.spacebeaverstudios.sqchat.commands.moderation.jane.JaneCmd;
import com.spacebeaverstudios.sqchat.commands.msg.*;
import com.spacebeaverstudios.sqchat.listeners.*;
import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import com.spacebeaverstudios.sqchat.utils.JaneUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

public class SQChat extends JavaPlugin {
    private static SQChat instance;

    public static SQChat getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        ChatUtils.setupChat();
        ChatUtils.loadMutedPlayers();

        if (!(new File(getDataFolder().getAbsolutePath() + "/config.yml")).exists()) {
            this.saveDefaultConfig();
        }
        JaneUtils.loadConfig();
        JaneUtils.loadBannedPlayers();
        JaneUtils.beginAnnouncing();

        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLogInOutListener(), this);
        getServer().getPluginManager().registerEvents(new WorldSaveListener(), this);

        // register commands
        getCommand("g").setExecutor(new GlobalChatCmd());
        getCommand("l").setExecutor(new LocalChatCmd());
        getCommand("nc").setExecutor(new NationChatCmd());
        getCommand("p").setExecutor(new PlanetChatCmd());
        getCommand("s").setExecutor(new StaffChatCmd());
        getCommand("tc").setExecutor(new TownChatCmd());

        MessageCmd messageCmd = new MessageCmd();
        getCommand("msg").setExecutor(messageCmd);
        getCommand("msg").setTabCompleter(messageCmd);

        ReplyCmd replyCmd = new ReplyCmd();
        getCommand("r").setExecutor(replyCmd);
        getCommand("r").setTabCompleter(replyCmd);

        MuteCmd muteCmd = new MuteCmd();
        getCommand("mute").setExecutor(muteCmd);
        getCommand("mute").setTabCompleter(muteCmd);

        UnmuteCmd unmuteCmd = new UnmuteCmd();
        getCommand("unmute").setExecutor(unmuteCmd);
        getCommand("unmute").setTabCompleter(unmuteCmd);

        SocialSpyCmd socialSpyCmd = new SocialSpyCmd();
        getCommand("socialspy").setExecutor(socialSpyCmd);
        getCommand("socialspy").setTabCompleter(socialSpyCmd);

        JaneCmd janeCmd = new JaneCmd();
        getCommand("jane").setExecutor(janeCmd);
        getCommand("jane").setTabCompleter(janeCmd);

        // script to unmute people
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (UUID uuid : ChatUtils.getMutedPlayers().keySet()) {
                ChatUtils.getMutedPlayers().put(uuid, ChatUtils.getMutedPlayers().get(uuid)-1);
                if (ChatUtils.getMutedPlayers().get(uuid) == 0) {
                    ChatUtils.getMutedPlayers().remove(uuid);
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        player.sendMessage(ChatColor.GREEN + "You have been unmuted!");
                    }
                }
            }
        }, 1200, 1200);
    }

    @Override
    public void onDisable() {
        ChatUtils.saveMutedPlayers();
        JaneUtils.saveBannedPlayers();
    }
}
