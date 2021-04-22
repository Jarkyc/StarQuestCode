package com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs;

import com.spacebeaverstudios.sqcombatnpcs.objects.targetselectors.TargetSelector;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class StructureCombatNPC extends CombatNPC {
    // static
    private static final HashMap<Location, StructureCombatNPC> npcsByBlock = new HashMap<>();

    public static HashMap<Location, StructureCombatNPC> getNPCsByBlock() {
        return npcsByBlock;
    }

    // instance
    protected final ArrayList<Location> blocks = new ArrayList<>();
    protected int health;

    public StructureCombatNPC(ArrayList<Location> blocks, TargetSelector enemies, int health) {
        super(enemies);
        this.health = health;
        this.blocks.addAll(blocks);
    }

    public abstract void damage(int damage);
}
