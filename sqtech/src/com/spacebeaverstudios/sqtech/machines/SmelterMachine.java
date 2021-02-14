package com.spacebeaverstudios.sqtech.machines;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;

public class SmelterMachine extends Machine {
    private int smeltCooldown = 0;

    public SmelterMachine(Block sign) {
        super(sign, "Test Machine");
    }

    public HashMap<Vector, Material> getSchema() {
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.FURNACE);
        schema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        return schema;
    }

    public void init() {
        Machine.getMachines().add(this);
        Sign sign = (Sign) this.getSign().getWorld().getBlockAt(this.getSign()).getState();
        sign.setLine(0, "");
        sign.setLine(1, ChatColor.BLUE + "Smelter");
        sign.setLine(2, ChatColor.RED + "Inactive");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        if (getInventory().size() != 0) {
            smeltCooldown++;
            if (smeltCooldown == 5) {
                smeltCooldown = 0;
                for (ItemStack stack : getInventory()) {
                    for (Iterator<Recipe> it = Bukkit.recipeIterator(); it.hasNext(); ) {
                        Recipe recipe = it.next();
                        if (recipe instanceof FurnaceRecipe) {
                            if (((FurnaceRecipe) recipe).getInput().getType().equals(stack.getType())) {
                                stack.setAmount(stack.getAmount()-1);
                                if (stack.getAmount() == 0) getInventory().remove(stack);
                                tryOutput(recipe.getResult());
                                return;
                            }
                        }
                    }
                }
            }
        }
        else smeltCooldown = 0;
    }

    public String getMachineInfo() {
        return "";
    }
}
