package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqtech.SQTech;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Lightable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.util.Vector;

import java.util.*;

public class SmelterMachine extends Machine {
    // static
    private static String SIGN_TEXT;
    private static int POWER_COST;
    private static int COOLDOWN;
    private static final ArrayList<HashMap<Vector, Material>> SCHEMAS = new ArrayList<>();
    private static final HashMap<Material, Material> recipes = new HashMap<>();

    public static void staticInitialize() {
        ConfigurationSection configSection = SQTech.getInstance().getConfig().getConfigurationSection("SmelterMachine");
        for (String text : configSection.getStringList("sign-texts")) {
            SIGN_TEXT = text;
            Machine.addSignText(text, SmelterMachine::new);
        }
        POWER_COST = configSection.getInt("power-cost");
        COOLDOWN = configSection.getInt("cooldown");

        // initialize recipes
        for (Iterator<Recipe> it = Bukkit.recipeIterator(); it.hasNext(); ) {
            Recipe recipe = it.next();
            if (recipe instanceof FurnaceRecipe) {
                FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;
                for (Material material : ((RecipeChoice.MaterialChoice) furnaceRecipe.getInputChoice()).getChoices()) {
                    recipes.put(material, furnaceRecipe.getResult().getType());
                }
            }
        }

        // initialize schemas
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.FURNACE);
        schema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        SCHEMAS.add(schema);
    }

    // instance
    private int smeltCooldown = 0;

    public SmelterMachine(Block sign) {
        super(sign);
    }

    public ArrayList<HashMap<Vector, Material>> getSchemas() {
        return SCHEMAS;
    }

    public void init() {
        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(0, ChatColor.BLUE + "Smelter");
        sign.setLine(1, ChatColor.RED + "Inactive");
        sign.setLine(2, "0 BV/second");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        Sign sign = (Sign) getSign().getBlock().getState();
        Lightable furnace = (Lightable) getSign().getBlock()
                .getRelative(((Directional) getSign().getBlock().getBlockData()).getFacing().getOppositeFace()).getBlockData();

        if (getAvailablePower() >= POWER_COST) {
            ItemStack smeltable = null;
            for (ItemStack stack : getInventory()) {
                if (recipes.containsKey(stack.getType())) {
                    smeltable = stack;
                    break;
                }
            }

            if (smeltable != null) {
                smeltCooldown++;
                sign.setLine(1, ChatColor.GREEN + "Active");
                sign.setLine(2, "-" + (POWER_COST / COOLDOWN) + " BV/second");
                sign.setLine(3, "[" + ChatColor.GREEN + (new String(new char[smeltCooldown * 2])).replace("\0", "|")
                        + ChatColor.RED + (new String(new char[(COOLDOWN - smeltCooldown) * 2])).replace("\0", "|")
                        + ChatColor.BLACK + "]");
                furnace.setLit(true);

                if (smeltCooldown == COOLDOWN) {
                    smeltCooldown = 0;
                    tryUsePower(POWER_COST);
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
                sign.setLine(3, "");
                furnace.setLit(false);
            }
        } else {
            smeltCooldown = 0;
            sign.setLine(1, ChatColor.RED + "No Power");
            sign.setLine(2, "0 BV/second");
            sign.setLine(3, "");
            furnace.setLit(false);
        }
        sign.update();
        getSign().getBlock().getRelative(((Directional) getSign().getBlock().getBlockData()).getFacing().getOppositeFace())
                .setBlockData(furnace);
    }

    public List<TransferType> getInputTypes() {
        return Arrays.asList(TransferType.ITEMS, TransferType.POWER);
    }
    public TransferType getOutputType() {
        return TransferType.ITEMS;
    }
    public String getMachineName() {
        return "Smelter";
    }
    public String getMachineInfo() {
        return "Smelts items like a furnace.\n " + ChatColor.GOLD + "Speed: " + ChatColor.GRAY + "1 item/" + COOLDOWN + " second\n "
                + ChatColor.GOLD + "Power Cost: " + ChatColor.GRAY + POWER_COST + " BV/item";
    }

    public String getSignText() {
        return SIGN_TEXT;
    }
}
