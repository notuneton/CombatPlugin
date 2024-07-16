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
        Objective objective = board.registerNewObjective("scoreboard", "dummy", ColorUtils.colorize("  &x&4&5&9&2&A&E&lQ&x&4&4&8&B&A&6&lu&x&4&3&8&4&9&E&lo&x&4&2&7&D&9&6&ll&x&4&1&7&6&8&E&ll&x&4&1&7&0&8&7&le&x&4&0&6&9&7&F&le&x&3&F&6&2&7&7&lt&x&3&E&5&B&6&F&l.&x&3&D&5&4&6&7&lc&x&3&C&4&D&5&F&lo  "));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        setScore(objective, " ", 15);
        setScore(objective," ", 14);
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        String online = ChatColor.WHITE + "  &9Online &f"+ onlinePlayers;
        setScore(objective, online, 13);

        int hours = plugin.getConfig().getInt("hour." + player.getUniqueId());
        int minutes = plugin.getConfig().getInt("minute." + player.getUniqueId());
        String playtimeString = ChatColor.WHITE + "  &9Playtime &7" + hours + "h " + minutes + "m";
        setScore(objective, playtimeString, 12);

        setScore(objective," ", 11);
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
                    this.cancel(); // Cancel the task if the player is offline
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Run every second
    }
}

