package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.machines.Machine;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ConfirmInputColorsGUIFunction extends GUIFunction {
    private final Machine machine;
    private final ArrayList<Material> enabledColors;

    public ConfirmInputColorsGUIFunction(Machine machine, ArrayList<Material> enabledColors) {
        this.machine = machine;
        this.enabledColors = enabledColors;
    }

    public void run(Player player) {
        machine.setInputPipeMaterials(enabledColors);
        player.closeInventory();
    }
}
