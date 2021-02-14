package com.spacebeaverstudios.sqcore.commands.World;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.command.CommandSender;

public class WorldCmd extends SQCmd {
    public WorldCmd() {
        super("world", "world commands", true);
            this.addSubcommand(new TPCmd());
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {

    }
}
