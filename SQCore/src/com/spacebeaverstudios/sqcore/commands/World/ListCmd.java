package com.spacebeaverstudios.sqcore.commands.World;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCmd extends SQCmd {
    public ListCmd() {
        super("list", "Lists all loaded worlds", true, "sqcore.world");
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player player = (Player) sender;

        player.sendMessage(ChatColor.GREEN + "Loaded Worlds:");

        for(World world : Bukkit.getWorlds()){
            player.sendMessage(ChatColor.BLUE + world.getName());
        }
    }
}
