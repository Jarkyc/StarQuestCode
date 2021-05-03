package com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;

@SuppressWarnings("unused")
public abstract class TargetSelector {
    private final boolean canTargetMobs;

    public boolean canTargetMobs() {
        return canTargetMobs;
    }

    public TargetSelector(boolean canTargetMobs) {
        this.canTargetMobs = canTargetMobs;
    }

    public ArrayList<LivingEntity> getEntitiesWithinRange(Location location, int range) {
        if (canTargetMobs()) {
            return new ArrayList<>(location.getNearbyLivingEntities(range, this::isSelected));
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

    public ArrayList<LivingEntity> getEntitiesWithinBox(Location location, BoundingBox box) {
        ArrayList<LivingEntity> livingEntities = new ArrayList<>();
        for (Entity entity : location.getWorld().getNearbyEntities(box,
                entity -> entity instanceof LivingEntity && isSelected((LivingEntity) entity))) {
            livingEntities.add((LivingEntity) entity);
        }
        return livingEntities;
    }
    public ArrayList<LivingEntity> getSortedEntitiesWithinBox(Location location, BoundingBox box) {
        ArrayList<LivingEntity> list = getEntitiesWithinBox(location, box);
        list.sort((entity1, entity2) ->
                (int) (Math.round(location.distance(entity2.getLocation()) - location.distance(entity1.getLocation()))));
        return list;
    }

    public abstract boolean isSelected(LivingEntity entity);
}
