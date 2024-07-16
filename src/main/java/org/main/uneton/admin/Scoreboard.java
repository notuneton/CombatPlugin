package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;
import org.main.uneton.utils.ColorUtils;

import static org.main.uneton.utils.ScoreboardUtils.updateScoreboard;

public class Scoreboard implements CommandExecutor {

    private static Combat plugin;
    public Scoreboard(Combat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length > 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&2&E&2&E&2&E&l- &7Usage: &x&A&B&A&B&A&B/&x&A&B&A&B&A&Bs&x&A&B&A&B&A&Bb");
            player.sendMessage(usage);
            return true;
        }

        if (!player.hasPermission("combat.sb.sv")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to run /" + command.getName() + ".");
            return true;
        }

        if (plugin == null) {
            throw new IllegalStateException("ScoreboardUtils not initialized with plugin instance.");
        }
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
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

        return true;
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
