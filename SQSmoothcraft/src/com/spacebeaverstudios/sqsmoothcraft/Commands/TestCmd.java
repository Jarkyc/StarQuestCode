package com.spacebeaverstudios.sqsmoothcraft.Commands;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import com.spacebeaverstudios.sqcore.utils.discord.DiscordUtils;
import org.bukkit.command.CommandSender;

public class TestCmd extends SQCmd {
    public TestCmd() {
        super("test", "test", true);
    }

    @Override
    public void onExecute(CommandSender commandSender, String s, Object[] objects) {

        DiscordUtils.reportError(DiscordUtils.tag("jarkyc") + " Test message fo today");
    }
}
