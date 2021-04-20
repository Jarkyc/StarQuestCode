package com.spacebeaverstudios.sqcore.commands.Template;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.TemplateArgument;
import com.spacebeaverstudios.sqcore.objects.template.Template;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PasteCmd extends SQCmd {
    public PasteCmd() {
        super("paste", "pastes the selected template", true, "sqcore.template.paste");
        this.addArgument(new TemplateArgument("template"));
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        ((Template) args[0]).paste(((Player) sender).getLocation());
        sender.sendMessage(ChatColor.GREEN + "Pasted template");
    }
}
