package com.spacebeaverstudios.sqtech.commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqtech.guis.PipeGUI;
import com.spacebeaverstudios.sqtech.objects.Pipe;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PipeCmd extends SQCmd {
    public PipeCmd() {
        super("pipe", "Shows what machines are connected to a pipe.", true);
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player player = (Player) sender;
        Block target = player.getTargetBlock(5);

        if (target == null || !Pipe.getPipesByBlock().containsKey(target.getLocation())) {
            player.sendMessage(ChatColor.RED + "You aren't looking at a pipe!");
        } else {
            (new PipeGUI(Pipe.getPipesByBlock().get(target.getLocation()), target.getLocation())).open(player);
        }
    }
}
