package com.spacebeaverstudios.sqdiscordbot;

import org.bukkit.plugin.java.JavaPlugin;

public class SQDiscordBot extends JavaPlugin {
    // TODO: README.md
    // TODO: switch to 1.15

    private static SQDiscordBot instance;

    public static SQDiscordBot getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        DiscordWrapper.initialize();
        DiscordWrapper.sendMessage("Test");
    }
}
