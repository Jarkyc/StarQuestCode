package com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs;

import com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors.TargetSelector;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class AutoTurretCombatNPC extends StructureCombatNPC {
    private final Location fireFrom;
    private final int FIRING_COOLDOWN;
    private int firingCooldown = 0;
    private final int DAMAGE;
    private final int FIRING_RANGE;
    private final int WARNING_SHOT_RANGE;

    public AutoTurretCombatNPC(ArrayList<Location> blocks, Location fireFrom, TargetSelector enemies, int health, int firingCooldown,
                               int damage, int firingRange, int warningShotRange) {
        super(blocks, enemies, health);
        this.fireFrom = fireFrom.add(0.5, 0.5, 0.5);
        this.FIRING_COOLDOWN = firingCooldown;
        this.DAMAGE = damage;
        this.FIRING_RANGE = firingRange;
        this.WARNING_SHOT_RANGE = warningShotRange;
    }

    public void tick() {
        firingCooldown++;
        if (firingCooldown >= FIRING_COOLDOWN) {
            firingCooldown = 0;
            ArrayList<Entity> targets = enemies.getSortedEntitiesWithinRange(fireFrom, WARNING_SHOT_RANGE);
            entityloop: for (Entity entity : targets) {
                Location eLoc = ((LivingEntity) entity).getEyeLocation();
                Vector direction = (new Vector(eLoc.getX() - fireFrom.getX(),
                        eLoc.getY() - fireFrom.getY(),
                        eLoc.getZ() - fireFrom.getZ())).normalize();
                Location currLoc = fireFrom.clone();
                double distance = fireFrom.distance(eLoc);

                // determine if can hit the entity, otherwise continue to next closest
                for (int i = 0; i < distance; i++) {
                    currLoc.add(direction);
                    if (!currLoc.getBlock().isPassable()) {
                        continue entityloop;
                    }
                }

                // calculate how much to miss/hit the target by
                Vector offset;
                if (distance <= FIRING_RANGE) {
                    // try to shoot at the player, with random accuracy based on sqrt(x) function
                    offset = new Vector(0, ThreadLocalRandom.current().nextDouble(0,
                            Math.sqrt((20.0 // The lower this number is, the higher the chance of hitting. Don't put it above 25.0
                                    / FIRING_RANGE) * distance) + 1), 0);
                    if (offset.getY() <= 1.5) {
                        // the shot hit
                        ((LivingEntity) entity).damage(DAMAGE);
                    }
                } else {
                    // always miss by 5 blocks
                    offset = new Vector(0, 5, 0);
                }

                // take the offset and spin it random degrees around the player
                // if the shot doesn't hit, multiply the distance by 1.2 for more realism
                Location fireAtUnmultiplied = eLoc.clone().add(offset.rotateAroundAxis(direction, Math.random() * 360));
                Location fireAt = fireFrom.clone().add((new Vector(fireAtUnmultiplied.getX() - fireFrom.getX(),
                            fireAtUnmultiplied.getY() - fireFrom.getY(),
                            fireAtUnmultiplied.getZ() - fireFrom.getZ())).multiply(offset.getY() <= 1.5 ? 1 : 1.2));
                direction = (new Vector(fireAt.getX() - fireFrom.getX(), fireAt.getY() - fireFrom.getY(),
                        fireAt.getZ() - fireFrom.getZ())).normalize();

                currLoc = fireFrom.clone();
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 127, 255), 2);
                for (int i = 0; i < distance; i++) {
                    currLoc.getWorld().playSound(currLoc, Sound.ENTITY_GENERIC_SPLASH, 1, 1);
                    currLoc.getWorld().spawnParticle(Particle.REDSTONE, currLoc, 1, 0, 0, 0, 1, dustOptions, true);
                    currLoc.add(direction);
                }
                break;
            }
        }
    }

    public void damage(int damage) {
        health -= damage;
        if (health <= 0) {
            die();
        }
    }
}
