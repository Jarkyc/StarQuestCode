package com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs;

import com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors.TargetSelector;

import java.util.ArrayList;

public abstract class CombatNPC {
    // static
    private static final ArrayList<CombatNPC> npcs = new ArrayList<>();

    public static ArrayList<CombatNPC> getNPCs() {
        return npcs;
    }

    public static void tickAll() {
        for (CombatNPC npc : npcs) {
            npc.tick();
        }
    }

    // instance
    private final ArrayList<Runnable> deathListeners = new ArrayList<>();
    protected TargetSelector enemies;

    public CombatNPC(TargetSelector enemies) {
        this.enemies = enemies;
        npcs.add(this);
    }

    public abstract void tick();

    public void addDeathListener(Runnable runnable) {
        deathListeners.add(runnable);
    }

    public void die() {
        for (Runnable listener : deathListeners) {
            listener.run();
        }
        npcs.remove(this);
    }
}
