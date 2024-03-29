package com.spacebeaverstudios.sqsmoothcraft.Objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShipBlock {

    public ShipLocation shipLoc;
    public Location location;
    public ArmorStand armorStand;
    private Material material;
    private double yOffset;
    public BlockData blockData;
    public boolean visible;
    public Inventory inv;

    public ShipBlock(ShipLocation shipLocation, Location location, Material material, double yOffset, BlockData data, Inventory inv, boolean visible){
        this.shipLoc = shipLocation;
        this.location = location;
        this.material = material;
        this.yOffset = yOffset;
        this.blockData = data;
        this.visible = visible;
        this.inv = inv;
    }

    public Material getMaterial(){
        return this.material;
    }

    public void buildArmorStand(){

        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        armorStand.setAI(false);
        armorStand.getEquipment().setHelmet(new ItemStack(this.material));
        armorStand.setVisible(false);
        armorStand.setCollidable(false);
        armorStand.setCanTick(false);
        armorStand.setBasePlate(false);



    }

    public double getyOffset(){
        return yOffset;
    }

    public ArmorStand getArmorStand(){
        return this.armorStand;
    }


}
