package com.spacebeaverstudios.sqtech.commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.command.CommandSender;

public class PipeCmd extends SQCmd {
    public PipeCmd() {
        super("pipe", "Shows what machines are connected to a pipe.", true);
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        // TODO
    }
}
