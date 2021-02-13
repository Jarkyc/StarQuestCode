package com.spacebeaverstudios.sqcore.utils;

import com.spacebeaverstudios.sqcore.gui.GUIItem;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GUIUtils {
    private static final ArrayList<GUIItem> buttons = new ArrayList<>();

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
        for (GUIItem item : getButtons()) {
            if (stack.equals(item.getItemStack())) return item;
        }
        return null;
    }

    public static List<String> splitStringOverLines(String string, String linePrefix, int characterLimit) {
        List<String> stringLines = new ArrayList<>();
        stringLines.add(linePrefix + "");

        int currentLine = 0;
        int currentLineCharacterCount = 0;

        String[] words = string.split(" ");

        // Split description across multiple lines so that the lore will fit on the screen
        for (int i = 0; i < words.length; i ++) {
            String word = words[i];
            stringLines.set(currentLine, stringLines.get(currentLine) + (currentLineCharacterCount == 0 ? "" : " ") + word);
            currentLineCharacterCount += word.length() + 1;

            if (currentLineCharacterCount >= characterLimit && i != words.length - 1) {
                currentLine ++;
                stringLines.add(linePrefix + "");
                currentLineCharacterCount = 0;
            }
        }
        return stringLines;
    }

    public static ItemStack setWanted(ItemStack itemStack, boolean isWanted) {
        if (itemStack == null) return null;
        else {
            //return setNBTFlag(itemStack, "isWanted", true);
            net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);

            NBTTagCompound tag = nmsStack.getOrCreateTag();
            tag.setBoolean("isWanted", isWanted);
            nmsStack.setTag(tag);

            return CraftItemStack.asBukkitCopy(nmsStack);
        }
    }

    public static boolean isWanted(ItemStack itemStack) {
        if (itemStack == null) return false;
        else {
//            return getNBTFlag(itemStack, "isWanted");
            net.minecraft.server.v1_15_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound tag = nmsStack.getOrCreateTag();
            if (!tag.hasKey("isWanted")) return false;
            return tag.getBoolean("isWanted");
        }
    }
}
