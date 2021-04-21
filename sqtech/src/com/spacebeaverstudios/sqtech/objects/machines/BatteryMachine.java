package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import com.spacebeaverstudios.sqtech.SQTech;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BatteryMachine extends Machine {
    // static
    private static String SIGN_TEXT;
    private static int SMALL_STORAGE;
    private static int MEDIUM_STORAGE;
    private static int LARGE_STORAGE;
    private static final ArrayList<HashMap<Vector, Material>> SCHEMAS = new ArrayList<>();

    public static void staticInitialize() {
        ConfigurationSection configSection = SQTech.getInstance().getConfig().getConfigurationSection("BatteryMachine");
        for (String text : configSection.getStringList("sign-texts")) {
            SIGN_TEXT = text;
            Machine.addSignText(text, BatteryMachine::new);
        }
        SMALL_STORAGE = configSection.getInt("small-storage");
        MEDIUM_STORAGE = configSection.getInt("medium-storage");
        LARGE_STORAGE = configSection.getInt("large-storage");

        // initialize schemas
        // small battery
        HashMap<Vector, Material> smallSchema = new HashMap<>();
        smallSchema.put(new Vector(1, 0, 0), Material.REDSTONE_BLOCK);
        smallSchema.put(new Vector(2, 0, 0), Material.LAPIS_BLOCK);
        SCHEMAS.add(smallSchema);

        // medium battery
        // yes I am aware that this for loop is an abomination
        for (int signY = 0; signY < 2; signY++) {
            for (int signZ = 0; signZ < 2; signZ++) {
                HashMap<Vector, Material> mediumSchema = new HashMap<>();
                for (int x = 1; x < 3; x++) {
                    for (int y = 0; y < 2; y++) {
                        for (int z = 0; z < 2; z++) {
                            mediumSchema.put(new Vector(x, y - signY, z - signZ), Material.REDSTONE_BLOCK);
                        }
                    }
                }
                mediumSchema.put(new Vector(3, 0, 0), Material.LAPIS_BLOCK);
                SCHEMAS.add(mediumSchema);
            }
        }

        // large battery
        HashMap<Vector, Material> largeSchema = new HashMap<>();
        for (int x = 1; x < 4; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    largeSchema.put(new Vector(x, y, z), Material.REDSTONE_BLOCK);
                }
            }
        }
        largeSchema.put(new Vector(4, 0, 0), Material.LAPIS_BLOCK);
        SCHEMAS.add(largeSchema);
    }

    // instance
    // TODO: some way to not have all the power vaporise when machine broken
    private int STORAGE;
    private int power = 0;

    public int getStorage() {
        return STORAGE;
    }
    public int getPower() {
        return power;
    }
    public void setPower(Integer power) {
        this.power = power;
    }

    public BatteryMachine(Block sign) {
        super(sign);
    }

    public ArrayList<HashMap<Vector, Material>> getSchemas() {
        return SCHEMAS;
    }

    public void init() {
        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(0, ChatColor.BLUE + "Battery");
        sign.setLine(1, "0 BV");
        sign.setLine(2, "0% full");
        sign.setLine(3, "");
        sign.update();

        // determine storage
        BlockFace opposite = ((Directional) getSign().getBlock().getBlockData()).getFacing().getOppositeFace();
        if (getSign().getBlock().getRelative(opposite, 2).getType() == Material.LAPIS_BLOCK) {
            STORAGE = SMALL_STORAGE;
        } else if (getSign().getBlock().getRelative(opposite, 3).getType() == Material.LAPIS_BLOCK) {
            STORAGE = MEDIUM_STORAGE;
        } else {
            STORAGE = LARGE_STORAGE;
        }
    }

    public void tick() {
        if (power > STORAGE) {
            SQTech.getInstance().getLogger().warning(DiscordUtils.tag("blankman") + " BatteryMachine at "
                    + getSign().getWorld().getName() + ", " + getSign().getBlockX() + ", " + getSign().getBlockY() + ", "
                    + getSign().getBlockZ() + " has overflowed power!");
            power = STORAGE;
        }

        Sign sign = (Sign) getSign().getBlock().getState();
        sign.setLine(1, (new DecimalFormat("#,###")).format(power) + " BV");
        int percentOutOf25 = Math.round(((power * 1f) / STORAGE) * 25);
        sign.setLine(2, "[" + ChatColor.GREEN + (new String(new char[percentOutOf25])).replace("\0", "|")
                + ChatColor.RED + (new String(new char[25 - percentOutOf25])).replace("\0", "|") + ChatColor.BLACK + "]");
        sign.update();
    }

    public List<TransferType> getInputTypes() {
        return Collections.singletonList(TransferType.POWER);
    }
    public TransferType getOutputType() {
        return TransferType.POWER;
    }
    public String getMachineName() {
        return "Battery";
    }
    public String getMachineInfo() {
        return "Stores BV.\n " + ChatColor.GOLD + "Power: " + ChatColor.AQUA + (new DecimalFormat("#,###")).format(power)
                + " BV\n " + ChatColor.GOLD + "Max Storage: " + ChatColor.AQUA + (new DecimalFormat("#,###")).format(STORAGE)
                + " BV";
    }

    public String getSignText() {
        return SIGN_TEXT;
    }
    public String getCustomSaveText() {
        return String.valueOf(power);
    }
    public void loadCustomSaveText(String text) {
        power = Integer.parseInt(text);
    }
}
