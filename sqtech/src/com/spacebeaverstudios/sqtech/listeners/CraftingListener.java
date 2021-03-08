package com.spacebeaverstudios.sqtech.listeners;

import com.spacebeaverstudios.sqtech.guis.guifunctions.ChooseCrafterMachineRecipeGUIFunction;
import com.spacebeaverstudios.sqtech.objects.machines.CrafterMachine;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ComplexRecipe;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CraftingListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onItemCraft(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        HashMap<Player, CrafterMachine> playersCrafting = ChooseCrafterMachineRecipeGUIFunction.getPlayersCrafting();

        if (playersCrafting.containsKey(player)) {
            event.setCancelled(true);
            if (event.getRecipe() instanceof ComplexRecipe) {
                player.sendMessage(ChatColor.RED + "This recipe cannot be used in the auto crafter!");
                return;
            }
            CrafterMachine machine = playersCrafting.get(player);
            machine.setOutputItemStack(event.getRecipe().getResult());
            machine.getInputItems().clear();
            for (Material material : machine.getPotentialInputItems().keySet()) {
                machine.getInputItems().put(material, machine.getPotentialInputItems().get(material));
            }
            player.closeInventory();
            player.sendMessage(ChatColor.GREEN + "Recipe set!");
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        Player player = (Player) event.getViewers().get(0);
        HashMap<Player, CrafterMachine> playersCrafting = ChooseCrafterMachineRecipeGUIFunction.getPlayersCrafting();

        if (playersCrafting.containsKey(player) && !event.isRepair()) {
            HashMap<Material, Integer> inputItems = playersCrafting.get(player).getPotentialInputItems();
            inputItems.clear();
            for (ItemStack itemStack : event.getInventory().getMatrix()) {
                if (itemStack != null) {
                    inputItems.put(itemStack.getType(), inputItems.getOrDefault(itemStack.getType(), 0)+1);
                }
            }
        }
    }
}
