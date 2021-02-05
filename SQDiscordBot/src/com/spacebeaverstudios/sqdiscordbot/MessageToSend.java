package com.spacebeaverstudios.sqdiscordbot;

public class MessageToSend {
    private final String message;
    private final Exception e;

    public MessageToSend(String message, Exception e) {
        this.message = message;
        this.e = e;
    }

    public void send() {
        if (message == null) DiscordWrapper.sendMessage(e);
        else if (e == null) DiscordWrapper.sendMessage(message);
        else DiscordWrapper.sendMessage(message, e);
    }
}
