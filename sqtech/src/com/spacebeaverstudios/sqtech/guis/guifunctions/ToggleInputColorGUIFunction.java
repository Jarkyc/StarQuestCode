package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import com.spacebeaverstudios.sqtech.SQTech;
import com.spacebeaverstudios.sqtech.guis.ChooseInputColorsGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ToggleInputColorGUIFunction extends GUIFunction {
    private final Material glassMaterial;

    public ToggleInputColorGUIFunction(Material glassMaterial) {
        this.glassMaterial = glassMaterial;
    }

    public void run(Player player) {
        GUI gui = GUI.getGuis().get(player);
        if (gui instanceof ChooseInputColorsGUI) ((ChooseInputColorsGUI) gui).toggle(glassMaterial);
        else SQTech.getInstance().getLogger().warning(DiscordUtils.tag("blankman") + " !(gui instanceof ChooseInputColorsGUI)");
    }
}
