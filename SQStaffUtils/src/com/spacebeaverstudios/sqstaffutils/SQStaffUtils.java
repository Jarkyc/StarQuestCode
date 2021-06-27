package com.spacebeaverstudios.sqstaffutils;

import com.spacebeaverstudios.sqstaffutils.objects.LogListener;
import org.bukkit.plugin.java.JavaPlugin;

public class SQStaffUtils extends JavaPlugin {
    private static SQStaffUtils instance;
    private static LogListener logListener;

    public static SQStaffUtils getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        logListener = new LogListener();
    }
}
