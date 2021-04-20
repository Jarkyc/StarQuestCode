package com.spacebeaverstudios.sqcore.objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlockState;

import java.io.Serializable;

public class TemplateBlock implements Serializable {

    private Material blockType;
    private BlockData data;

    public TemplateBlock(Location location){
        this.blockType = location.getBlock().getType();
        this.data = location.getBlock().getBlockData();
    }

    public void paste(Location loc){
        loc.getBlock().setType(blockType);
        loc.getBlock().setBlockData(data);
    }

}
