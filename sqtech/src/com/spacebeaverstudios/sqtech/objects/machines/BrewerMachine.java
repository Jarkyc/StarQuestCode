package com.spacebeaverstudios.sqtech.objects.machines;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqcore.utils.GUIUtils;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import java.util.*;

public class BrewerMachine extends Machine {
    private PotionData inputPotionData = null;
    private PotionData outputPotionData = null;
    private Material ingredient = null;
    private Integer brewCooldown = 0;
    private boolean toSplash = false;
    private boolean toLingering = false;

    public BrewerMachine(Block sign) {
        super(sign);
    }

    public ArrayList<HashMap<Vector, Material>> getSchemas() {
        HashMap<Vector, Material> schema = new HashMap<>();
        schema.put(new org.bukkit.util.Vector(1, 0, 0), Material.BREWING_STAND);
        schema.put(new org.bukkit.util.Vector(2, 0, 0), Material.LAPIS_BLOCK);
        return new ArrayList<>(Collections.singletonList(schema));
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
        org.bukkit.block.data.type.BrewingStand brewingStand = (org.bukkit.block.data.type.BrewingStand) getSign().getBlock()
                .getRelative(((Directional) getSign().getBlock().getBlockData()).getFacing().getOppositeFace()).getBlockData();

        if (outputPotionData != null) {
            if (getAvailablePower() > 200) {
                ItemStack ingredientToUse = null;
                ArrayList<ItemStack> potionsToUse = new ArrayList<>();
                for (ItemStack itemStack : getInventory()) {
                    if (itemStack.getType() == ingredient) {
                        ingredientToUse = itemStack;
                    }
                    if (potionsToUse.size() < 3 && itemStack.getItemMeta() instanceof PotionMeta
                            && ((PotionMeta) itemStack.getItemMeta()).getBasePotionData().equals(inputPotionData)) {
                        potionsToUse.add(itemStack);
                    }
                }

                if (ingredientToUse != null && potionsToUse.size() > 0) {
                    brewCooldown++;
                    sign.setLine(1, ChatColor.GREEN + "Active");
                    sign.setLine(2, "-20 BV/second");
                    sign.setLine(3, "[" + (new String(new char[brewCooldown])).replace("\0", "|")
                            + (new String(new char[10 - brewCooldown])).replace("\0", ".") + "]");
                    for (int i = 0; i < 3; i++) {
                        brewingStand.setBottle(i, potionsToUse.size() >= i);
                    }

                    if (brewCooldown == 10) {
                        brewCooldown = 0;
                        tryUsePower(200);
                        Material material;
                        if (toSplash) {
                            material = Material.SPLASH_POTION;
                        } else if (toLingering) {
                            material = Material.LINGERING_POTION;
                        } else {
                            material = Material.POTION;
                        }
                        ItemStack output = new ItemStack(material, 1);
                        PotionMeta outputMeta = (PotionMeta) output.getItemMeta();
                        outputMeta.setBasePotionData(outputPotionData);
                        output.setItemMeta(outputMeta);

                        for (ItemStack itemStack : potionsToUse) {
                            getInventory().remove(itemStack);
                            tryOutput(output.clone()); // do it here to get the proper number
                        }
                        ingredientToUse.setAmount(ingredientToUse.getAmount() - 1);
                        if (ingredientToUse.getAmount() == 0) {
                            getInventory().remove(ingredientToUse);
                        }
                    }
                } else {
                    brewCooldown = 0;
                    sign.setLine(1, ChatColor.RED + "Inactive");
                    sign.setLine(2, "0 BV/second");
                    sign.setLine(3, "");
                }
            } else {
                brewCooldown = 0;
                sign.setLine(1, ChatColor.RED + "No Power");
                sign.setLine(2, "0 BV/second");
                sign.setLine(3, "");
            }
        } else {
            brewCooldown = 0;
            sign.setLine(1, ChatColor.RED + "No Recipe");
            sign.setLine(2, "0 BV/second");
            sign.setLine(3, "");
        }
        sign.update();
    }

