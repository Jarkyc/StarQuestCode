package com.spacebeaverstudios.sqcore.objects;


import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TabList extends Tickable {

    Object footer = new ChatComponentText("§3Visit §bstarquest-mc.com/shop §3for perks!");

    public void onTick(){
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        Object header = new ChatComponentText("§9Welcome to StarQuest!\n§bPlayers Online: §f" + Bukkit.getOnlinePlayers().size());
        try{
            Field a = packet.getClass().getDeclaredField("header");
            a.setAccessible(true);
            Field b = packet.getClass().getDeclaredField("footer");
            b.setAccessible(true);

            a.set(packet, header);
            b.set(packet, footer);

            if(Bukkit.getOnlinePlayers().size() == 0) return;
            for(Player player: Bukkit.getOnlinePlayers()){
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        } catch (NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }
    }
}
