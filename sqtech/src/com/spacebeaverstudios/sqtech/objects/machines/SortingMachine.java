package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.utils.RotationUtils;
import com.spacebeaverstudios.sqtech.SQTech;
import com.spacebeaverstudios.sqtech.guis.FiltersListGUI;
import com.spacebeaverstudios.sqtech.guis.guifunctions.OpenMachineGUIFunction;
import com.spacebeaverstudios.sqtech.objects.SortingFilter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class SortingMachine extends Machine {
    // static
    private static String SIGN_TEXT;
    private static final ArrayList<HashMap<Vector, Material>> SCHEMAS = new ArrayList<>();

    public static void staticInitialize() {
        ConfigurationSection configSection = SQTech.getInstance().getConfig().getConfigurationSection("SortingMachine");
        for (String text : configSection.getStringList("sign-texts")) {
            SIGN_TEXT = text;
            Machine.addSignText(text, SortingMachine::new);
        }

        // initialize schemas
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new Vector(1, 0, 0), Material.LAPIS_BLOCK);
        schema.put(new Vector(1, -1, 0), Material.HOPPER);
        SCHEMAS.add(schema);
    }

    // instance
    public final SortingFilter[] filters = {new SortingFilter(), new SortingFilter(), new SortingFilter(), new SortingFilter(),
            new SortingFilter(), new SortingFilter(), new SortingFilter(), new SortingFilter(), new SortingFilter()};
    private final ArrayList<Block> hoppers = new ArrayList<>();
    private int hoppersCalculatedWhen = 0;

    public SortingMachine(Block sign) {
        super(sign);
    }

    public ArrayList<HashMap<Vector, Material>> getSchemas() {
        return SCHEMAS;
    }

    public void init() {
        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(0, ChatColor.BLUE + "Sorter");
        sign.setLine(1, "");
        sign.setLine(2, "");
        sign.setLine(3, "");
        sign.update();
    }

    public void tick() {
        // all functionality is in tryAddItemStack()
    }

    public ArrayList<Block> getHoppers() {
        // only calculate hoppers once per second
        if (hoppersCalculatedWhen < Bukkit.getCurrentTick()) {
            hoppersCalculatedWhen = Bukkit.getCurrentTick();
            hoppers.clear();
            BlockFace signDirection = ((Directional) getSign().getBlock().getBlockData()).getFacing();
            Block checking = getSign().getBlock().getRelative(signDirection.getOppositeFace()).getRelative(BlockFace.DOWN);

            while (checking.getType() == Material.HOPPER && hoppers.size() < 9) {
                hoppers.add(checking.getLocation().getBlock()); // roundabout cloning
                checking = checking.getRelative(RotationUtils.rotateLeft(signDirection));
            }
        }
        return hoppers;
    }

    @Override
    public ItemStack tryAddItemStack(ItemStack itemStack) {
        ArrayList<Block> hoppers = getHoppers();
        for (int j = 0; j < hoppers.size(); j++) {
            if (filters[j].itemFits(itemStack.getType())) {
                ItemStack left = ((Hopper) hoppers.get(j).getState()).getInventory().addItem(itemStack).get(0);
                if (left == null) {
                    return new ItemStack(itemStack.getType(), 0);
                } else {
                    itemStack = left;
                }
            }
        }
        return itemStack;
    }

    public List<TransferType> getInputTypes() {
        return Collections.singletonList(TransferType.ITEMS);
    }
    public TransferType getOutputType() {
        return null;
    }
    public String getMachineName() {
        return "Sorter";
    }
    public String getMachineInfo() {
        return "Sorts items into up to 9 hoppers. Sorting is done starting with the leftmost hopper.";
    }

    @Override
    public GUIItem getCustomOptionsGUIItem() {
        return new GUIItem("Set Filters", null, Material.HOPPER,
                new OpenMachineGUIFunction(new FiltersListGUI(this), this));
    }

    public String getSignText() {
        return SIGN_TEXT;
    }
    public String getCustomSaveText() {
        StringBuilder str = new StringBuilder();
        for (SortingFilter filter : filters) {
            str.append(";").append(filter.getSaveString());
        }
        return str.toString();
    }
    public void loadCustomSaveText(String str) {
        String[] split = str.split(";");
        for (int i = 0; i < filters.length; i++) {
            filters[i].loadFromString(split[i + 1]);
        }
    }
}
