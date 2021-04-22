package com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;

public abstract class TargetSelector {
    public abstract boolean isSelected(Entity entity);
    public abstract boolean canTargetMobs();

    public ArrayList<Entity> getEntitiesWithinRange(Location location, int range) {
        if (canTargetMobs()) {
            return new ArrayList<>(location.getNearbyEntitiesByType(LivingEntity.class, range, this::isSelected));
        } else {
            return new ArrayList<>(location.getNearbyPlayers(range, this::isSelected));
        }
    }
    public ArrayList<Entity> getSortedEntitiesWithinRange(Location location, int range) {
        ArrayList<Entity> list = getEntitiesWithinRange(location, range);
        list.sort((entity1, entity2) ->
                (int) (Math.round(location.distance(entity2.getLocation()) - location.distance(entity1.getLocation()))));
        return list;
    }
}
