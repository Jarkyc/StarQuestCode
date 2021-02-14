package com.spacebeaverstudios.sqcore.gui;

import com.spacebeaverstudios.sqcore.SQCore;
import com.spacebeaverstudios.sqcore.gui.guifunctions.ChangePageFunction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class ListGUI extends GUI {
    private final int rows;
    private int currentPage = 0;

    public ListGUI(String inventoryName, int rows) {
        super(inventoryName);
        if (rows < 1) this.rows = 1;
        else this.rows = Math.min(rows, 6);
    }

    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, rows*9, super.getInventoryName() + " Page " + (currentPage+1));

        ArrayList<ItemStack> bottomRow = createBottomRow();
        for (int i = 0; i < bottomRow.size(); i++) inventory.setItem(inventory.getSize()-(9-i), bottomRow.get(i));

        for (int i = 0; i < inventory.getSize() - 9; i ++) {
            int objectIndex = i + (currentPage * (inventory.getSize() - 9));
            List<Object> objectList = getObjectList();

            if (objectIndex < objectList.size()) inventory.setItem(i, getObjectItem(objectList.get(objectIndex)));
            else break;
        }

        return inventory;
    }

    // this is separate so it can be overridden
    public ArrayList<ItemStack> createBottomRow() {
        ArrayList<GUIItem> guiItems = new ArrayList<>();

        // previous page item
        if (currentPage > 0) guiItems.add(getPrevPageItem());
        else guiItems.add(new GUIItem(" ", null, Material.IRON_BARS, null));

        // all the bars in between
        for (int i = 0; i < 7; i++) guiItems.add(new GUIItem(" ", null, Material.IRON_BARS, null));

        // next page item
        if (currentPage < (Math.ceil(getObjectList().size()/9d))/(rows-1)) guiItems.add(getNextPageItem());
        else guiItems.add(new GUIItem(" ", null, Material.IRON_BARS, null));

        // convert to ArrayList<ItemStack>
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        for (GUIItem item : guiItems) {
            itemStacks.add(item.getItemStack());
            getGuiItems().add(item);
        }
        return itemStacks;
    }
    // these are separate as well to reduce code when overriding
    public GUIItem getPrevPageItem() {
        ItemStack banner = new ItemStack(Material.WHITE_BANNER);
        BannerMeta bannerMeta = (BannerMeta) banner.getItemMeta();
        bannerMeta.setDisplayName(ChatColor.BLUE + "Previous Page");
        bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        bannerMeta.addPattern(new Pattern(DyeColor.BLUE, PatternType.RHOMBUS_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_RIGHT));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_RIGHT));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));

        return new GUIItem(banner, new ChangePageFunction(this, currentPage-1));
    }
    public GUIItem getNextPageItem() {
        ItemStack banner = new ItemStack(Material.WHITE_BANNER);
        BannerMeta bannerMeta = (BannerMeta) banner.getItemMeta();
        bannerMeta.setDisplayName(ChatColor.BLUE + "Next Page");
        bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        bannerMeta.addPattern(new Pattern(DyeColor.BLUE, PatternType.RHOMBUS_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_LEFT));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));

        return new GUIItem(banner, new ChangePageFunction(this, currentPage-1));
    }

    public void goToPage(int page) {
        currentPage = page;
        Inventory inventory = createInventory();
        Bukkit.getScheduler().runTaskLater(SQCore.getInstance(), () -> getPlayer().openInventory(inventory), 1);
    }

    public abstract List<Object> getObjectList();
    public abstract ItemStack getObjectItem(Object obj);
}