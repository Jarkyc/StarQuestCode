package com.spacebeaverstudios.sqcore.commands.Template;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.StringArgument;
import com.spacebeaverstudios.sqcore.objects.Template;
import com.spacebeaverstudios.sqcore.objects.TemplateBlock;
import com.spacebeaverstudios.sqcore.utils.TemplateUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;

public class CreateCmd extends SQCmd {
    public CreateCmd() {
        super("create", "creates a template", true, "sqcore.template.create");
        this.addArgument(new StringArgument("name"));
    }

    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        Player player = (Player) sender;
        String name = (String) args[0];

        if(TemplateUtils.getTemplateByName(name) != null){
            sender.sendMessage(ChatColor.RED + "There is already a template with this name!");
            return;
        }

        Location cornerOne = null;
        Location cornerTwo = null;

        try {
            Region selection = WorldEdit.getInstance().getSessionManager().get(new BukkitPlayer(player)).getSelection(new BukkitWorld(player.getWorld()));

            BlockVector3 vector = selection.getMinimumPoint();
            BlockVector3 vector2 = selection.getMaximumPoint();
            cornerOne = new Location(player.getWorld(), vector.getX(), vector.getY(), vector.getZ());
            cornerTwo = new Location(player.getWorld(), vector2.getX(), vector2.getY(), vector2.getZ());

        } catch (Exception e){
            e.printStackTrace();
        }

        int maxX = 0;
        int maxY = 0;
        int maxZ = 0;

        int minX = 0;
        int minY = 0;
        int minZ = 0;

        if(cornerOne == null || cornerTwo == null){
            player.sendMessage(ChatColor.RED + "Please select a region");
            return;
        }

        if (cornerOne.getBlockX() >= cornerTwo.getBlockX()) {

            maxX = cornerOne.getBlockX();
            minX = cornerTwo.getBlockX();

        } else {

            maxX = cornerTwo.getBlockX();
            minX = cornerOne.getBlockX();

        }

        if (cornerOne.getBlockY() >= cornerTwo.getBlockY()) {

            maxY = cornerOne.getBlockY();
            minY = cornerTwo.getBlockY();

        } else {

            maxY = cornerTwo.getBlockY();
            minY = cornerOne.getBlockY();

        }

        if (cornerOne.getBlockZ() >= cornerTwo.getBlockZ()) {

            maxZ = cornerOne.getBlockZ();
            minZ = cornerTwo.getBlockZ();

        } else {

            maxZ = cornerTwo.getBlockZ();
            minZ = cornerOne.getBlockZ();

        }

        Template template = new Template(name, player.getLocation());

        for (int x = minX; x <= maxX; x ++) {

            System.out.println(x);

            for (int y = minY; y <= maxY; y ++) {

                for (int z = minZ; z <= maxZ; z ++) {

                    Block block = player.getWorld().getBlockAt(x, y, z);

                    if (block.getType().equals(Material.AIR))
                        continue;

                    template.addBlock(block.getLocation(), new TemplateBlock(block.getLocation()));
                    System.out.println(block.getType());

                }

            }

        }
        sender.sendMessage(ChatColor.GREEN + "Template created");
        TemplateUtils.registerTemplate(template);
    }
}
