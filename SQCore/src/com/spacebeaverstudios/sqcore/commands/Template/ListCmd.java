package com.spacebeaverstudios.sqcore.commands.Template;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.objects.Template;
import com.spacebeaverstudios.sqcore.utils.TemplateUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ListCmd extends SQCmd {
    public ListCmd() {
        super("list", "list templates", true, "sqcore.template.list");
    }


    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        for(Template template : TemplateUtils.getTemplates()){
            sender.sendMessage(ChatColor.GREEN + template.name);
        }
    }
}
