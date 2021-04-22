package com.spacebeaverstudios.sqcombatnpcs;

import com.spacebeaverstudios.sqcombatnpcs.listeners.StructureCombatNPCListener;
import com.spacebeaverstudios.sqcombatnpcs.objects.combatnpcs.CombatNPC;
import org.bukkit.plugin.java.JavaPlugin;

public class SQCombatNPCs extends JavaPlugin {
    private static SQCombatNPCs instance;

    public static SQCombatNPCs getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new StructureCombatNPCListener(), this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, CombatNPC::tickAll, 1, 1);
    }
}
