package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqtech.guis.guifunctions.ChooseCrafterMachineRecipeGUIFunction;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CrafterMachine extends Machine {
    private final HashMap<Material, Integer> potentialInputItems = new HashMap<>();
    private final HashMap<Material, Integer> inputItems = new HashMap<>();
    private ItemStack outputItemStack = null;

    public CrafterMachine(Block sign) {
        super(sign, "Auto Crafter", "Automatically crafts items.\n " + ChatColor.GOLD + "Speed: "
                + ChatColor.GRAY + "1 craft/second\n " + ChatColor.GOLD + "Power Usage: " + ChatColor.GRAY + "10 BV/craft");
    }

    public HashMap<Vector, Material> getSchema() {
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.CRAFTING_TABLE);
        schema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        return schema;
    }

    public void init() {
        Sign sign = (Sign) getSign().getWorld().getBlockAt(this.getSign()).getState();
        sign.setLine(0, ChatColor.BLUE + "Auto Crafter");
        sign.setLine(1, ChatColor.RED + "Inactive");
        sign.setLine(2, "0 BV/second");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        Sign sign = (Sign) getSign().getWorld().getBlockAt(this.getSign()).getState();
        if (outputItemStack != null) {
            @SuppressWarnings("unchecked")
            HashMap<Material, Integer> deductibleInputs = (HashMap<Material, Integer>) inputItems.clone();
            ArrayList<ItemStack> newInventory = new ArrayList<>();
            for (ItemStack itemStack : getInventory()) {
                newInventory.add(new ItemStack(itemStack.getType(), itemStack.getAmount()));
            }

            for (ItemStack itemStack : newInventory) {
                if (deductibleInputs.containsKey(itemStack.getType())) {
                    if (deductibleInputs.get(itemStack.getType()) <= itemStack.getAmount()) {
                        itemStack.setAmount(itemStack.getAmount()-deductibleInputs.get(itemStack.getType()));
                        deductibleInputs.put(itemStack.getType(), 0);
                    } else {
                        deductibleInputs.put(itemStack.getType(), deductibleInputs.get(itemStack.getType())-itemStack.getAmount());
                        itemStack.setAmount(0);
                    }
                }
            }

            for (int i = 0; i < newInventory.size(); i++) {
                if (newInventory.get(i).getAmount() == 0) {
                    newInventory.remove(i);
                    i--;
                }
            }

            boolean canCraft = true;
            for (Integer num : deductibleInputs.values()) {
                if (num != 0) {
                    canCraft = false;
                    break;
                }
            }

            if (canCraft) {
                if (tryUsePower(10)) {
                    getInventory().clear();
                    getInventory().addAll(newInventory);
                    tryOutput(new ItemStack(outputItemStack.getType(), outputItemStack.getAmount()));
                    sign.setLine(1, ChatColor.GREEN + "Active");
                    sign.setLine(2, "-10 BV/second");
                } else {
                    sign.setLine(1, ChatColor.RED + "No Power");
                    sign.setLine(2, "0 BV/second");
                }
            } else {
                sign.setLine(1, ChatColor.RED + "Inactive");
                sign.setLine(2, "0 BV/second");
            }
        } else {
            sign.setLine(1, ChatColor.RED + "No Recipe");
            sign.setLine(2, "0 BV/second");
        }
        sign.update();
    }

    public HashMap<Material, Integer> getPotentialInputItems() {
        return potentialInputItems;
    }
    public HashMap<Material, Integer> getInputItems() {
        return inputItems;
    }

    public void setOutputItemStack(ItemStack outputItemStack) {
        this.outputItemStack = outputItemStack;
    }

    public List<TransferType> getInputTypes() {
        return Arrays.asList(TransferType.ITEMS, TransferType.POWER);
    }
    public TransferType getOutputType() {
        return TransferType.ITEMS;
    }

    @Override
    public GUIItem getCustomOptionsGUIItem() {
        return new GUIItem("Choose Crafting Recipe", ChatColor.GRAY + "Craft the item you want the machine to craft.\n "
                + ChatColor.GOLD + "Current Recipe: " + (outputItemStack == null ? ChatColor.GRAY + "None"
                : ChatColor.AQUA + WordUtils.capitalizeFully(outputItemStack.getType().toString().replace("_", " "))),
                Material.CRAFTING_TABLE, new ChooseCrafterMachineRecipeGUIFunction(this));
    }

    public String getSignText() {
        return "[crafter]";
    }
    @Override
    public String getCustomSaveText() {
        if (outputItemStack == null) {
            return "0";
        } else {
            StringBuilder text = new StringBuilder(outputItemStack.getType().toString() + ":" + outputItemStack.getAmount() + ";");
            for (Material input : inputItems.keySet()) {
                text.append(input.toString()).append(":").append(inputItems.get(input)).append(";");
            }
            return text.toString();
        }
    }
    @Override
    public void loadCustomSaveText(String text) {
        String[] textSplit = text.split(";");

        String[] outputSplit = textSplit[0].split(":");
        outputItemStack = new ItemStack(Material.getMaterial(outputSplit[0]), Integer.parseInt(outputSplit[1]));

        for (int i = 1; i < textSplit.length; i++) {
            String[] inputSplit = textSplit[i].split(":");
            inputItems.put(Material.getMaterial(inputSplit[0]), Integer.parseInt(inputSplit[1]));
        }
    }
}
