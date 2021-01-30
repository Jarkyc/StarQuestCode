package com.spacebeaverstudios.sqsmoothcraft.Objects;

import com.spacebeaverstudios.sqsmoothcraft.SQSmoothcraft;
import com.spacebeaverstudios.sqsmoothcraft.Utils.MathUtils;
import io.netty.util.internal.MathUtil;
import org.bukkit.Location;
import org.bukkit.Material;
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
    public Vector autoPilotDirection;
    public boolean isAutopilot = false;
    private Location originVec;

    public Ship(HashSet<ShipBlock> blocks, Player owner, Location origin, ShipBlock core, Location originalVector){
        this.blocks = blocks;
        this.owner = owner;
        this.shipLocation = origin;
        this.core = core;
        this.originVec = originalVector;

        SQSmoothcraft.instance.allShips.add(this);
        for(ShipBlock block : blocks){
            block.buildArmorStand();
        }
        core.armorStand.addPassenger(owner);
    }

    public Player getOwner(){
        return this.owner;
    }

    public void onTick(){
        handleShiftFly();
        updateOrigin();
        updateData();
        rotateStands();
        handleAutoPilot();

    }

    private void updateData(){
        for(ShipBlock block : blocks){
            block.location = block.getArmorStand().getLocation();

            if(((getOwner().getInventory().getItemInMainHand().getType() != Material.CLOCK && !isAutopilot) || (getOwner().getInventory().getItemInMainHand().getType() == Material.CLOCK && !getOwner().isSneaking())) && block.armorStand.getVelocity().getY() < 0) {
                block.armorStand.setVelocity(block.armorStand.getVelocity().clone().setY(0));
            }
        }
    }

    private void updateOrigin(){
        this.shipLocation = core.armorStand.getLocation();
    }

    private void rotateStands(){

        if(isAutopilot)
            return;

        double pitch = this.getOwner().getEyeLocation().getPitch();
        pitch = Math.toRadians(pitch);

        double yaw = this.getOwner().getEyeLocation().getYaw();
        yaw = Math.toRadians(yaw);

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

            World world = this.shipLocation.getWorld();

            Location locationShip = new Location(world,
                    ((arg1 * shipLocation.y) - (arg2 * shipLocation.z) + (yawCos * shipLocation.x)),
                    ((shipLocation.y * pitchCos) - (shipLocation.z * pitchSin)),
                    ((arg3 * shipLocation.y) + (arg4 * shipLocation.z) + (yawSin * shipLocation.x))).add(this.shipLocation);

           locationShip.setYaw(0);
           locationShip.setPitch(0);


            block.armorStand.teleport(locationShip);

            block.armorStand.setHeadPose(new EulerAngle(pitch, yaw, 0));


        }

    }

    private void handleShiftFly(){

        if(getOwner().isSneaking() && !isAutopilot && getOwner().getInventory().getItemInMainHand().getType() == Material.CLOCK){
            for(ShipBlock block : blocks){
                block.armorStand.setVelocity(getOwner().getLocation().getDirection().normalize().multiply(1));
            }
        }

    }

    private void handleAutoPilot(){
        if(isAutopilot){
            for(ShipBlock block : blocks){
                block.armorStand.setVelocity(this.autoPilotDirection.normalize().multiply(1));
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


            //Fixes ships building a block below where they are showing
            locationShip.add(0, 1, 0);

            locationShip.getWorld().getBlockAt(locationShip).setType(block.getMaterial());
            block.getArmorStand().remove();
            SQSmoothcraft.instance.allShips.remove(this);

        }


    }



}
