package com.spacebeaverstudios.sqcore.gui;

import com.spacebeaverstudios.sqcore.gui.guifunctions.GUIFunction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GUIItem {
    private final ItemStack item;
    private final boolean hasFunction;
    private final GUIFunction function;

    public GUIItem(String name, String lore, Material itemType, GUIFunction function) {
        this.function = function;
        this.hasFunction = function != null;

        ItemStack stack = new ItemStack(itemType);
        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setDisplayName(name);
        if (lore != null) {
            List<String> loreList = GUIUtils.splitStringOverLines(lore, "", 20);
            stackMeta.setLore(loreList);
        }
        stack.setItemMeta(stackMeta);

        this.item = stack;
        GUIUtils.setWanted(item, true);
        GUIUtils.getButtons().add(this);
    }
    public GUIItem(ItemStack item, GUIFunction function) {
        this.item = item;
        this.function = function;
        this.hasFunction = function != null;
        GUIUtils.setWanted(item, true);
    }

    public ItemStack getItemStack() {
        return item;
    }

    public boolean hasFunction() {
        return hasFunction;
    }

    public void runFunction(Player player) {
        function.run(player);
    }
}
