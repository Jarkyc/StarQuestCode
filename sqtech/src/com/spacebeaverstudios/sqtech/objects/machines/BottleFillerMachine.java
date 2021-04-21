package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqtech.SQTech;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import java.util.*;

public class BottleFillerMachine extends Machine {
    // static
    private static String SIGN_TEXT;
    private static int POWER_COST;
    private static final ArrayList<HashMap<Vector, Material>> SCHEMAS = new ArrayList<>();

    public static void staticInitialize() {
        ConfigurationSection configSection = SQTech.getInstance().getConfig().getConfigurationSection("BottleFillerMachine");
        for (String text : configSection.getStringList("sign-texts")) {
            SIGN_TEXT = text;
            Machine.addSignText(text, BottleFillerMachine::new);
        }
        POWER_COST = configSection.getInt("power-cost");

        // intialize schemas
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.CAULDRON);
        schema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        SCHEMAS.add(schema);
    }

    // instance
    public BottleFillerMachine(Block sign) {
        super(sign);
    }

    public ArrayList<HashMap<Vector, Material>> getSchemas() {
        return SCHEMAS;
    }

    public void init() {
        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(0, ChatColor.BLUE + "Bottle Filler");
        sign.setLine(1, ChatColor.RED + "Inactive");
        sign.setLine(2, "0 BV/second");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        Sign sign = (Sign) getSign().getBlock().getState();
        Levelled cauldron = (Levelled) getSign().getBlock().getRelative(
                ((Directional) getSign().getBlock().getBlockData()).getFacing().getOppositeFace()).getBlockData();
        if (cauldron.getLevel() == cauldron.getMaximumLevel()) {
            for (ItemStack itemStack : getInventory()) {
                if (itemStack.getType() == Material.GLASS_BOTTLE) {
                    if (tryUsePower(POWER_COST)) {
                        itemStack.setAmount(itemStack.getAmount() - 1);
                        if (itemStack.getAmount() == 0) {
                            getInventory().remove(itemStack);
                        }

                        ItemStack waterBottle = new ItemStack(Material.POTION, 1);
                        PotionMeta waterBottleMeta = (PotionMeta) waterBottle.getItemMeta();
                        waterBottleMeta.setBasePotionData(new PotionData(PotionType.WATER));
                        waterBottle.setItemMeta(waterBottleMeta);
                        tryOutput(waterBottle);

                        sign.setLine(1, ChatColor.GREEN + "Active");
                        sign.setLine(2, "-" + POWER_COST + " BV/second");
                    } else {
                        sign.setLine(1, ChatColor.RED + "No Power");
                        sign.setLine(2, "0 BV/second");
                    }
                    sign.update();
                    return;
                }
            }
            // no bottles
            sign.setLine(1, ChatColor.RED + "Inactive");
        } else {
            sign.setLine(1, ChatColor.RED + "No Water");
        }
        sign.setLine(2, "0 BV/second");
        sign.update();
    }

    public List<TransferType> getInputTypes() {
        return Arrays.asList(TransferType.ITEMS, TransferType.POWER);
    }
    public TransferType getOutputType() {
        return TransferType.ITEMS;
    }
    public String getMachineName() {
        return "Bottle Filler";
    }
    public String getMachineInfo() {
        return "Fills glass bottles with water.\n " + ChatColor.GOLD + "Speed: " + ChatColor.GRAY + "1 item/second\n "
                + ChatColor.GOLD + "Power Cost: " + ChatColor.GRAY + POWER_COST + " BV/item";
    }

    public String getSignText() {
        return SIGN_TEXT;
    }
}
