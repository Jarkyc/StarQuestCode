package com.spacebeaverstudios.sqbaseclasses.gui.guifunctions;

import com.spacebeaverstudios.sqbaseclasses.gui.ListGUI;
import org.bukkit.entity.Player;

public class ChangePageFunction extends GUIFunction {
    private final ListGUI gui;
    private final int page;

    public ChangePageFunction(ListGUI gui, int page) {
        this.gui = gui;
        this.page = page;
    }

    @Override
    public void run(Player player) {
        gui.goToPage(page);
    }
}
