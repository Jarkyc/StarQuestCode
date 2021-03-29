package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.guis.ReplicatorGUI;
import com.spacebeaverstudios.sqtech.objects.machines.ReplicatorMachine;
import org.bukkit.entity.Player;

public class SwapReplicationSideGUIFunction extends GUIFunction {
    private final ReplicatorMachine machine;
    private final ReplicatorGUI gui;

    public SwapReplicationSideGUIFunction(ReplicatorMachine machine, ReplicatorGUI gui) {
        this.machine = machine;
        this.gui = gui;
    }

    public void run(Player player) {
        machine.swapReplicationSide();
        gui.refreshInventory();
    }
}
