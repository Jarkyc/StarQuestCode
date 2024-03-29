package com.spacebeaverstudios.sqcore.objects.template;

import com.spacebeaverstudios.sqcore.SQCore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.io.Serializable;

public class TemplateBlock implements Serializable {
    private final Material material;
    private final String blockDataString; // have to store as a String because BlockData isn't serializable
    private final int vecX, vecY, vecZ; // have to store as Integers because Vectors aren't serializable

    public TemplateBlock(Location location, Location origin) {
        this.material = location.getBlock().getType();
        this.blockDataString = location.getBlock().getBlockData().getAsString();
        this.vecX = location.getBlockX() - origin.getBlockX();
        this.vecY = location.getBlockY() - origin.getBlockY();
        this.vecZ = location.getBlockZ() - origin.getBlockZ();
    }

    @SuppressWarnings("unused")
    public Material getMaterial() {
        return material;
    }
    @SuppressWarnings("unused")
    public BlockData getBlockData() {
        return SQCore.getInstance().getServer().createBlockData(blockDataString);
    }
    public Vector getVector() {
        return new Vector(vecX, vecY, vecZ);
    }

    public void paste(Location loc) {
        loc.getBlock().setType(material);
        loc.getBlock().setBlockData(SQCore.getInstance().getServer().createBlockData(blockDataString));
    }
}
