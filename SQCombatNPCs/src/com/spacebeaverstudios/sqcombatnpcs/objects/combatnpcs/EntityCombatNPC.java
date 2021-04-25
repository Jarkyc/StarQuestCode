package com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs;

import com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors.TargetSelector;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;

@SuppressWarnings("unused")
public abstract class EntityCombatNPC extends CombatNPC {
    // TODO: make sure something happens to the entities on server shut down, otherwise problems be happening
    // static
    private static final HashMap<Entity, EntityCombatNPC> entityCombatNPCs = new HashMap<>();

    public static HashMap<Entity, EntityCombatNPC> getEntityCombatNPCs() {
        return entityCombatNPCs;
    }

    // instance
    protected final LivingEntity entity;

    public EntityCombatNPC(LivingEntity entity, TargetSelector enemies) {
        super(enemies);
        this.entity = entity;
        entity.setRemoveWhenFarAway(false);
        entityCombatNPCs.put(entity, this);
    }

    public boolean isLoaded() {
        Location loc = entity.getLocation();
        return loc.getWorld().isChunkLoaded(loc.getBlockX() / 16, loc.getBlockZ() / 16);
    }

    public void die() {
        super.die();
        entityCombatNPCs.remove(entity);
    }
}
