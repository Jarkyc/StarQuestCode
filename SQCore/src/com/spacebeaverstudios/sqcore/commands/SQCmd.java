package com.spacebeaverstudios.sqcore.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.spacebeaverstudios.sqcore.commands.arguments.Argument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public abstract class SQCmd implements CommandExecutor, TabCompleter {

    private final List<SQCmd> subcommands = new ArrayList<SQCmd>();
    @SuppressWarnings("rawtypes")
    private final List<Argument> arguments = new ArrayList<Argument>();
    @SuppressWarnings("rawtypes")
    private final List<Argument> optionalArguments = new ArrayList<Argument>();
    private SQCmd helpCommand;
    private final HashSet<String> aliases = new HashSet<String>();
    private final String label;
    private final String description;
    private final boolean requiresPlayer;
    private final String permission;

    public SQCmd(String label, String description, boolean requiresPlayer) {
        this.addAliase(label);
        this.label = label.toLowerCase();
        this.description = description;
        this.requiresPlayer = requiresPlayer;
        this.permission = "";
    }

    public SQCmd(String label, String description, boolean requiresPlayer, String permission) {
        this.addAliase(label);
        this.label = label.toLowerCase();
        this.description = description;
        this.requiresPlayer = requiresPlayer;
        this.permission = permission;
    }

    public void addAliase(String aliase) {
        this.aliases.add(aliase.toLowerCase());
    }

    public boolean hasAliase (String aliase) {
        return this.aliases.contains(aliase.toLowerCase());
    }

    public String getLabel () {
        return this.label;
    }

    public String getDescription () {
        return this.description;
    }

    public void addSubcommand(SQCmd subcommand) {
        this.subcommands.add(subcommand);
    }

    public List<SQCmd> getSubcommands() {
        return this.subcommands;
    }

    public void addArgument (Argument argument) {
        this.arguments.add(argument);
    }

    public List<Argument> getArguments () {
        return this.arguments;
    }

    public void addOptionalArgument (Argument argument) {
        this.optionalArguments.add(argument);
    }

    public List<Argument> getOptionalArguments() {
        return this.optionalArguments;
    }

    public void addHelpCommand() {
        helpCommand = new HelpCmd(this);
        addSubcommand(helpCommand);
    }

    public SQCmd getHelpCommand () {
        if (helpCommand == null) {
            addHelpCommand();
        }
        return helpCommand;
    }

    public boolean hasPermission (CommandSender sender) {
        return this.permission.equals("") || sender.hasPermission(this.permission);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            for (SQCmd subcommand : this.subcommands) {
                if (subcommand.hasPermission(sender) && subcommand.hasAliase(args[0])) {
                    String[] newArgs = new String[args.length - 1];
                    for (int i = 1; i < args.length; i ++) {
                        newArgs[i - 1] = args[i];
                    }
                    return subcommand.onTabComplete(sender, command, label + " " + args[0], newArgs);
                }
            }
        }
        List<String> suggestions = new ArrayList<String>();
        boolean removeIfMatches = true;
        int location = args.length - 1;
        if (location < 0) {
            location = 0;
        }
        if (this.subcommands.size() > 0) {
            for (SQCmd subcommand : this.subcommands) {
                if (subcommand.hasPermission(sender)) {
                    suggestions.addAll(subcommand.aliases);
                }
            }
        } else {
            List<Argument> allArguments = new ArrayList<>(this.arguments.size() + this.optionalArguments.size());
            allArguments.addAll(this.arguments);
            allArguments.addAll(this.optionalArguments);

            if (location < allArguments.size()) {
                List<String> argumentSuggestions = allArguments.get(location).getSuggestions();
                if (args.length <= location || argumentSuggestions.size() == 0 || argumentSuggestions.contains(args[location])) {
                    String suggestion = "";
                    if (args.length <= location || !argumentSuggestions.contains(args[location])) {
                        if (location < this.arguments.size()) {
                            suggestion = " <" + allArguments.get(location).getName() + ">";
                        } else {
                            suggestion = " [" + allArguments.get(location).getName() + "]";
                        }
                    }

                    if (argumentSuggestions.size() == 0) {
                        int checkLocation = location + 1;
                        do {
                            if (checkLocation < allArguments.size()) {
                                if (checkLocation < this.arguments.size()) {
                                    suggestion += " <" + allArguments.get(checkLocation).getName() + ">";
                                } else {
                                    suggestion += " [" + allArguments.get(checkLocation).getName() + "]";
                                }
                                checkLocation ++;
                            }
                        } while (checkLocation < this.arguments.size());
                    }

                    if (!suggestion.equals("")) {
                        suggestions.add(suggestion);
                    }
                    removeIfMatches = false;
                } else {
                    suggestions.addAll(argumentSuggestions);
                }
            }
        }

        if (removeIfMatches && args.length > 0) {
            for (int i = 0; i < suggestions.size(); i ++) {
                if (!suggestions.get(i).toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                    suggestions.remove(i);
                    i --;
                }
            }
        }
        return suggestions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (this.requiresPlayer && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only a player can use this command!");
            return true;
        }

        if (!hasPermission(sender)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return true;
        }

        if (args.length > 0) {
            for (SQCmd subcommand : this.subcommands) {
                if (subcommand.hasAliase(args[0])) {
                    String[] newArgs = new String[args.length - 1];
                    for (int i = 1; i < args.length; i ++) {
                        newArgs[i - 1] = args[i];
                    }
                    return subcommand.onCommand(sender, command, label + " " + args[0], newArgs);
                }
            }
        }

        List<Argument> allArguments = new ArrayList<>(this.arguments.size() + this.optionalArguments.size());
        allArguments.addAll(this.arguments);
        allArguments.addAll(this.optionalArguments);

        if (args.length < this.arguments.size()) {
            sender.sendMessage(ChatColor.RED + "You must specify a value for <" + this.arguments.get(args.length).getName() + ">!");
            return true;
        }

        Object[] arguments = new Object[args.length];

        for (int i = 0; i < args.length; i ++) {
            if (i < allArguments.size()) {
                Argument argument = allArguments.get(i);
                Object result = argument.parse(args[i]);
                if (result == null) {
                    sender.sendMessage(ChatColor.RED + "You must specify a valid value for <" + argument.getName() + ">!");
                    return true;
                }
                arguments[i] = result;
            } else {
                arguments[i] = args[i];
            }
        }

        onExecute(sender, label, arguments);
        return true;

    }

    public abstract void onExecute (CommandSender sender, String previousLabels, Object[] args);
}