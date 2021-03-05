package com.spacebeaverstudios.sqtech.objects.machines;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SmelterMachine extends Machine {
    // static
    private static final HashMap<Material, Material> recipes = new HashMap<>();

    public static void initializeRecipes() {
        for (Iterator<Recipe> it = Bukkit.recipeIterator(); it.hasNext(); ) {
            Recipe recipe = it.next();
            if (recipe instanceof FurnaceRecipe) {
                FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;
                for (Material material : ((RecipeChoice.MaterialChoice) furnaceRecipe.getInputChoice()).getChoices()) {
                    recipes.put(material, furnaceRecipe.getResult().getType());
                }
            }
        }
    }

    // instance
    private int smeltCooldown = 0;

    public SmelterMachine(Block sign) {
        super(sign, "Smelter", "Smelts items like a furnace.");
    }

    public HashMap<Vector, Material> getSchema() {
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.FURNACE);
        schema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        return schema;
    }

    public void init() {
        Sign sign = (Sign) getSign().getWorld().getBlockAt(this.getSign()).getState();
        sign.setLine(0, ChatColor.BLUE + "Smelter");
        sign.setLine(1, ChatColor.RED + "Inactive");
        sign.setLine(2, "0 BV/second");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        Sign sign = (Sign) getSign().getWorld().getBlockAt(this.getSign()).getState();
        if (canUsePower(50)) {
            ItemStack smeltable = null;
            for (ItemStack stack : getInventory()) {
                if (recipes.containsKey(stack.getType())) {
                    smeltable = stack;
                    break;
                }
            }

            if (smeltable != null) {
                sign.setLine(1, ChatColor.GREEN + "Active");
                sign.setLine(2, "-50 BV/5 seconds");
                smeltCooldown++;
                if (smeltCooldown == 5) {
                    smeltCooldown = 0;
                    tryUsePower(50);
                    ItemStack output = new ItemStack(recipes.get(smeltable.getType()), 1);
                    smeltable.setAmount(smeltable.getAmount()-1);
                    if (smeltable.getAmount() == 0) {
                        getInventory().remove(smeltable);
                    }
                    tryOutput(output); // separate to make ItemStack bullshit work
                }
            } else {
                smeltCooldown = 0;
                sign.setLine(1, ChatColor.RED + "Inactive");
                sign.setLine(2, "0 BV/second");
            }
        } else {
            sign.setLine(1, ChatColor.RED + "No Power");
            sign.setLine(2, "0 BV/second");
        }
        sign.update();
    }

    public List<TransferType> getInputTypes() {
        return Arrays.asList(TransferType.ITEMS, TransferType.POWER);
    }
    public TransferType getOutputType() {
        return TransferType.ITEMS;
    }
}
