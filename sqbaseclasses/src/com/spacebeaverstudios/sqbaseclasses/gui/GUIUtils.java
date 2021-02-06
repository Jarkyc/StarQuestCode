package com.spacebeaverstudios.sqbaseclasses.gui;

import org.bukkit.Material;
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
}
