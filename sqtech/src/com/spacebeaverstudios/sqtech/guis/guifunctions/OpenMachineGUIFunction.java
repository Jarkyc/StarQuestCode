package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.entity.Player;

public class OpenMachineGUIFunction extends GUIFunction {
    private final GUI gui;
    private final Machine machine;

    public OpenMachineGUIFunction(GUI gui, Machine machine) {
        this.gui = gui;
        this.machine = machine;
    }

    public void run(Player player) {
        machine.setGUI(gui);
        gui.open(player);
    }
}
