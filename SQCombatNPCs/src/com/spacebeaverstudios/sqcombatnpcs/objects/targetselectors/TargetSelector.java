package com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;

@SuppressWarnings("unused")
public abstract class TargetSelector {
    // TODO: ship targetting
    private final boolean canTargetMobs;

    public boolean canTargetMobs() {
        return canTargetMobs;
    }

    public TargetSelector(boolean canTargetMobs) {
        this.canTargetMobs = canTargetMobs;
    }

    public ArrayList<LivingEntity> getEntitiesWithinRange(Location location, int range) {
        if (canTargetMobs()) {
            return new ArrayList<>(location.getNearbyEntitiesByType(LivingEntity.class, range, this::isSelected));
        } else {
            return new ArrayList<>(location.getNearbyPlayers(range, this::isSelected));
        }
    }
    public ArrayList<LivingEntity> getSortedEntitiesWithinRange(Location location, int range) {
        ArrayList<LivingEntity> list = getEntitiesWithinRange(location, range);
        list.sort((entity1, entity2) ->
                (int) (Math.round(location.distance(entity2.getLocation()) - location.distance(entity1.getLocation()))));
        return list;
    }

    public abstract boolean isSelected(LivingEntity entity);
}
