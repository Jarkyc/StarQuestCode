package com.spacebeaverstudios.sqcore.commands.arguments;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class ChatColorArgument extends Argument<ChatColor> {
    public ChatColorArgument(String name) {
        super(name);
    }

    public List<String> getSuggestions() {
        return Arrays.asList("aqua", "black", "blue", "dark_aqua", "dark_gray", "dark_green", "dark_purple", "dark_red", "gold",
                "gray", "green", "light_purple", "red", "white", "yellow");
    }

    public ChatColor parse(String arg) {
        return ChatColor.valueOf(arg.toUpperCase());
    }
}
