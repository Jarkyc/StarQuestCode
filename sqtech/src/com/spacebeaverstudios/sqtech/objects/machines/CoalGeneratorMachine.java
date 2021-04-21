package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqtech.SQTech;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CoalGeneratorMachine extends Machine {
    // static
    private static String SIGN_TEXT;
    private static int POWER_GENERATED;
    private static final ArrayList<HashMap<Vector, Material>> SCHEMAS = new ArrayList<>();

    public static void staticInitialize() {
        ConfigurationSection configSection = SQTech.getInstance().getConfig().getConfigurationSection("CoalGeneratorMachine");
        for (String text : configSection.getStringList("sign-texts")) {
            SIGN_TEXT = text;
            Machine.addSignText(text, CoalGeneratorMachine::new);
        }
        POWER_GENERATED = configSection.getInt("power-generated");

        // initialize schemas
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.FURNACE);
        schema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        SCHEMAS.add(schema);
    }

    // instance
    public CoalGeneratorMachine(Block sign) {
        super(sign);
    }

    public ArrayList<HashMap<Vector, Material>> getSchemas() {
        return SCHEMAS;
    }

    public void init() {
        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(0, ChatColor.BLUE + "Coal Generator");
        sign.setLine(1, ChatColor.RED + "Inactive");
        sign.setLine(2, "0 BV/second");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        Sign sign = (Sign) getSign().getBlock().getState();
        if (getPowerOutputPipe() != null && getPowerOutputPipe().connectedToBattery()) {
            for (ItemStack itemStack : getInventory()) {
                if (itemStack.getType() == Material.COAL || itemStack.getType() == Material.CHARCOAL) {
                    if (getPowerOutputPipe().connectedToBatteryWithSpace()) {
                        itemStack.setAmount(itemStack.getAmount() - 1);
                        if (itemStack.getAmount() == 0) {
                            getInventory().remove(itemStack);
                        }
                        getPowerOutputPipe().powerToBattery(POWER_GENERATED);
                        sign.setLine(1, ChatColor.GREEN + "Active");
                        sign.setLine(2, "+" + POWER_GENERATED + " BV/second");
                        sign.update();
                    } else {
                        sign.setLine(1, ChatColor.RED + "Battery Full");
                        sign.setLine(2, "0 BV/second");
                    }
                    sign.update();
                    return;
                }
            }
            sign.setLine(1, ChatColor.RED + "Inactive");
        } else {
            sign.setLine(1, ChatColor.RED + "Not Connected");
        }
        sign.setLine(2, "0 BV/second");
        sign.update();
    }

    public List<TransferType> getInputTypes() {
        return Collections.singletonList(TransferType.ITEMS);
    }
    public TransferType getOutputType() {
        return TransferType.POWER;
    }
    public String getMachineName() {
        return "Coal Generator";
    }
    public String getMachineInfo() {
        return "Generates BV from coal and charcoal.\n " + ChatColor.GOLD + "Speed: " + ChatColor.GRAY + "1 item/second\n "
                + ChatColor.GOLD + "Power Generation: " + ChatColor.GRAY + POWER_GENERATED + " BV/item";
    }

    public String getSignText() {
        return SIGN_TEXT;
    }
}
