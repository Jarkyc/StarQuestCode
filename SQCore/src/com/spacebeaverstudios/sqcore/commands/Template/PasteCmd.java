package com.spacebeaverstudios.sqcore.commands.Template;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.StringArgument;
import com.spacebeaverstudios.sqcore.objects.Template;
import com.spacebeaverstudios.sqcore.utils.TemplateUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PasteCmd extends SQCmd {
    public PasteCmd() {
        super("paste", "pastes the selected template", true, "sqcore.template.paste");
        this.addArgument(new StringArgument("name"));
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        String template = (String) args[0];
        if(TemplateUtils.getTemplateByName(template) == null){
            sender.sendMessage(ChatColor.RED + "There are no templates with this name");
            return;
        } else {
            Template template1 = TemplateUtils.getTemplateByName(template);
            template1.paste(((Player) sender).getLocation());
            sender.sendMessage(ChatColor.GREEN + "Pasted template");
        }
    }
}
