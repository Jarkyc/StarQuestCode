package com.spacebeaverstudios.sqtech.objects;

import org.bukkit.Material;

import java.util.ArrayList;

public class SortingFilter {
    public boolean whitelist = true; // false if blacklist
    public final ArrayList<Material> items = new ArrayList<>();

    public boolean itemFits(Material material) {
        return whitelist == items.contains(material);
    }

    public String getSaveString() {
        StringBuilder str = new StringBuilder(String.valueOf(whitelist));
        for (Material material : items) {
            str.append(":").append(material);
        }
        return str.toString();
    }
    public void loadFromString(String str) {
        String[] split = str.split(":");
        whitelist = split[0].equals("true");
        for (int i = 1; i < split.length; i++) {
            items.add(Material.getMaterial(split[i]));
        }
    }
}
