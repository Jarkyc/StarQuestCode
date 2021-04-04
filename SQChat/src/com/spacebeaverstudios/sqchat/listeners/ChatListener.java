package com.spacebeaverstudios.sqchat.listeners;

import com.spacebeaverstudios.sqchat.SQChat;
import com.spacebeaverstudios.sqchat.utils.ChatUtils;
import com.spacebeaverstudios.sqchat.utils.JaneUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

public class ChatListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setCancelled(true);

        if (ChatUtils.getMutedPlayers().containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You are muted!");
            return;
        }

        ArrayList<Player> inMessage = new ArrayList<>();
        switch (ChatUtils.getPlayerChannels().get(player)) {
            case GLOBAL:
                // edit the chat message instead of sending a new one so it can be picked up by DiscordSRV
                event.setFormat(ChatColor.GRAY + "[" + ChatColor.GREEN + "G" + ChatColor.GRAY + "] "
                        + ChatUtils.getRankString(player) + ChatColor.WHITE + player.getDisplayName() + ": " + event.getMessage());
                event.setCancelled(false);
                SQChat.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(SQChat.getInstance(),
                        () -> JaneUtils.maybeReply(event.getMessage(), event.getPlayer()), 1);
                break;
            case PLANET:
                for (Player p : player.getLocation().getWorld().getPlayers()) {
                    inMessage.add(player);
                    p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "P" + ChatColor.GRAY + "] "
                            + ChatUtils.getRankString(player) + ChatColor.WHITE + player.getDisplayName() + ": " + event.getMessage());
                }
                for (Player spy : ChatUtils.getChannelSpies()) {
                    if (!inMessage.contains(spy)) {
                        spy.sendMessage(ChatUtils.getSocialSpyPrefix() + ChatColor.GRAY + "[" + ChatColor.BLUE + "P" + ChatColor.GRAY + "] "
                                + ChatUtils.getRankString(player) + ChatColor.WHITE + player.getDisplayName() + ": " + event.getMessage());
                    }
                }
                break;
            case LOCAL:
                for (Player p : player.getLocation().getWorld().getPlayers()) {
                    if (Math.sqrt(Math.pow(player.getLocation().getBlockX()-p.getLocation().getBlockX(), 2)
                            + Math.pow(player.getLocation().getBlockZ()-p.getLocation().getBlockZ(), 2)) <= 250) {
                        inMessage.add(p);
                        p.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "L" + ChatColor.GRAY + "] "
                                + ChatUtils.getRankString(player) + ChatColor.WHITE + player.getDisplayName() + ": " + event.getMessage());
                    }
                }
                for (Player spy : ChatUtils.getChannelSpies()) {
                    if (!inMessage.contains(spy)) {
                        spy.sendMessage(ChatUtils.getSocialSpyPrefix() + ChatColor.GRAY + "[" + ChatColor.YELLOW + "L" + ChatColor.GRAY + "] "
                                + ChatUtils.getRankString(player) + ChatColor.WHITE + player.getDisplayName() + ": " + event.getMessage());
                    }
                }
                break;
            case TOWN:
                player.sendMessage(ChatColor.RED + "This hasn't been implemented yet! " +
                        "If towns are actually in the game, then please let a staff member know because this should be implemented.");
                break;
            case NATION:
                player.sendMessage(ChatColor.RED + "This hasn't been implemented yet! " +
                        "If nations are actually in the game, then please let a staff member know because this should be implemented.");
                break;
            case STAFF:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("SQChat.staff")) {
                        p.sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "S" + ChatColor.GRAY + "] "
                                + ChatUtils.getRankString(player) + ChatColor.WHITE + player.getDisplayName()
                                + ": " + event.getMessage());
                    }
                }
                break;
        }
    }
}
