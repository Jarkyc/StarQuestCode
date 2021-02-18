package com.spacebeaverstudios.sqcore.commands.arguments;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class WorldArgument extends Argument<World>{
    public WorldArgument (String name) {
        super(name);
    }

    @Override
    public List<String> getSuggestions() {
        List<String> suggestions = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            suggestions.add(world.getName());
        }
        return suggestions;
    }

    @Override
    public World parse(String arg) {
        return Bukkit.getWorld(arg.toLowerCase());
    }
}
