package com.spacebeaverstudios.sqcore.commands.Template;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.objects.template.Template;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ListCmd extends SQCmd {
    public ListCmd() {
        super("list", "list templates", false, "sqcore.template.list");
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        sender.sendMessage(ChatColor.GOLD + "Templates:");
        for (String name : Template.getTemplates().keySet()) {
            sender.sendMessage(ChatColor.AQUA + name);
        }
    }
}
