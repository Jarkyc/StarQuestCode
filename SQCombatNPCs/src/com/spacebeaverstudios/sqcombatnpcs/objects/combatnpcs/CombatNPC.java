package com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs;

import com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors.TargetSelector;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unused")
public abstract class CombatNPC {
    // TODO: make sure something happens to the entities on server shut down, otherwise problems be happening
    // static
    private static final ArrayList<CombatNPC> npcs = new ArrayList<>();
    private static final HashMap<LivingEntity, CombatNPC> npcsByEntity = new HashMap<>();

    public static ArrayList<CombatNPC> getNPCs() {
        return npcs;
    }
    public static HashMap<LivingEntity, CombatNPC> getNPCsByEntity() {
        return npcsByEntity;
    }

    public static void tickAll() {
        for (CombatNPC npc : npcs) {
            if (npc.isLoaded()) {
                npc.tick();
            }
        }
    }

    // instance
    private final ArrayList<Runnable> deathListeners = new ArrayList<>();
    protected TargetSelector enemies;
    protected final LivingEntity entity;

    public TargetSelector getEnemies() {
        return enemies;
    }
    public LivingEntity getEntity() {
        return entity;
    }

    public CombatNPC(LivingEntity entity, TargetSelector enemies) {
        npcs.add(this);
        this.entity = entity;
        entity.setRemoveWhenFarAway(false);
        npcsByEntity.put(entity, this);
        this.enemies = enemies;
    }

    public abstract void tick();

    public void addDeathListener(Runnable runnable) {
        deathListeners.add(runnable);
    }

    public void die() {
        if (!entity.isDead()) {
            entity.remove();
        }
        for (Runnable listener : deathListeners) {
            listener.run();
        }
        npcs.remove(this);
        npcsByEntity.remove(entity);
    }

    public boolean isLoaded() {
        Location loc = entity.getLocation();
        return loc.getWorld().isChunkLoaded(loc.getBlockX() / 16, loc.getBlockZ() / 16);
    }
}
