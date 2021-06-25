package com.spacebeaverstudios.sqcore.gui;

import com.spacebeaverstudios.sqcore.SQCore;
import com.spacebeaverstudios.sqcore.utils.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GUI {
    private static final HashMap<Player, GUI> guis = new HashMap<>();

    public static HashMap<Player, GUI> getGuis() {
        return guis;
    }

    public static void closePlayer(Player player, Inventory inventory) {
        if (guis.containsKey(player) && guis.get(player).getInventory().equals(inventory)) {
            guis.get(player).onClose();
        }
    }

    private Player player;
    private final String inventoryName;
    private final ArrayList<GUIItem> guiItems = new ArrayList<>(); // make sure to add things here so they're saved
    private Inventory inventory;

    public GUI(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public abstract Inventory createInventory();
    @SuppressWarnings("unused")
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        // override this function to capture when a player clicks their own inventory
    }

    public void open(Player player) {
        this.player = player;
        inventory = createInventory();
        GUI gui = this;
        Bukkit.getScheduler().runTaskLater(SQCore.getInstance(), () -> {
            player.openInventory(inventory);
            guis.put(player, gui);
        }, 1);
    }

    public String getInventoryName() {
        return inventoryName;
    }
    public ArrayList<GUIItem> getGuiItems() {
        return guiItems;
    }
    public Player getPlayer() {
        return player;
    }
    public Inventory getInventory() {
        return inventory;
    }

    public void refreshInventory() {
        for (GUIItem guiItem : guiItems) {
            GUIUtils.getButtons().remove(guiItem);
        }
        guiItems.clear();
        open(player);
    }

    public void onClose() {
        guis.remove(player);
        for (GUIItem guiItem : guiItems) {
            GUIUtils.getButtons().remove(guiItem);
        }
    }
}
