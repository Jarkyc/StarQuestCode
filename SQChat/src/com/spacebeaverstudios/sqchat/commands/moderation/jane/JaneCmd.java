package com.spacebeaverstudios.sqchat.commands.moderation.jane;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.command.CommandSender;

public class JaneCmd extends SQCmd {
    public JaneCmd() {
        super("jane", "Supercommand for Jane.", false);

        this.addSubcommand(new JaneBanCmd());
        this.addSubcommand(new JaneBansCmd());
        this.addSubcommand(new JaneReloadCmd());
        this.addSubcommand(new JaneSayCmd());
        this.addSubcommand(new JaneUnbanCmd());
    }

    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        this.getHelpCommand().onExecute(sender, previousLabels, args);
    }
}
