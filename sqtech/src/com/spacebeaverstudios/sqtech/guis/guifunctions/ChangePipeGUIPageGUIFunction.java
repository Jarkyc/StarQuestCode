package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.guis.PipeGUI;
import org.bukkit.entity.Player;

public class ChangePipeGUIPageGUIFunction extends GUIFunction {
    private final PipeGUI gui;
    private final int page;

    public ChangePipeGUIPageGUIFunction(PipeGUI gui, int page) {
        this.gui = gui;
        this.page = page;
    }

    @Override
    public void run(Player player) {
        gui.goToPage(page);
    }
}
