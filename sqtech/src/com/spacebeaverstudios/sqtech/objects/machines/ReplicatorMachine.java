package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import com.spacebeaverstudios.sqtech.SQTech;
import com.spacebeaverstudios.sqtech.guis.ReplicatorGUI;
import com.spacebeaverstudios.sqtech.guis.guifunctions.OpenMachineGUIFunction;
import com.spacebeaverstudios.sqtech.objects.BoxLocation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ReplicatorMachine extends Machine {
    private boolean copyFromLeft = true;
    private BoxLocation copyFromBox;
    private BoxLocation copyToBox;

    public ReplicatorMachine(Block sign) {
        super(sign);
    }

    public HashMap<Vector, Material> getSchema() {
        // to make things easier, only these count as the "core"
        // everything else can be added or removed later, and just gets recalculated
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.BRICKS);
        schema.put(new Vector(1, 1, 0), Material.BRICKS);
        schema.put(new Vector(1, 0, 1), Material.BRICKS);
        schema.put(new Vector(1, 0, -1), Material.BRICKS);
        schema.put(new Vector(1, -1, 0), Material.LAPIS_BLOCK);
        return schema;
    }

    public void init() {
        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(0, ChatColor.BLUE + "Replicator");
        sign.setLine(1, "<-- From");
        sign.setLine(2, "To -->");
        sign.setLine(3, "");
        sign.update();

        // initial values don't matter because I'm just going to calculate them immediately
        copyFromBox = new BoxLocation(getSign(), getSign());
        copyToBox = new BoxLocation(getSign(), getSign());
        recalculateBoxes();
    }

    private void recalculateBoxes() {
        Location currentBlock;
        BlockFace backwards = ((Directional) getSign().getBlock().getBlockData()).getFacing().getOppositeFace();

        // width
        int width = 0;
        currentBlock = getSign().getBlock().getRelative(backwards).getLocation();
        while (true) {
            if (backwards == BlockFace.NORTH || backwards == BlockFace.SOUTH) {
                if (currentBlock.getBlock().getRelative(BlockFace.EAST, width+1).getType() == Material.BRICKS
                        && currentBlock.getBlock().getRelative(BlockFace.WEST, width+1).getType() == Material.BRICKS) {
                    width++;
                } else {
                    break;
                }
            } else {
                if (currentBlock.getBlock().getRelative(BlockFace.NORTH, width+1).getType() == Material.BRICKS
                        && currentBlock.getBlock().getRelative(BlockFace.SOUTH, width+1).getType() == Material.BRICKS) {
                    width++;
                } else {
                    break;
                }
            }
        }

        // length
        int length = 0;
        currentBlock = getSign().getBlock().getRelative(backwards, 2).getLocation();
        while (currentBlock.getBlock().getType() == Material.BRICKS) {
            length++;
            currentBlock = currentBlock.getBlock().getRelative(backwards).getLocation();
        }

        // height
        int height = 0;
        currentBlock = getSign().getBlock().getRelative(backwards).getLocation();
        while (currentBlock.getBlock().getType() == Material.BRICKS) {
            height++;
            currentBlock.add(0, 1, 0);
        }

        // boxes
        currentBlock = getSign().getBlock().getRelative(backwards).getLocation();
        int xMod, zMod; // referring to right direction
        switch(backwards) {
            case NORTH:
                xMod = 1;
                zMod = 0;
                break;
            case SOUTH:
                xMod = -1;
                zMod = 0;
                break;
            case EAST:
                xMod = 0;
                zMod = 1;
                break;
            case WEST:
                xMod = 0;
                zMod = -1;
                break;
            default:
                // this really shouldn't happen happen, but logging just in case
                xMod = 0;
                zMod = 0;
                SQTech.getInstance().getLogger().warning(DiscordUtils.tag("blankman")
                        + " goddammit the default in switch(backwards) in ReplicatorMachine#recalcalateBoxes happened wtf");
                break;
        }

        Location leftPointOne = currentBlock.clone().add(xMod, 0, zMod).getBlock().getRelative(backwards).getLocation();
        Location leftPointTwo = currentBlock.clone().add(width * xMod, height, width * zMod).getBlock()
                .getRelative(backwards, length).getLocation();
        Location rightPointOne = currentBlock.clone().add(-xMod, 0, -zMod).getBlock().getRelative(backwards).getLocation();
        Location rightPointTwo = currentBlock.clone().add(width * (-xMod), height, width * (-zMod)).getBlock()
                .getRelative(backwards, length).getLocation();

        if (copyFromLeft) {
            copyFromBox.setPointOne(rightPointOne);
            copyFromBox.setPointTwo(rightPointTwo);
            copyToBox.setPointOne(leftPointOne);
            copyToBox.setPointTwo(leftPointTwo);
        } else {
            copyFromBox.setPointOne(leftPointOne);
            copyFromBox.setPointTwo(leftPointTwo);
            copyToBox.setPointOne(rightPointOne);
            copyToBox.setPointTwo(rightPointTwo);
        }
    }

    public boolean getCopyFromLeft() {
        return copyFromLeft;
    }
    public void swapReplicationSide() {
        copyFromLeft = !copyFromLeft;
        Sign sign = (Sign) getSign().getBlock().getState();
        if (copyFromLeft) {
            sign.setLine(1, "<-- From");
            sign.setLine(2, " To  -->");
        } else {
            sign.setLine(1, "From -->");
            sign.setLine(2, "<-- To");
        }
        sign.update();
    }

    public BoxLocation getCopyFromBox() {
        recalculateBoxes();
        return copyFromBox;
    }
    public BoxLocation getCopyToBox() {
        recalculateBoxes();
        return copyToBox;
    }

    public void tick() {
        // do nothing, all the functionality is in GUI
    }

    public ArrayList<Inventory> getChestInventories() {
        Location checking = getSign().clone().subtract(0, 1, 0);
        ArrayList<Inventory> inventories = new ArrayList<>();
        while (checking.getBlock().getType() == Material.CHEST) {
            inventories.add(((Chest) checking.getBlock().getState()).getInventory());
            checking.subtract(0, 1, 0);
        }
        return inventories;
    }

    public List<TransferType> getInputTypes() {
        return Collections.singletonList(TransferType.POWER);
    }
    public TransferType getOutputType() {
        return null;
    }
    public String getMachineName() {
        return "Replicator";
    }
    public String getMachineInfo() {
        return "Replicates ships.\n " + ChatColor.GOLD + "BV / block replicated: " + ChatColor.AQUA + "5";
    }

    @Override
    public GUIItem getCustomOptionsGUIItem() {
        return new GUIItem("Replicate", "", Material.WRITABLE_BOOK,
                new OpenMachineGUIFunction(new ReplicatorGUI(this), this));
    }

    public String getSignText() {
        return "[replicator]";
    }
    public String getCustomSaveText() {
        return String.valueOf(copyFromLeft);
    }
    public void loadCustomSaveText(String text) {
        copyFromLeft = text.equals("true");
    }
}
