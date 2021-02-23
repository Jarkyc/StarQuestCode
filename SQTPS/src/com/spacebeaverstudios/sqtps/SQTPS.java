package com.spacebeaverstudios.sqtps;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

@SuppressWarnings("unused")
public class SQTPS extends JavaPlugin {
    private Scoreboard scoreboard;
    private Objective objective;

    public void onEnable() {
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        if (scoreboard.getObjective("TPS") != null) objective = scoreboard.getObjective("TPS");
        else objective = scoreboard.registerNewObjective("TPS", "tps", "tps");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
            for (String score : scoreboard.getEntries()) scoreboard.resetScores(score);
            objective.getScore(("tps: " + getServer().getTPS()[0]).substring(0, 9)).setScore(0);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }, 20, 20);
    }

    public void onDisable() {
        for (String score : scoreboard.getEntries()) scoreboard.resetScores(score);
    }
}
