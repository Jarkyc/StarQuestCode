package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqtech.SQTech;
import com.spacebeaverstudios.sqtech.guis.ChooseBrewingRecipeGUI;
import com.spacebeaverstudios.sqtech.guis.guifunctions.OpenMachineGUIFunction;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.util.Vector;

import java.util.*;

public class BrewerMachine extends Machine {
    private PotionData inputPotionData = null;
    private PotionData outputPotionData = null;
    private Material ingredient = null;
    private Integer brewCooldown = 0;

    public BrewerMachine(Block sign) {
        super(sign);
    }

    public HashMap<Vector, Material> getSchema() {
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new org.bukkit.util.Vector(1, 0, 0), Material.BREWING_STAND);
        schema.put(new org.bukkit.util.Vector(2, 0, 0), Material.LAPIS_BLOCK);
        return schema;
    }

    public void init() {
        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(0, ChatColor.BLUE + "Brewer");
        sign.setLine(1, ChatColor.RED + "Inactive");
        sign.setLine(2, "0 BV/second");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        Sign sign = (Sign) getSign().getBlock().getState();

        if (outputPotionData != null) {
            if (getAvailablePower() > 200) {
                // TODO
            } else {
                sign.setLine(1, ChatColor.RED + "No Power");
                sign.setLine(2, "0 BV/second");
                sign.setLine(3, "");
            }
        } else {
            sign.setLine(1, ChatColor.RED + "No Recipe");
            sign.setLine(2, "0 BV/second");
            sign.setLine(3, "");
        }
        sign.update();
    }

    public void trySetRecipe(Material potentialIngredient, ItemStack potion) {
        if (potion.getType() != Material.POTION) {
            getGUIPlayer().sendMessage(ChatColor.RED + "The item you selected is not a potion, and cannot be brewed from!");
            return;
        }

        BrewingStand brewingStand = (BrewingStand) getSign().getBlock()
                .getRelative(((Directional) getSign().getBlock().getBlockData()).getFacing().getOppositeFace()).getState();
        brewingStand.getInventory().setIngredient(new ItemStack(potentialIngredient, 1));
        brewingStand.getInventory().setItem(0, potion.clone());
        brewingStand.setFuelLevel(1);
        brewingStand.setBrewingTime(1);
        brewingStand.update(); // TODO: this wipes the inventory for some reason?

        SQTech.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(SQTech.getInstance(), () -> {
            // if the brewing worked, the ingredient should be gone
            if (brewingStand.getInventory().getIngredient() == null) {
                inputPotionData = ((PotionMeta) potion.getItemMeta()).getBasePotionData();
                outputPotionData = ((PotionMeta) brewingStand.getInventory().getItem(0).getItemMeta()).getBasePotionData();
                ingredient = potentialIngredient;
                getGUIPlayer().sendMessage(ChatColor.GREEN + "The potion recipe has been set!");
            } else {
                getGUIPlayer().sendMessage(ChatColor.RED + "That is not a valid potion recipe!");
            }

            // empty the brewing stand
            brewingStand.getInventory().setIngredient(null);
            brewingStand.getInventory().setItem(0, null);
            brewingStand.setFuelLevel(0);
            brewingStand.setBrewingTime(0);
            brewingStand.update();
        }, 1);
    }

    public List<TransferType> getInputTypes() {
        return Arrays.asList(TransferType.ITEMS, TransferType.POWER);
    }
    public TransferType getOutputType() {
        return TransferType.ITEMS;
    }
    public String getMachineName() {
        return "Auto Brewer";
    }
    public String getMachineInfo() {
        return "Automatically brews potions.\n " + ChatColor.GOLD + "Speed: " + ChatColor.GRAY + "1 brew/20 seconds\n "
                + ChatColor.GOLD + "Power Cost: " + ChatColor.GRAY + "200 BV/brew";
    }

    public GUIItem getCustomOptionsGUIItem() {
        return new GUIItem("Choose Recipe", ChatColor.GRAY + "Choose the recipe for potions you want to brew.\n "
                + ChatColor.GOLD + "Current Recipe: " + (outputPotionData == null ? ChatColor.GRAY + "None" : ChatColor.AQUA
                + WordUtils.capitalizeFully(outputPotionData.getType().toString().replace("_", " "))),
                Material.BREWING_STAND, new OpenMachineGUIFunction(new ChooseBrewingRecipeGUI(this), this));
    }

    public String getSignText() {
        return "[brewer]";
    }
    public void loadCustomSaveText(String text) {
        // TODO
    }
    public String getCustomSaveText() {
        // TODO
        return "t";
    }
}
