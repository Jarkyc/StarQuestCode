package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChooseOutputColorGUIFunction extends GUIFunction {
    private final Machine machine;
    private final Material material;

    public ChooseOutputColorGUIFunction(Machine machine, Material material) {
        this.machine = machine;
        this.material = material;
    }

    public void run(Player player) {
        machine.setOutputPipeMaterial(material, player);
        player.closeInventory();
    }
}
