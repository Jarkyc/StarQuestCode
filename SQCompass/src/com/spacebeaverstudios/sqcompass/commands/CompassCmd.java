package com.spacebeaverstudios.sqcompass.commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.command.CommandSender;

public class CompassCmd extends SQCmd {
    public CompassCmd() {
        super("compass", "Commands for using the compass display", true);

        this.addHelpCommand();
        this.addSubcommand(new CompassMarkersCmd());
        this.addSubcommand(new ShowCommonMarkerCmd());
        this.addSubcommand(new HideCommonMarkerCmd());
        this.addSubcommand(new AddMarkerCmd());
        this.addSubcommand(new DeleteMarkerCmd());
        this.addSubcommand(new ShowCompassCmd());
        this.addSubcommand(new HideCompassCmd());
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        this.getHelpCommand().onExecute(sender, previousLabels, args);
    }
}
