package com.spacebeaverstudios.sqbaseclasses.commands;

import com.spacebeaverstudios.sqbaseclasses.commands.arguments.Argument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCmd extends SQCmd {
    private final SQCmd masterCommand;

    public HelpCmd(SQCmd masterCommand) {
        super("help", "Displays this", false);
        this.masterCommand = masterCommand;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onExecute(CommandSender sender, String previousLabels, Object[] args) {
        String label = masterCommand.getLabel().substring(0, 1).toUpperCase() + masterCommand.getLabel().substring(1);
        String previousLabelsModified = previousLabels;
        if (previousLabelsModified.endsWith(" help")) {
            previousLabelsModified = previousLabelsModified.substring(0, previousLabelsModified.length() - 5);
        }

        sender.sendMessage(ChatColor.GOLD + "==========[" + label + " Help]==========");

        for (SQCmd subcommand : masterCommand.getSubcommands()) {
            if (subcommand.hasPermission(sender)) {
                StringBuilder argList = new StringBuilder();
                for (Argument argument : subcommand.getArguments()) {
                    argList.append(" <").append(argument.getName()).append(">");
                }
                for (Argument argument : subcommand.getOptionalArguments()) {
                    argList.append(" [").append(argument.getName()).append("]");
                }
                sender.sendMessage(ChatColor.GOLD + "/" + previousLabelsModified + " " + subcommand.getLabel() + argList + " - " + ChatColor.BLUE + subcommand.getDescription());
            }
        }
    }
}