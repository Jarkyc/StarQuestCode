package com.spacebeaverstudios.sqcore.commands.World;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.StringArgument;
import com.spacebeaverstudios.sqcore.commands.arguments.WorldArgument;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPCmd extends SQCmd {
    public TPCmd() {
        super("tp", "teleports to a worlds spawn", true);
        this.addArgument(new WorldArgument("world"));
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
       World world = (World) args[0];
       Player player = (Player) sender;

       player.teleport(world.getSpawnLocation());
    }
}
