package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.guis.FiltersListGUI;
import com.spacebeaverstudios.sqtech.objects.SortingFilter;
import org.bukkit.entity.Player;

public class ToggleWhitelistGUIFunction extends GUIFunction {
    private final FiltersListGUI gui;
    private final SortingFilter filter;

    public ToggleWhitelistGUIFunction(FiltersListGUI gui, SortingFilter filter) {
        this.gui = gui;
        this.filter = filter;
    }

    public void run(Player player) {
        filter.whitelist = !filter.whitelist;
        gui.refreshInventory();
    }
}
