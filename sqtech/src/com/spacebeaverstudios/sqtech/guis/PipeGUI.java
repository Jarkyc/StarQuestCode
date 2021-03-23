package com.spacebeaverstudios.sqtech.guis;

import com.spacebeaverstudios.sqcore.gui.GUI;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import com.spacebeaverstudios.sqtech.SQTech;
import com.spacebeaverstudios.sqtech.guis.guifunctions.ChangePipeGUIPageGUIFunction;
import com.spacebeaverstudios.sqtech.objects.Pipe;
import com.spacebeaverstudios.sqtech.objects.machines.Machine;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

public class PipeGUI extends GUI {
    private final Pipe pipe;
    private int currentPage = 0;

    public PipeGUI(Pipe pipe, Location location) {
        super("Pipe at " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
        this.pipe = pipe;
    }

    public Inventory createInventory() {
        int size = 36;
        if (currentPage != 0 || pipe.getItemInputMachines().size() > 7 || pipe.getPowerInputMachines().size() > 7
                || pipe.getItemOutputMachines().size() > 7 || pipe.getPowerOutputMachines().size() > 7) {
            size = 45;
        }
        
        Inventory inventory = Bukkit.createInventory(null, size, getInventoryName());

        // labels
        GUIItem itemInputLabelItem = new GUIItem("Item Input Machines",
                ChatColor.GRAY + "Machines that input items to this pipe.", Material.CHEST, null);
        getGuiItems().add(itemInputLabelItem);
        inventory.setItem(0, itemInputLabelItem.getItemStack());

        GUIItem powerInputLabelItem = new GUIItem("Power Input Machines",
                ChatColor.GRAY + "Machines that input power to this pipe.", Material.REDSTONE_BLOCK, null);
        getGuiItems().add(powerInputLabelItem);
        inventory.setItem(0, powerInputLabelItem.getItemStack());

        GUIItem itemOutputLabelItem = new GUIItem("Item Output Machines",
                ChatColor.GRAY + "Machines that receive items from this pipe.", Material.CHEST, null);
        getGuiItems().add(itemOutputLabelItem);
        inventory.setItem(0, itemOutputLabelItem.getItemStack());

        GUIItem powerOutputLabelItem = new GUIItem("Power Output Machines",
                ChatColor.GRAY + "Machines that receive power from this pipe.", Material.REDSTONE_BLOCK, null);
        getGuiItems().add(powerOutputLabelItem);
        inventory.setItem(0, powerOutputLabelItem.getItemStack());

        // machines
        for (int i = currentPage * 7; i < (currentPage + 1) * 7; i++) {
            if (pipe.getItemInputMachines().size() >= i) {
                Machine machine = pipe.getItemInputMachines().get(i);
                GUIItem machineItem = new GUIItem(machine.getMachineName(), "Inputs items to this pipe.\n"
                        + ChatColor.GOLD + "Sign Location: " + ChatColor.AQUA + machine.getSign().getBlockX() + ", "
                        + machine.getSign().getBlockY() + ", " + machine.getSign().getBlockZ() + "\n " + ChatColor.GOLD
                        + "Node Location: " + ChatColor.AQUA + machine.getNode().getBlockX() + ", " + machine.getNode().getBlockY()
                        + ", " + machine.getNode().getBlockZ(), Material.LAPIS_BLOCK, null);
                getGuiItems().add(machineItem);
                inventory.setItem((i % 7) + 2, machineItem.getItemStack());
            }
            if (pipe.getPowerInputMachines().size() >= i) {
                Machine machine = pipe.getPowerInputMachines().get(i);
                GUIItem machineItem = new GUIItem(machine.getMachineName(), "Inputs power to this pipe.\n"
                        + ChatColor.GOLD + "Sign Location: " + ChatColor.AQUA + machine.getSign().getBlockX() + ", "
                        + machine.getSign().getBlockY() + ", " + machine.getSign().getBlockZ() + "\n " + ChatColor.GOLD
                        + "Node Location: " + ChatColor.AQUA + machine.getNode().getBlockX() + ", " + machine.getNode().getBlockY()
                        + ", " + machine.getNode().getBlockZ(), Material.LAPIS_BLOCK, null);
                getGuiItems().add(machineItem);
                inventory.setItem((i % 7) + 11, machineItem.getItemStack());
            }
            if (pipe.getItemOutputMachines().size() >= i) {
                Machine machine = pipe.getItemOutputMachines().get(i);
                GUIItem machineItem = new GUIItem(machine.getMachineName(), "Receives items from this pipe.\n"
                        + ChatColor.GOLD + "Sign Location: " + ChatColor.AQUA + machine.getSign().getBlockX() + ", "
                        + machine.getSign().getBlockY() + ", " + machine.getSign().getBlockZ() + "\n " + ChatColor.GOLD
                        + "Node Location: " + ChatColor.AQUA + machine.getNode().getBlockX() + ", " + machine.getNode().getBlockY()
                        + ", " + machine.getNode().getBlockZ(), Material.LAPIS_BLOCK, null);
                getGuiItems().add(machineItem);
                inventory.setItem((i % 7) + 20, machineItem.getItemStack());
            }
            if (pipe.getPowerOutputMachines().size() >= i) {
                Machine machine = pipe.getPowerOutputMachines().get(i);
                GUIItem machineItem = new GUIItem(machine.getMachineName(), "Receives power from this pipe.\n"
                        + ChatColor.GOLD + "Sign Location: " + ChatColor.AQUA + machine.getSign().getBlockX() + ", "
                        + machine.getSign().getBlockY() + ", " + machine.getSign().getBlockZ() + "\n " + ChatColor.GOLD
                        + "Node Location: " + ChatColor.AQUA + machine.getNode().getBlockX() + ", " + machine.getNode().getBlockY()
                        + ", " + machine.getNode().getBlockZ(), Material.LAPIS_BLOCK, null);
                getGuiItems().add(machineItem);
                inventory.setItem((i % 7) + 29, machineItem.getItemStack());
            }
        }

        // bottom row
        if (size == 45) {
            if (currentPage != 0) {
                ItemStack banner = new ItemStack(Material.WHITE_BANNER);
                BannerMeta bannerMeta = (BannerMeta) banner.getItemMeta();
                bannerMeta.setDisplayName(ChatColor.BLUE + "Previous Page");
                bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                bannerMeta.addPattern(new Pattern(DyeColor.BLUE, PatternType.RHOMBUS_MIDDLE));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_RIGHT));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_RIGHT));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
                banner.setItemMeta(bannerMeta);

