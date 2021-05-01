package com.spacebeaverstudios.sqcore.commands.Template;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.command.CommandSender;

public class TemplateCmd extends SQCmd {
    public TemplateCmd(){
        super("template", "template commands", true, "sqcore.template");
        this.addSubcommand(new CreateCmd());
        this.addSubcommand(new PasteCmd());
        this.addSubcommand(new ListCmd());
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        this.getHelpCommand().onExecute(sender, previousLabels, args);
    }
}
