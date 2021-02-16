package com.spacebeaverstudios.sqchat.commands.arguments;

import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import com.spacebeaverstudios.sqcore.commands.arguments.Argument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MutedPlayerArgument extends Argument {
    public MutedPlayerArgument(String name) {
        super(name);
    }

    public ArrayList<String> getSuggestions() {
        ArrayList<String> suggestions = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
            if (ChatUtils.getMutedPlayers().containsKey(player))
                suggestions.add(player.getDisplayName());
        return suggestions;
    }

    public Object parse(String string) {
        return Bukkit.getPlayer(string);
    }
}
