package com.spacebeaverstudios.sqbaseclasses.gui.guifunctions;

import com.spacebeaverstudios.sqbaseclasses.gui.GUI;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class OpenGUIFunction extends GUIFunction {
    private final GUI gui;

    public OpenGUIFunction(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void run(Player player) {
        gui.open(player);
    }
}
