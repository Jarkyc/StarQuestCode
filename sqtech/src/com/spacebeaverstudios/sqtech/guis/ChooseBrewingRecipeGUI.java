package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqtech.objects.machines.BrewerMachine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChooseBrewingRecipeGUI extends GUI {
    private final BrewerMachine machine;
    private Material ingredientChosen = null;

    public ChooseBrewingRecipeGUI(BrewerMachine machine) {
        super("Choose Brewing Recipe");
        this.machine = machine;
    }

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, getInventoryName());

        // iron bars
        for (int i = 0; i < 27; i++) {
            if (i == 13) {
                if (ingredientChosen != null) {
                    GUIItem guiItem = new GUIItem(new ItemStack(ingredientChosen, 1), null);
                    getGuiItems().add(guiItem);
                    inventory.setItem(i, guiItem.getItemStack());
                }
            } else {
                GUIItem guiItem = new GUIItem(" ", "Select the " + (ingredientChosen == null ? "ingredient" : "potion")
                        + " you want to brew with from your inventory.", Material.IRON_BARS, null);
                getGuiItems().add(guiItem);
                inventory.setItem(i, guiItem.getItemStack());
            }
        }

        return inventory;
    }

    public void selectItem(ItemStack itemStack) {
        if (ingredientChosen == null) {
            ingredientChosen = itemStack.getType();
            refreshInventory();
        } else {
            machine.trySetRecipe(ingredientChosen, itemStack);
            getPlayer().closeInventory();
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        machine.setGUIPlayer(getPlayer());
        (new MachineGUI(machine)).open(getPlayer());
    }
}
