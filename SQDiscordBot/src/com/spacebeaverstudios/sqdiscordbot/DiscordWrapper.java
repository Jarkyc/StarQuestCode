package com.spacebeaverstudios.sqdiscordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.ArrayList;
import java.util.Objects;

public class DiscordWrapper {
    private static JDA jda;
    private static TextChannel bugChannel;
    private static boolean initialized = false;
    private static final ArrayList<MessageToSend> futureMessages = new ArrayList<>();

    public static void initialize() {
        try {
            JDABuilder builder = JDABuilder.createDefault(SQDiscordBot.getInstance().getConfig().getString("token"));
            builder.setActivity(Activity.playing("StarQuest"));

            // these things are here to reduce strain on the bot by just disabling all the features we don't need
            // if things aren't working and you don't know why, double check here
            // if the scope of the bot is expanded, make sure you edit this list appropriately
            builder.disableIntents(GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGE_TYPING,
                    GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_TYPING,
                    GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES);
            builder.disableCache(CacheFlag.ROLE_TAGS, CacheFlag.EMOTE, CacheFlag.VOICE_STATE, CacheFlag.ACTIVITY);

            builder.build(); // connects the bot to discord

            bugChannel = jda.getGuilds().get(0).getTextChannelsByName(
                    Objects.requireNonNull(SQDiscordBot.getInstance().getConfig().getString("bug-channel")), true).get(0);
            if (bugChannel == null) throw new NullPointerException("bugChannel == null");
            if (!bugChannel.canTalk()) throw new Exception("Not allowed to talk in bugChannel!");

            initialized = true;

            // all messages that tried to be sent before initialization will be sent now
            for (MessageToSend msg : futureMessages) msg.send();
            futureMessages.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String message) {
        if (initialized) bugChannel.sendMessage(message);
        else futureMessages.add(new MessageToSend(message, null));
    }
    public static void sendMessage(String message, Exception e) {
        if (initialized) {
            StringBuilder errorStr = new StringBuilder();
            for (StackTraceElement elem : e.getStackTrace())
                errorStr.append(elem.toString()).append("\n");
            bugChannel.sendMessage(message + "```" + errorStr + "```");
        } else futureMessages.add(new MessageToSend(message, e));
    }
    public static void sendMessage(Exception e) {
        if (initialized) {
            StringBuilder errorStr = new StringBuilder();
            for (StackTraceElement elem : e.getStackTrace())
                errorStr.append(elem.toString()).append("\n");
            bugChannel.sendMessage("```" + errorStr + "```");
        } else futureMessages.add(new MessageToSend(null, e));
    }
}
