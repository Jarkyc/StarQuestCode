package com.spacebeaverstudios.sqchat.commands.moderation.jane;

import com.spacebeaverstudios.sqchat.utils.JaneUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class JaneReloadCmd extends SQCmd {
    public JaneReloadCmd() {
        super("reload", "Reloads Jane's config", false);
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        JaneUtils.loadConfig();
        sender.sendMessage(ChatColor.GREEN + "Jane's config has been reloaded!");
    }
}
