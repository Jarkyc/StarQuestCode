package com.spacebeaverstudios.sqcompass.commands;

import com.spacebeaverstudios.sqcompass.utils.CompassUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.StringArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteMarkerCmd extends SQCmd {
    public DeleteMarkerCmd() {
        super("deletemarker", "Deletes a custom marker from the compass display. " +
                "Do /compass markers if you forgot which ones you have.", true);

        this.addArgument(new StringArgument("marker"));
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player player = (Player) sender;
        if (CompassUtils.getCustomMarkers().get(player.getUniqueId()).containsKey(args[0].toString())) {
            CompassUtils.getCompassMarkers().get(player).remove(
                    CompassUtils.getCustomMarkers().get(player.getUniqueId()).get(args[0].toString()));
            CompassUtils.getCustomMarkers().get(player.getUniqueId()).remove(args[0].toString());
            CompassUtils.render(player);
            player.sendMessage(ChatColor.GREEN + "Marker deleted!");
        } else player.sendMessage(ChatColor.RED + "You don't have any markers with that name! " +
                "Do /compass markers if you forgot which ones you have.");
    }
}
