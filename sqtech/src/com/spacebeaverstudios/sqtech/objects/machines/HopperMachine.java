package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqtech.SQTech;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HopperMachine extends Machine {
    // static
    private static String SIGN_TEXT;
    private static final ArrayList<HashMap<Vector, Material>> SCHEMAS = new ArrayList<>();

    public static void staticInitialize() {
        ConfigurationSection configSection = SQTech.getInstance().getConfig().getConfigurationSection("HopperMachine");
        for (String text : configSection.getStringList("sign-texts")) {
            SIGN_TEXT = text;
            Machine.addSignText(text, HopperMachine::new);
        }

        // initialize schemas
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.HOPPER);
        schema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        SCHEMAS.add(schema);
    }

    // instance
    private Inventory hopperInventory;

    public Inventory getHopperInventory() {
        return hopperInventory;
    }

    public HopperMachine(Block sign) {
        super(sign);
    }

    public ArrayList<HashMap<Vector, Material>> getSchemas() {
        return SCHEMAS;
    }

    public void init() {
        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(0, ChatColor.BLUE + "Hopper Input/Output");
        sign.setLine(1, "");
        sign.setLine(2, "");
        sign.setLine(3, "");
        sign.update();

        hopperInventory = ((Hopper) sign.getBlock().getRelative(((Directional) sign.getBlock().getBlockData())
                .getFacing().getOppositeFace()).getState()).getInventory();
    }

    public void tick() {
        if (hopperInventory.getViewers().size() == 0 && getItemOutputPipe() != null) {
            ItemStack[] contents = hopperInventory.getContents();
            for (int i = hopperInventory.getSize() - 1; i >= 0; i--) {
                if (contents[i] != null) {
                    hopperInventory.setItem(i, null);
                    tryOutput(contents[i]);
                    break;
                }
            }
        }
    }

    @Override
    public ItemStack tryAddItemStack(ItemStack itemStack) {
        HashMap<Integer, ItemStack> left = hopperInventory.addItem(itemStack);
        if (left.size() != 0) {
            for (ItemStack stack : left.values()) {
                return stack;
            }
        }
        return new ItemStack(itemStack.getType(), 0);
    }

    public List<TransferType> getInputTypes() {
        return Collections.singletonList(TransferType.ITEMS);
    }
    public TransferType getOutputType() {
        return TransferType.ITEMS;
    }
    public String getMachineName() {
        return "Hopper Input/Output";
    }
    public String getMachineInfo() {
        return "Allows pipe input/output to and from the attached hopper.";
    }

    public String getSignText() {
        return SIGN_TEXT;
    }
}