    public void trySetRecipe(Material potentialIngredient, ItemStack potion, Player player) {
        if (potion.getType() == Material.SPLASH_POTION && potentialIngredient == Material.DRAGON_BREATH) {
            inputPotionData = ((PotionMeta) potion.getItemMeta()).getBasePotionData();
            outputPotionData = ((PotionMeta) potion.getItemMeta()).getBasePotionData();
            ingredient = potentialIngredient;
            toSplash = false;
            toLingering = true;
            player.sendMessage(ChatColor.GREEN + "The potion recipe has been set!");
            return;
        } else if (potion.getType() != Material.POTION) {
            player.sendMessage(ChatColor.RED + "The item you selected is not a potion, and cannot be brewed from!");
            return;
        }

        if (potentialIngredient == Material.GUNPOWDER) {
            inputPotionData = ((PotionMeta) potion.getItemMeta()).getBasePotionData();
            outputPotionData = ((PotionMeta) potion.getItemMeta()).getBasePotionData();
            ingredient = potentialIngredient;
            toSplash = true;
            toLingering = false;
            player.sendMessage(ChatColor.GREEN + "The potion recipe has been set!");
            return;
        }

        BrewingStand brewingStand = (BrewingStand) getSign().getBlock()
                .getRelative(((Directional) getSign().getBlock().getBlockData()).getFacing().getOppositeFace()).getState();
        brewingStand.setFuelLevel(1);
        brewingStand.setBrewingTime(1);
        brewingStand.update();
        brewingStand.getInventory().setIngredient(new ItemStack(potentialIngredient, 1));
        brewingStand.getInventory().setItem(0, potion.clone());

        SQTech.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(SQTech.getInstance(), () -> {
            // if the brewing worked, the ingredient should be gone
            if (brewingStand.getInventory().getIngredient() == null) {
                inputPotionData = ((PotionMeta) potion.getItemMeta()).getBasePotionData();
                outputPotionData = ((PotionMeta) brewingStand.getInventory().getItem(0).getItemMeta()).getBasePotionData();
                ingredient = potentialIngredient;
                toSplash = false;
                toLingering = false;
                player.sendMessage(ChatColor.GREEN + "The potion recipe has been set!");
            } else {
                player.sendMessage(ChatColor.RED + "That is not a valid potion recipe!");
            }

            // empty the brewing stand
            brewingStand.getInventory().setIngredient(null);
            brewingStand.getInventory().setItem(0, null);
            brewingStand.setFuelLevel(0);
            brewingStand.setBrewingTime(0);
            brewingStand.update();

            // refresh MachineGUI to display potion properly
            if (GUI.getGuis().containsKey(player)) {
                GUI.getGuis().get(player).refreshInventory();
            }
        }, 2);
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
        String potionName;
        if (outputPotionData == null) {
            potionName = ChatColor.GRAY + "None";
        } else {
            // prefix
            if (toSplash) {
                potionName = ChatColor.AQUA + "Splash Potion of ";
            } else if (toLingering) {
                potionName = ChatColor.AQUA + "Lingering Potion of ";
            } else {
                potionName = ChatColor.AQUA + "Potion of ";
            }
            // effect name
            switch (outputPotionData.getType()) {
                case AWKWARD:
                    potionName = ChatColor.AQUA + "Awkward Potion";
                    break;
                case MUNDANE:
                    potionName = ChatColor.AQUA + "Mundane Potion";
                    break;
                case THICK:
                    potionName = ChatColor.AQUA + "Thick Potion";
                    break;
                case INSTANT_DAMAGE:
                    potionName += "Harming";
                    break;
                case INSTANT_HEAL:
                    potionName += "Healing";
                    break;
                case JUMP:
                    potionName += "Leaping";
                    break;
                case REGEN:
                    potionName += "Regeneration";
                    break;
                case TURTLE_MASTER:
                    potionName += "the Turtle Master";
                    break;
                default:
                    potionName += WordUtils.capitalizeFully(outputPotionData.getType().toString().replace("_", " "));
                    break;
            }
            // suffix
            if (outputPotionData.isExtended()) {
                potionName += " ++";
            } else if (outputPotionData.isUpgraded()) {
                potionName += " II";
            }
        }

        // make the itemstack
        ItemStack potion;
        if (outputPotionData == null) {
            potion = new ItemStack(Material.GLASS_BOTTLE, 1);
            ItemMeta potionMeta = potion.getItemMeta();
            potionMeta.setDisplayName(ChatColor.WHITE + "Choose Recipe");
            potionMeta.setLore(GUIUtils.splitStringOverLines(ChatColor.GRAY + "Choose the recipe for potions you want to brew.\n "
                    + ChatColor.GOLD + "Current Recipe: " + potionName, 40));
            potion.setItemMeta(potionMeta);
        } else {
            Material material;
            if (toSplash) {
                material = Material.SPLASH_POTION;
            } else if (toLingering) {
                material = Material.LINGERING_POTION;
            } else {
                material = Material.POTION;
            }
            potion = new ItemStack(material, 1);
            PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
            potionMeta.setBasePotionData(outputPotionData);
            potionMeta.setDisplayName(ChatColor.WHITE + "Choose Recipe");
            potionMeta.setLore(GUIUtils.splitStringOverLines(ChatColor.GRAY + "Choose the recipe for potions you want to brew.\n "
                    + ChatColor.GOLD + "Current Recipe: " + potionName, 40));
            potion.setItemMeta(potionMeta);
        }

        return new GUIItem(potion, new OpenMachineGUIFunction(new ChooseBrewingRecipeGUI(this), this));
    }

    public String getSignText() {
        return "[brewer]";
    }
    public String getCustomSaveText() {
        if (ingredient == null) {
            return "0";
        } else {
            return ingredient + ";" + inputPotionData.getType() + ";" + inputPotionData.isExtended() + ";"
                    + inputPotionData.isUpgraded() + ";" + outputPotionData.getType() + ";" + outputPotionData.isExtended()
                    + ";" + outputPotionData.isUpgraded() + ";" + toSplash + ";" + toLingering;
        }

    }
    public void loadCustomSaveText(String text) {
        String[] split = text.split(";");
        ingredient = Material.getMaterial(split[0]);
        inputPotionData = new PotionData(PotionType.valueOf(split[1]), Boolean.parseBoolean(split[2]), Boolean.parseBoolean(split[3]));
        outputPotionData = new PotionData(PotionType.valueOf(split[4]), Boolean.parseBoolean(split[5]), Boolean.parseBoolean(split[6]));
        toSplash = Boolean.parseBoolean(split[7]);
        toLingering = Boolean.parseBoolean(split[8]);
    }
}
