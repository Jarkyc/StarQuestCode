package com.spacebeaverstudios.sqsmoothcraft.Tasks;

import com.spacebeaverstudios.sqsmoothcraft.Objects.Ship;
import com.spacebeaverstudios.sqsmoothcraft.Utils.ShipUtils;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;

public class CannonTask {

    public CannonTask(Location origin, Vector vector, Ship gunner) {
        Location newLoc = origin.clone();
        int damage = 1;
        int steps = 100;
        vector.normalize();
        for (int i = 0; i < steps; i++) {
            newLoc = newLoc.add(vector);

            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 127, 255), 2);
            origin.getWorld().spawnParticle(Particle.REDSTONE, newLoc, 1, 0, 0, 0, 1, dustOptions, true);
            newLoc.getWorld().playSound(newLoc, Sound.ENTITY_GENERIC_SPLASH, 1, 1);

            if(newLoc.getWorld().getBlockAt(newLoc).getType() != Material.AIR){
                newLoc.getWorld().createExplosion(newLoc, 1);
                return;
            }

            if (!origin.getWorld().getNearbyEntities(newLoc, 1, 1, 1).isEmpty()) {
                Collection<Entity> ents = origin.getWorld().getNearbyEntities(newLoc, 1, 1, 1);

                for (Entity ent : ents) {
                    if (ent.getType() == EntityType.ARMOR_STAND) {
                        ArmorStand stand = (ArmorStand) ent;
                        if (ShipUtils.getShipByStand(stand) != null) {
                            Ship ship = ShipUtils.getShipByStand(stand);
                            if(ship == gunner){
                                break;
                            } else {
                                ship.damage(damage);
                                newLoc.getWorld().playSound(newLoc, Sound.ENTITY_GENERIC_EXPLODE,1, 1);
                                System.out.println(ship.health);
                                return;
                            }
                        }
                    }
                }
                continue;
            }

        }

    }

}

