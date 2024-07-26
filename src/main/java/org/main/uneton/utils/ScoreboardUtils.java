package org.main.uneton.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.main.uneton.Combat;

import java.util.HashMap;
import java.util.UUID;
import static org.main.uneton.Combat.playTimes;

public class ScoreboardUtils {

    private static Combat plugin;
    private static final HashMap<UUID, Integer> kills = new HashMap<>();
    private static final HashMap<UUID, Integer> deaths = new HashMap<>();

    public ScoreboardUtils(Combat plugin) {
        ScoreboardUtils.plugin = plugin;
    }

    public static void startUpdatingScoreboard(Player player, Combat instance) {
        plugin = instance;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    updateScoreboard(player);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public static void updateScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);

        if (objective == null) {
            if (player.hasPermission("op")) {
                String title = ColorUtils.colorize("  &x&4&D&9&8&F&B[&x&6&6&A&6&F&B[ &x&F&F&F&F&F&F&lQ&x&F&F&F&F&F&F&lu&x&F&F&F&F&F&F&lo&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&lt&x&F&F&F&F&F&F&l &x&4&D&9&8&F&B]&x&6&6&A&6&F&B] &8(&b&lDev&8) ");
                objective = scoreboard.registerNewObjective("scoreboard", "dummy", title);
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            } else {
                String title = ColorUtils.colorize("  &x&4&D&9&8&F&B[&x&6&6&A&6&F&B[ &x&F&F&F&F&F&F&lQ&x&F&F&F&F&F&F&lu&x&F&F&F&F&F&F&lo&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&lt&x&F&F&F&F&F&F&l &x&4&D&9&8&F&B]&x&6&6&A&6&F&B]  ");
                objective = scoreboard.registerNewObjective("scoreboard", "dummy", title);
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            }
        }
        clearExistingScores(scoreboard);

        int ping = player.getPing();
        String currentTime = ColorUtils.colorize("&7" + TimeUtils.getCurrentFormattedTime() + " &7(" + (String.format("&3"+ping+"ms")+"&7)"));
        setScore(objective, currentTime, 12);

        setScore(objective, "&7 ", 11);

        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        String totalPlayers = String.valueOf(onlinePlayers);
        String online = ColorUtils.colorize("  &fPlayers &a" + totalPlayers);
        setScore(objective, online, 10);

        UUID uuid = player.getUniqueId();
        int playtimeSeconds = playTimes.getOrDefault(uuid, 0);

        int hours = playtimeSeconds / 3600;
        int minutes = (playtimeSeconds % 3600) / 60;
        int seconds = playtimeSeconds % 60;

        String playtimeString = formatPlaytime(hours, minutes, seconds);
        setScore(objective, playtimeString, 9);

        int playerKills = kills.getOrDefault(uuid, 0);
        int playerDeaths = deaths.getOrDefault(uuid, 0);
        if (playerDeaths > 0) {
            setScore(objective, "  &fDeaths &a" + playerDeaths, 8);
        }
        setScore(objective, "  &fKills &a" + playerKills, 7);

        setScore(objective, "&8 ", 6);

        player.setScoreboard(scoreboard);
    }

    private static void clearExistingScores(Scoreboard scoreboard) {
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }
    }

    private static String formatPlaytime(int hours, int minutes, int seconds) {
        if (seconds >= 60) {
            minutes += seconds / 60;
            seconds %= 60;
        }
        if (minutes >= 60) {
            hours += minutes / 60;
            minutes %= 60;
        }
        return String.format("  &fPlaytime &e%dh %dm %ds", hours, minutes, seconds);
    }

    private static void setScore(Objective objective, String text, int score) {
        Score line = objective.getScore(ColorUtils.colorize(text));
        line.setScore(score);
    }

    public static void addKill(UUID playeruuid) {
        kills.put(playeruuid, deaths.getOrDefault(playeruuid, 0) + 1);
    }

    public static void addDeath(UUID playeruuid) {
        deaths.put(playeruuid, deaths.getOrDefault(playeruuid, 0) + 1);
    }
}