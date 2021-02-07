package com.spacebeaverstudios.sqsmoothcraft.Tasks;

import org.bukkit.*;
import org.bukkit.util.Vector;

public class CannonTask {

    public CannonTask(Location origin, Vector vector){
        Location newLoc = origin.clone();
        int steps = 100;
        vector.normalize();
        for(int i = 0; i < steps; i++){
            newLoc = newLoc.add(vector);

            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 127, 255), 2);
            origin.getWorld().spawnParticle(Particle.REDSTONE, newLoc, 1, 0, 0, 0, 1, dustOptions, true);
            newLoc.getWorld().playSound(newLoc, Sound.ENTITY_GENERIC_SPLASH, 1, 1);
        }

    }

}

