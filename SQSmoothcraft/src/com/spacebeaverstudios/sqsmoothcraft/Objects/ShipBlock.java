package com.spacebeaverstudios.sqsmoothcraft.Objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class ShipBlock {

    public ShipLocation shipLoc;
    public Location location;
    public ArmorStand armorStand;
    private Material material;

    public ShipBlock(ShipLocation shipLocation, Location location, Material material){
        this.shipLoc = shipLocation;
        this.location = location;
        this.material = material;
    }

    public Material getMaterial(){
        return this.material;
    }

    public void buildArmorStand(){

        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        armorStand.getEquipment().setHelmet(new ItemStack(this.material));
        armorStand.setVisible(false);
        armorStand.setCollidable(false);


        location.getWorld().getBlockAt(location).setType(Material.AIR);



    }

    public ArmorStand getArmorStand(){
        return this.armorStand;
    }


}
