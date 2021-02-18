package com.spacebeaverstudios.sqcore.commands.arguments;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class OfflinePlayerArgument extends Argument<OfflinePlayer>{
    public OfflinePlayerArgument (String name) {
        super(name);
    }

    @Override
    public List<String> getSuggestions() {
        List<String> suggestions = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            // TODO: Uncomment this line when VanishAPI is a thing again
            // if (!VanishAPI.isInvisible(player)) {
            suggestions.add(player.getName());
            // }
        }
        return suggestions;
    }

    @Override
    public OfflinePlayer parse(String arg) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(arg);

        if (offlinePlayer == null || offlinePlayer.getName() == null)
            return null;

        return offlinePlayer;
    }
}
