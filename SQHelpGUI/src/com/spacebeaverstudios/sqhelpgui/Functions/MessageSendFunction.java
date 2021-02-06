package com.spacebeaverstudios.sqhelpgui.Functions;

import org.bukkit.entity.Player;

public class MessageSendFunction extends Function{

    private String message;

    public MessageSendFunction(String string){
        this.message = string;
    }

    @Override
    public void run (Player player){

        player.sendMessage(this.message);


    }



}
