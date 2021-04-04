package com.spacebeaverstudios.sqchat.commands.moderation.jane;

import com.spacebeaverstudios.sqchat.utils.JaneUtils;
import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.commands.arguments.StringArgument;
import org.bukkit.command.CommandSender;

public class JaneSayCmd extends SQCmd {
    public JaneSayCmd() {
        super("say", "Make Jane say something", false);
        this.addArgument(new StringArgument("message"));
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        StringBuilder message = new StringBuilder();
        for (Object obj : args) {
            message.append(obj.toString()).append(" ");
        }
        JaneUtils.say(message.toString(), true);
    }
}
