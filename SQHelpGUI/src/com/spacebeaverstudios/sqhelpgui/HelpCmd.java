package com.spacebeaverstudios.sqhelpgui;

import com.spacebeaverstudios.sqcore.commands.SQCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCmd extends SQCmd{

    public HelpCmd() {
        super("help", "opens help GUI", true);

    }

    @Override
    public void onExecute(CommandSender commandSender, String s, Object[] objects) {

        Player player = (Player) commandSender;

        player.openInventory(SQHelpGUI.mainInv);


    }
}
