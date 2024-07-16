package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.uneton.Combat;

public class ScoreboardUtils {

    private static Combat plugin;
    public ScoreboardUtils(Combat plugin) {
        ScoreboardUtils.plugin = plugin;
    }

    public static void updateScoreboard(Player player) {
        if (plugin == null) {
            throw new IllegalStateException("ScoreboardUtils not initialized with plugin instance.");
        }
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("scoreboard", "dummy", ChatColor.BLUE.toString()+ChatColor.BOLD+("  PROTOTYPE  "));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        setScore(objective, " ", 12);
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        String online = ChatColor.WHITE + "  &9Online &f"+ onlinePlayers;
        setScore(objective, online, 11);

        int hours = plugin.getConfig().getInt("hour." + player.getUniqueId());
        int minutes = plugin.getConfig().getInt("minute." + player.getUniqueId());
        String playtimeString = ChatColor.WHITE + "  &9Playtime &7" + hours + "h " + minutes + "m";
        setScore(objective, playtimeString, 10);

        setScore(objective," ", 9);
        player.setScoreboard(board);
    }

    private static void setScore(Objective objective, String text, int score) {
        Score line = objective.getScore(ChatColor.translateAlternateColorCodes('&', text));
        line.setScore(score);
    }

    public static void startUpdatingScoreboard(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    updateScoreboard(player);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // run every second
    }
}

