package com.spacebeaverstudios.sqcore.commands.arguments;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class OnlinePlayerArgument extends Argument<Player>{
    public OnlinePlayerArgument (String name) {
        super(name);
    }

    @Override
    public List<String> getSuggestions() {
        List<String> suggestions = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            // TODO: uncomment this line when VanishAPI is a thing again
//            if (!VanishAPI.isInvisible(player)) {
            suggestions.add(player.getName());
//            }
        }
        return suggestions;
    }

    @Override
    public Player parse(String arg) {
        return Bukkit.getPlayer(arg);
    }
}
