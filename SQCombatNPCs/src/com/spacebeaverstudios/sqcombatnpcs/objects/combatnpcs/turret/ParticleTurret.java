package com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs.turret;

import com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors.TargetSelector;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class ParticleTurret extends Turret {
    private int firingCooldown = 0;
    private final int FIRING_COOLDOWN;
    private final int DAMAGE;
    private final Integer FIRING_RANGE;
    private final BoundingBox firingBox;
    private final Integer WARNING_SHOT_RANGE;
    private final BoundingBox warningBox;

    public ParticleTurret(Location location, DyeColor color, TargetSelector enemies, int health, int firingCooldown,
                  int damage, int firingRange, int warningShotRange) {
        super(location, color, enemies, health);
        this.FIRING_COOLDOWN = firingCooldown;
        this.DAMAGE = damage;
        this.FIRING_RANGE = firingRange;
        this.firingBox = null;
        this.WARNING_SHOT_RANGE = Math.max(firingRange, warningShotRange);
        this.warningBox = null;
    }

    public ParticleTurret(Location location, DyeColor color, TargetSelector enemies, int health, int firingCooldown,
                          int damage, BoundingBox firingBox, BoundingBox warningBox) {
        super(location, color, enemies, health);
        this.FIRING_COOLDOWN = firingCooldown;
        this.DAMAGE = damage;
        this.FIRING_RANGE = null;
        this.firingBox = firingBox;
        this.WARNING_SHOT_RANGE = null;
        this.warningBox = warningBox;
    }

    public void tick() {
        firingCooldown++;
        if (firingCooldown >= FIRING_COOLDOWN) {
            firingCooldown = 0;
            ArrayList<LivingEntity> targets;
            if (warningBox == null) {
                targets = enemies.getSortedEntitiesWithinRange(fireFrom, WARNING_SHOT_RANGE);
            } else {
                targets = enemies.getSortedEntitiesWithinBox(fireFrom, warningBox);
            }

            if (targets.size() == 0) {
                close();
                return;
            } else {
                open();
            }

            entityloop: for (LivingEntity entity : targets) {
                Location eLoc = entity.getEyeLocation();
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
                if ((FIRING_RANGE != null && distance <= FIRING_RANGE)
                        || (firingBox != null && firingBox.contains(entity.getEyeLocation().toVector()))) {
                    // try to shoot at the player, with random accuracy based on sqrt(x) function
                    offset = new Vector(0, ThreadLocalRandom.current().nextDouble(0,
                            // The lower the first number is, the higher the chance of hitting. Don't put it above 25.0
                            // Hopefully firingBox isn't super skewed in size
                            Math.sqrt((20.0 / (FIRING_RANGE != null ? FIRING_RANGE : firingBox.getWidthX() / 2)) * distance) + 1),
                            0);
                    if (offset.getY() <= 1.5) {
                        // the shot hit
                        entity.damage(DAMAGE);
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
}
