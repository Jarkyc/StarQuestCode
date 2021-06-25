package com.spacebeaverstudios.sqtech.guis.guifunctions;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import com.spacebeaverstudios.sqtech.guis.FilterItemsGUI;
import com.spacebeaverstudios.sqtech.objects.SortingFilter;
import org.bukkit.entity.Player;

public class RemoveFromFilterGUIFunction extends GUIFunction {
    private final FilterItemsGUI gui;
    private final SortingFilter filter;
    private final int i;

    public RemoveFromFilterGUIFunction(FilterItemsGUI gui, SortingFilter filter, int i) {
        this.gui = gui;
        this.filter = filter;
        this.i = i;
    }

    public void run(Player player) {
        filter.items.remove(i);
        gui.refresh();
    }
}
