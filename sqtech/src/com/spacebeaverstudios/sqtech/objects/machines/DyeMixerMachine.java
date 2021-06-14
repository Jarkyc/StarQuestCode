package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqtech.SQTech;
import com.spacebeaverstudios.sqtech.guis.ChooseDyeToConvertToGUI;
import com.spacebeaverstudios.sqtech.guis.guifunctions.OpenMachineGUIFunction;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DyeMixerMachine extends Machine {
    // static
    private static String SIGN_TEXT;
    private static int POWER_COST;
    private static final ArrayList<HashMap<Vector, Material>> SCHEMAS = new ArrayList<>();
    private static final ArrayList<Material> DYES = new ArrayList<>();

    public static ArrayList<Material> getDyes() {
        return DYES;
    }

    public static void staticInitialize() {
        ConfigurationSection configSection = SQTech.getInstance().getConfig().getConfigurationSection("DyeMixerMachine");
        for (String text : configSection.getStringList("sign-texts")) {
            SIGN_TEXT = text;
            Machine.addSignText(text, DyeMixerMachine::new);
        }
        POWER_COST = configSection.getInt("power-cost");
        for (String text : configSection.getStringList("dyes")) {
            DYES.add(Material.getMaterial(text));
        }

        // initialize schemas
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.LOOM);
        schema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        SCHEMAS.add(schema);
    }

    // instance
    private Material convertTo = null;

    public void setDyeToConvertTo(Material material) {
        convertTo = material;
    }

    public DyeMixerMachine(Block sign) {
        super(sign);
    }

    public ArrayList<HashMap<Vector, Material>> getSchemas() {
        return SCHEMAS;
    }

    public void init() {
        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(0, ChatColor.BLUE + "Dye Mixer");
        sign.setLine(1, ChatColor.RED + "Inactive");
        sign.setLine(2, "0 BV/second");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        Sign sign = (Sign) getSign().getBlock().getState();
        if (getAvailablePower() >= POWER_COST) {
            if (convertTo != null) {
                sign.setLine(1, ChatColor.RED + "Inactive");
                sign.setLine(2, "0 BV/second");
                for (ItemStack stack : getInventory()) {
                    if (DYES.contains(stack.getType()) && stack.getType() != convertTo) {
                        sign.setLine(1, ChatColor.GREEN + "Active");
                        sign.setLine(2, "-" + POWER_COST + " BV/second");
                        stack.setAmount(stack.getAmount() - 1);
                        if (stack.getAmount() == 0) {
                            getInventory().remove(stack);
                        }
                        tryOutput(new ItemStack(convertTo, 1));
                        break;
                    }
                }
            } else {
                sign.setLine(1, ChatColor.RED + "No Dye Chosen");
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
    public String getMachineName() {
        return "Dye Mixer";
    }
    public String getMachineInfo() {
        return "Changes dyes into other colors of dye.\n " + ChatColor.GOLD + "Speed: " + ChatColor.GRAY + "1 item/second\n "
                + ChatColor.GOLD + "Power Cost: " + ChatColor.GRAY + POWER_COST + " BV/item";
    }

    public GUIItem getCustomOptionsGUIItem() {
        return new GUIItem("Choose Dye to Convert To", ChatColor.GOLD + "Currently Selected: " +
                (convertTo == null ? ChatColor.GRAY + "None"
                        : ChatColor.AQUA + WordUtils.capitalizeFully(convertTo.toString().replace("_", " "))),
                (convertTo == null ? Material.GLASS : convertTo), new OpenMachineGUIFunction(new ChooseDyeToConvertToGUI(this), this));
    }

    public String getSignText() {
        return SIGN_TEXT;
    }
    public String getCustomSaveText() {
        if (convertTo == null) {
            return "0";
        } else {
            return convertTo.toString();
        }
    }
    public void loadCustomSaveText(String text) {
        convertTo = Material.getMaterial(text);
    }
}
