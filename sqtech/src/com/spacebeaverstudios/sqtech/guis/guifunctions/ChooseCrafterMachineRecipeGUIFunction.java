package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.SQTech;
import com.spacebeaverstudios.sqtech.objects.machines.CrafterMachine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ChooseCrafterMachineRecipeGUIFunction extends GUIFunction {
    // static
    private static final HashMap<Player, CrafterMachine> playersCrafting = new HashMap<>();

    public static HashMap<Player, CrafterMachine> getPlayersCrafting() {
        return playersCrafting;
    }

    // instance
    private final CrafterMachine machine;

    public ChooseCrafterMachineRecipeGUIFunction(CrafterMachine machine) {
        this.machine = machine;
    }

    public void run(Player player) {
        player.openWorkbench(null, true);
        playersCrafting.put(player, machine);
        Bukkit.getScheduler().runTaskLater(SQTech.getInstance(), () -> machine.setGUIPlayer(player), 1);
    }
}
