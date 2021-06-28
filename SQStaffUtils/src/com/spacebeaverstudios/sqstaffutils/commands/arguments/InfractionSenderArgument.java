package com.spacebeaverstudios.sqstaffutils.commands.arguments;

import com.spacebeaverstudios.sqcore.commands.arguments.Argument;
import com.spacebeaverstudios.sqstaffutils.objects.InfractionSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class InfractionSenderArgument extends Argument<InfractionSender> {
    public InfractionSenderArgument(String name) {
        super(name);
    }

    public ArrayList<String> getSuggestions() {
        ArrayList<String> suggestions = new ArrayList<>(Collections.singletonList("Console"));
        for (Player player : Bukkit.getOnlinePlayers()) {
            suggestions.add(player.getName());
        }
        return suggestions;
    }

    public InfractionSender parse(String str) {
        if (str.equalsIgnoreCase("console")) {
            return new InfractionSender(null);
        } else if (Bukkit.getOfflinePlayer(str) != null) {
            return new InfractionSender(Bukkit.getOfflinePlayer(str).getUniqueId());
        } else {
            return null;
        }
    }
}
