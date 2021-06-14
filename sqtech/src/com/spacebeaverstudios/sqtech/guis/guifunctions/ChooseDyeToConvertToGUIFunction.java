package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.DyeMixerMachine;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChooseDyeToConvertToGUIFunction extends GUIFunction {
    private final DyeMixerMachine machine;
    private final Material color;

    public ChooseDyeToConvertToGUIFunction(DyeMixerMachine machine, Material color) {
        this.machine = machine;
        this.color = color;
    }

    public void run(Player player) {
        machine.setDyeToConvertTo(color);
        player.closeInventory();
    }
}
