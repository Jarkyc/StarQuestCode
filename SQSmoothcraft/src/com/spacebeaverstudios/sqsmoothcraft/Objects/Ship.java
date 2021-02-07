package com.spacebeaverstudios.sqsmoothcraft.Objects;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Modules.Jammer;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Modules.Module;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Modules.Shield;
import com.spacebeaverstudios.sqsmoothcraft.SQSmoothcraft;
import com.spacebeaverstudios.sqsmoothcraft.Utils.MathUtils;
import net.minecraft.server.v1_15_R1.PacketPlayOutSpawnEntity;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
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
    public ArrayList<ShipBlock> pistons;
    public ArrayList<Module> modules = new ArrayList<>();
    public Shield shieldCore;
    public int health;
    public int maxHealth;
    public int shieldHealth;
    public int maxShieldHealth;

    public Ship(HashSet<ShipBlock> blocks, Player owner, Location origin, ShipBlock core, Location originalVector, ArrayList<ShipBlock> pistons) {
        this.blocks = blocks;
        this.owner = owner;
        this.shipLocation = origin;
        this.core = core;
        this.originVec = originalVector;
        this.pistons = pistons;

        SQSmoothcraft.instance.allShips.add(this);
        for (ShipBlock block : blocks) {
            block.buildArmorStand();
        }
        //prevents the player's view from lowering when the crouch
        owner.setFlying(true);
        core.armorStand.addPassenger(owner);
    }

    public Player getOwner() {
        return this.owner;
    }

    public void onTick() {
        updateData();
        handleModules();
        handleShiftFly();
        updateOrigin();
        rotateStands();
        handleAutoPilot();

    }

    public ShipBlock getCore() {
        return this.core;
    }

    public Location getLocation() {
        return core.armorStand.getLocation();
    }

    private void handleModules() {
        for (Module module : this.modules) {
            if (module.passive) {
                module.activate(this, owner);
            }
        }
    }

    private void updateData() {
        for (ShipBlock block : blocks) {
            block.location = block.getArmorStand().getLocation();
            if (((getOwner().getInventory().getItemInMainHand().getType() != Material.CLOCK && !isAutopilot) || (getOwner().getInventory().getItemInMainHand().getType() == Material.CLOCK && !getOwner().isSneaking())) && block.armorStand.getVelocity().getY() < 0) {
                block.armorStand.setVelocity(block.armorStand.getVelocity().clone().setY(0));
            }
        }
    }

    public void damage(int x) {
        if (shieldCore != null && shieldHealth > 0) {
            shieldHealth -= x;
        } else {
            health -= x;
        }
    }

    private void updateOrigin() {
        this.shipLocation = core.armorStand.getLocation();
    }

    private void rotateStands() {

        if (isAutopilot)
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

        for (ShipBlock block : this.blocks) {

            ShipLocation shipLocation = block.shipLoc;

            World world = this.shipLocation.getWorld();

            Location locationShip = new Location(world,
                    ((arg1 * shipLocation.y) - (arg2 * shipLocation.z) + (yawCos * shipLocation.x)),
                    ((shipLocation.y * pitchCos) - (shipLocation.z * pitchSin)),
                    ((arg3 * shipLocation.y) + (arg4 * shipLocation.z) + (yawSin * shipLocation.x))).add(this.shipLocation.clone());

            locationShip.setYaw(0);
            locationShip.setPitch(0);

            locationShip.add(0, block.getyOffset(), 0);

            block.armorStand.teleport(locationShip);

            final double tempPitch = pitch;
            final double tempYaw = yaw;

            Bukkit.getScheduler().scheduleSyncDelayedTask(SQSmoothcraft.instance, new Runnable() {
                @Override
                public void run() {
                    block.armorStand.setHeadPose(new EulerAngle(tempPitch, tempYaw, 0));
                }
            }, 1L);

        }

    }

    private void handleShiftFly() {

        if (getOwner().isSneaking() && !isAutopilot && getOwner().getInventory().getItemInMainHand().getType() == Material.CLOCK) {
            //Don't ask me why but this makes chunk loading happy
            owner.setSneaking(false);
            for (ShipBlock block : blocks) {
                block.armorStand.setVelocity(getOwner().getLocation().getDirection().normalize().multiply(2));
            }
            owner.playSound(core.getArmorStand().getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.PLAYERS, 5, 1);
        }

    }

    private void handleAutoPilot() {
        if (isAutopilot) {

            for (Ship other : SQSmoothcraft.instance.allShips) {
                if (other.getLocation().distance(this.getLocation()) <= 1000) {
                    for (Module module : other.modules) {
                        if (module instanceof Jammer && module.active) {
                            this.isAutopilot = false;
                            owner.sendMessage(ChatColor.RED + "Autopilot has been disabled due to a jammer nearby!");
                        }
                    }
                }
            }

            for (ShipBlock block : blocks) {
                block.armorStand.setVelocity(this.autoPilotDirection.normalize().multiply(1));
            }
        }
    }

    //Trigger on unpilot
    public void buildSolid() {

        double pitch = Math.toRadians(0);


        Iterator<ShipBlock> it = blocks.iterator();
        ShipBlock firstBlock = it.next();

        double euToDeg = firstBlock.armorStand.getHeadPose().getY() * 180 / Math.PI;
        if (euToDeg < 0) euToDeg += 360;


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

        boolean canCompile = true;

        for (ShipBlock block : this.blocks) {
            ShipLocation shipLocation = block.shipLoc;

            Location locationShip = new Location(this.shipLocation.getWorld(),
                    ((arg1 * shipLocation.y) - (arg2 * shipLocation.z) + (yawCos * shipLocation.x)),
                    ((shipLocation.y * pitchCos) - (shipLocation.z * pitchSin)),
                    ((arg3 * shipLocation.y) + (arg4 * shipLocation.z) + (yawSin * shipLocation.x))).add(this.shipLocation.clone());

            locationShip.add(0, 1, 0);

            if (locationShip.getBlock().getType() != Material.AIR) {
                canCompile = false;
                break;
            }

        }


        if (canCompile) {
            owner.setFlying(false);
            owner.leaveVehicle();
            for (ShipBlock block : this.blocks) {

                ShipLocation shipLocation = block.shipLoc;

                Location locationShip = new Location(this.shipLocation.getWorld(),
                        ((arg1 * shipLocation.y) - (arg2 * shipLocation.z) + (yawCos * shipLocation.x)),
                        ((shipLocation.y * pitchCos) - (shipLocation.z * pitchSin)),
                        ((arg3 * shipLocation.y) + (arg4 * shipLocation.z) + (yawSin * shipLocation.x))).add(this.shipLocation.clone());


                //Fixes ships building a block below where they are showing
                locationShip.add(0, 1, 0);

                locationShip.getWorld().getBlockAt(locationShip).setType(block.getMaterial());
                locationShip.getWorld().getBlockAt(locationShip).setBlockData(block.blockData);
                block.getArmorStand().remove();
                SQSmoothcraft.instance.allShips.remove(this);

            }
        } else {
            owner.sendMessage(ChatColor.RED + "Can not unpilot ship; blocks are in the way!");
        }
    }


}
