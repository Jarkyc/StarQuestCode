package com.spacebeaverstudios.sqsmoothcraft.Objects;

import com.spacebeaverstudios.sqsmoothcraft.SQSmoothcraft;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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
        armorStand.setInvulnerable(true);
        armorStand.setVisible(false);
        armorStand.setRemoveWhenFarAway(false);

        location.getWorld().getBlockAt(location).setType(Material.AIR);



    }

    public ArmorStand getArmorStand(){
        return this.armorStand;
    }


}
