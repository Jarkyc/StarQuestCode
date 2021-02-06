package com.spacebeaverstudios.sqbaseclasses.gui.guifunctions;

import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class SendMessageFunction extends GUIFunction {
    private final String message;

    public SendMessageFunction(String string){
        this.message = string;
    }

    @Override
    public void run(Player player){
        player.sendMessage(this.message);
    }
}
