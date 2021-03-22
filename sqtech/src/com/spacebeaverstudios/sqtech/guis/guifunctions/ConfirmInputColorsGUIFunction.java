package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ConfirmInputColorsGUIFunction extends GUIFunction {
    private final Machine machine;
    private final ArrayList<Material> enabledColors;
    private final Machine.TransferType transferType;

    public ConfirmInputColorsGUIFunction(Machine machine, ArrayList<Material> enabledColors, Machine.TransferType transferType) {
        this.machine = machine;
        this.enabledColors = enabledColors;
        this.transferType = transferType;
    }

    public void run(Player player) {
        if (transferType == Machine.TransferType.ITEMS) {
            machine.setItemInputPipeMaterials(enabledColors, player);
        } else {
            machine.setPowerInputPipeMaterials(enabledColors, player);
        }
        player.closeInventory();
    }
}
