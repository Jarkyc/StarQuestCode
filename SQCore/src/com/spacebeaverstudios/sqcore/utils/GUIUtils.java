package com.spacebeaverstudios.sqcore.utils;

import com.spacebeaverstudios.sqcore.SQCore;
import com.spacebeaverstudios.sqcore.gui.GUIItem;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GUIUtils {
    private static final ArrayList<GUIItem> buttons = new ArrayList<>();
    private static int randomPersistentDataCounter = 0;
    private static NamespacedKey guiItemNamespacedKey;

    public static boolean isButton(ItemStack itemClicked){
        if (itemClicked == null || itemClicked.getType() == Material.AIR) {
            return false;
        }

        for(GUIItem item : buttons) {
            ItemStack stack = item.getItemStack();
            if (stack.getItemMeta().getDisplayName().equals(itemClicked.getItemMeta().getDisplayName())
                    && stack.getType() == itemClicked.getType()) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<GUIItem> getButtons(){
        return buttons;
    }

    public static GUIItem getGUIItem(ItemStack stack) {
        String stackValue = stack.getItemMeta().getPersistentDataContainer().get(guiItemNamespacedKey,
                PersistentDataType.PrimitivePersistentDataType.STRING);

        if (stackValue == null) {
            return null; // stack isn't a GUIItem
        }
        for (GUIItem item : getButtons()) {
            if (stack.equals(item.getItemStack()) && item.getItemStack().getItemMeta().getPersistentDataContainer()
                    .get(guiItemNamespacedKey, PersistentDataType.STRING).equals(stackValue)) {
                return item;
            }
        }
        return null;
    }

    public static List<String> splitStringOverLines(String string, int characterLimit) {
        List<String> stringLines = new ArrayList<>();
        stringLines.add("");

        int currentLine = 0;
        int currentLineCharacterCount = 0;

        String[] words = string.split(" ");

        // Split description across multiple lines so that the lore will fit on the screen
        for (int i = 0; i < words.length; i ++) {
            if (words[i].endsWith("\n")) {
                stringLines.set(currentLine, stringLines.get(currentLine) + (currentLineCharacterCount == 0 ? "" : " ")
                        + words[i].substring(0, words[i].length()-1));

                if (i != words.length - 1) {
                    currentLine++;
                    stringLines.add(ChatColor.getLastColors(stringLines.get(stringLines.size()-1)) + "");
                    currentLineCharacterCount = 0;
                }
            } else {
                String word = words[i];
                stringLines.set(currentLine, stringLines.get(currentLine) + (currentLineCharacterCount == 0 ? "" : " ") + word);
                currentLineCharacterCount += word.length() + 1;

                if (currentLineCharacterCount >= characterLimit && i != words.length - 1) {
                    currentLine++;
                    stringLines.add(ChatColor.getLastColors(stringLines.get(stringLines.size()-1)) + "");
                    currentLineCharacterCount = 0;
                }
            }
        }
        return stringLines;
    }

    public static ItemStack setWanted(ItemStack itemStack, boolean isWanted) {
        if (itemStack == null) {
            return null;
        } else {
            //return setNBTFlag(itemStack, "isWanted", true);
            net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);

            NBTTagCompound tag = nmsStack.getOrCreateTag();
            tag.setBoolean("isWanted", isWanted);
            nmsStack.setTag(tag);

            return CraftItemStack.asBukkitCopy(nmsStack);
        }
    }

    public static boolean isWanted(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        } else {
//            return getNBTFlag(itemStack, "isWanted");
            net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound tag = nmsStack.getOrCreateTag();
            if (!tag.hasKey("isWanted")) return false;
            return tag.getBoolean("isWanted");
        }
    }

    public static void initializeGUIItemNamespacedKey() {
        guiItemNamespacedKey = new NamespacedKey(SQCore.getInstance(), "GUIItemRandomPersistentData");
    }
    public static void setRandomPersistentData(ItemStack itemStack) {
        // prevent getGUIItem() from getting wrong item that is similar
        // not important what the value is, just has to be unique
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(guiItemNamespacedKey, PersistentDataType.STRING,
                String.valueOf(Instant.now().toEpochMilli()) + randomPersistentDataCounter);
        itemStack.setItemMeta(meta);
        randomPersistentDataCounter++;
    }
}
