package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.ReplicatorMachine;
import org.bukkit.entity.Player;

public class ReplicateGUIFunction extends GUIFunction {
    private final ReplicatorMachine machine;

    public ReplicateGUIFunction(ReplicatorMachine machine) {
        this.machine = machine;
    }

    public void run(Player player) {
        machine.replicate(player);
    }
}
