package com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs.ship;

import com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs.CombatNPC;
import com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors.TargetSelector;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Pilot;
import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import com.spacebeaverstudios.sqsmoothcraft.Objects.ShipClass;
import com.spacebeaverstudios.sqsmoothcraft.Tasks.DetectionTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public class ShipCombatNPC extends CombatNPC {
    private final Ship ship;
    private final Zombie pilot;
    public int rotation = 0;

    public ShipCombatNPC(TargetSelector enemies, Location pilotLocation, BlockFace facing) {
        super(pilotLocation.getWorld().spawn(pilotLocation, Zombie.class), enemies);

        // initialize entity
        this.pilot = (Zombie) entity;
        pilot.setCollidable(false);
        pilot.getLocation().setPitch(0);
        pilot.setAI(false);
        pilot.setInvulnerable(true);
        switch (facing) {
            case NORTH:
                pilot.getLocation().setYaw(180);
                break;
            case EAST:
                pilot.getLocation().setYaw(270);
                break;
            case SOUTH:
                pilot.getLocation().setYaw(0);
                break;
            case WEST:
                pilot.getLocation().setYaw(90);
                break;
        }
        pilot.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));

        // initialize ship
        this.ship = (new DetectionTask(pilotLocation, new Pilot(pilot), ShipClass.ADMINSHIP)).getShip();
        ship.getOwner().entitySneaking = true; // ship will permanently move forward
    }

    public void tick() {
        rotation = (rotation + 4) % 360;
        setLooking(0, rotation);
    }

    public void setLooking(int pitch, int yaw) {
        Location newLoc = pilot.getLocation().clone();
        newLoc.setPitch(pitch);
        newLoc.setYaw(yaw);
        pilot.teleport(newLoc);
        ship.getCore().armorStand.addPassenger(pilot);
    }
}
