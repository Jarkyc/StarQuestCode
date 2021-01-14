package com.spacebeaverstudios.sqsmoothcraft.Objects;

import com.spacebeaverstudios.sqsmoothcraft.SQSmoothcraft;
import com.spacebeaverstudios.sqsmoothcraft.Utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Iterator;

public class Ship {

    private HashSet<ShipBlock> blocks;
    private Player owner;
    private Location shipLocation;
    private ShipBlock core;

    public Ship(HashSet<ShipBlock> blocks, Player owner, Location origin, ShipBlock core){
        this.blocks = blocks;
        this.owner = owner;
        this.shipLocation = origin;
        this.core = core;

        SQSmoothcraft.instance.allShips.add(this);
        for(ShipBlock block : blocks){
            block.buildArmorStand();
        }
        core.getArmorStand().addPassenger(owner);
    }

    public Player getOwner(){
        return this.owner;
    }

    public void onTick(){
        rotateStands();
        updateOrigin();
        updateData();
        handleShiftFly();
    }

    private void updateData(){
        for(ShipBlock block : blocks){
            block.location = block.getArmorStand().getLocation();


            block.armorStand.setVelocity(block.armorStand.getVelocity().clone().setY(0));
        }
    }

    private void updateOrigin(){
        this.shipLocation = core.getArmorStand().getLocation();
    }

    private void rotateStands(){

        double pitch = this.getOwner().getLocation().getPitch();
        pitch = Math.toRadians(pitch);

        double yaw = this.getOwner().getLocation().getYaw();
        yaw = Math.toRadians(yaw);

        double yawCos = Math.cos(yaw);
        double yawSin = Math.sin(yaw);

        double pitchCos = Math.cos(pitch);
        double pitchSin = Math.sin(pitch);

        double arg1 = yawSin * pitchSin * -1;
        double arg2 = yawSin * pitchCos;
        double arg3 = yawCos * pitchSin;
        double arg4 = yawCos * pitchCos;

        Vector vec = this.owner.getLocation().getDirection();


        for(ShipBlock block : this.blocks){

            ShipLocation shipLocation = block.shipLoc;

            World world = this.shipLocation.getWorld();

            Location locationShip = new Location(world,
                    ((arg1 * shipLocation.y) - (arg2 * shipLocation.z) + (yawCos * shipLocation.x)),
                    ((shipLocation.y * pitchCos) - (shipLocation.z * pitchSin)),
                    ((arg3 * shipLocation.y) + (arg4 * shipLocation.z) + (yawSin * shipLocation.x))).add(this.shipLocation.clone());

            locationShip.setYaw(0);
            locationShip.setPitch(0);


            block.armorStand.teleport(locationShip);
            block.armorStand.setHeadPose(new EulerAngle(pitch, yaw, 0));

        }

    }

    private void handleShiftFly(){

        for(ShipBlock block : blocks){
            if(getOwner().isSneaking()){
                block.armorStand.setVelocity(getOwner().getLocation().getDirection().normalize().multiply(1));
            }
        }

    }

    //Trigger on unpilot
    public void buildSolid(){

        double pitch = Math.toRadians(0);


        Iterator<ShipBlock> it = blocks.iterator();
        ShipBlock firstBlock = it.next();

        double euToDeg = firstBlock.armorStand.getHeadPose().getY() * 180 / Math.PI;
        if(euToDeg < 0) euToDeg += 360;


        double closestCard = MathUtils.getClosestCardinal(euToDeg);
        double yaw = Math.toRadians(closestCard);

        double yawCos = Math.cos(yaw);
        double yawSin = Math.sin(yaw);

        double pitchCos = Math.cos(pitch);
        double pitchSin = Math.sin(pitch);

        double arg1 = yawSin * pitchSin * -1;
        double arg2 = yawSin * pitchCos;
        double arg3 = yawCos * pitchSin;
        double arg4 = yawCos * pitchCos;

        for(ShipBlock block : this.blocks){

            ShipLocation shipLocation = block.shipLoc;

            Location locationShip = new Location(this.shipLocation.getWorld(),
                    ((arg1 * shipLocation.y) - (arg2 * shipLocation.z) + (yawCos * shipLocation.x)),
                    ((shipLocation.y * pitchCos) - (shipLocation.z * pitchSin)),
                    ((arg3 * shipLocation.y) + (arg4 * shipLocation.z) + (yawSin * shipLocation.x))).add(this.shipLocation.clone());



            locationShip.getWorld().getBlockAt(locationShip.toBlockLocation()).setType(block.getMaterial());
            block.getArmorStand().remove();
            SQSmoothcraft.instance.allShips.remove(this);

        }


    }



}