                GUIItem bannerItem = new GUIItem(banner, new ChangePipeGUIPageGUIFunction(this, currentPage - 1));
                getGuiItems().add(bannerItem);
                inventory.setItem(36, bannerItem.getItemStack());
            } else {
                GUIItem barItem = new GUIItem(" ", null, Material.IRON_BARS, null);
                getGuiItems().add(barItem);
                inventory.setItem(36, barItem.getItemStack());
            }

            for (int i = 0; i < 7; i++) {
                GUIItem barItem = new GUIItem(" ", null, Material.IRON_BARS, null);
                getGuiItems().add(barItem);
                inventory.setItem(37 + i, barItem.getItemStack());
            }

            if (pipe.getItemInputMachines().size() > (currentPage + 1) * 7
                    || pipe.getPowerInputMachines().size() > (currentPage + 1) * 7
                    || pipe.getItemOutputMachines().size() > (currentPage + 1) * 7
                    || pipe.getPowerOutputMachines().size() > (currentPage + 1) * 7) {
                ItemStack banner = new ItemStack(Material.WHITE_BANNER);
                BannerMeta bannerMeta = (BannerMeta) banner.getItemMeta();
                bannerMeta.setDisplayName(ChatColor.BLUE + "Next Page");
                bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                bannerMeta.addPattern(new Pattern(DyeColor.BLUE, PatternType.RHOMBUS_MIDDLE));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_LEFT));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT));
                bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
                banner.setItemMeta(bannerMeta);

                GUIItem bannerItem = new GUIItem(banner, new ChangePipeGUIPageGUIFunction(this, currentPage + 1));
                getGuiItems().add(bannerItem);
                inventory.setItem(44, bannerItem.getItemStack());
            } else {
                GUIItem barItem = new GUIItem(" ", null, Material.IRON_BARS, null);
                getGuiItems().add(barItem);
                inventory.setItem(44, barItem.getItemStack());
            }
        }

        return inventory;
    }

    public void goToPage(int page) {
        currentPage = page;
        getGuiItems().clear();
        Inventory inventory = createInventory();
        Bukkit.getScheduler().runTaskLater(SQTech.getInstance(), () -> getPlayer().openInventory(inventory), 1);
    }
}
