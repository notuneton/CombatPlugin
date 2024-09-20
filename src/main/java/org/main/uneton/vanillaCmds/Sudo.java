package org.main.uneton.vanillaCmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.Arrays;
import java.util.List;

import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Sudo implements CommandExecutor {

    private final List<String> allowedPlayers = Arrays.asList(
            "unetonn"
            // Add more usernames as needed
    );

    public static String warn = ColorUtils.colorize("&4>&c> &8+ &7");
    public static String success = ColorUtils.colorize("&2>&a> &8+ ");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        if (!allowedPlayers.contains(player.getName())) {
            player.sendMessage(ColorUtils.colorize("&cIllegalAccessError: &7This command can only be executed as a specific person! Here is the list of players that can execute this command "+ allowedPlayers));
            playCancerSound(player);
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &8+ &x&A&B&A&B&A&B/&x&A&B&A&B&A&Bs&x&A&B&A&B&A&Bu&x&A&B&A&B&A&Bd&x&A&B&A&B&A&Bo &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bt&x&A&B&A&B&A&Ba&x&A&B&A&B&A&Br&x&A&B&A&B&A&Bg&x&A&B&A&B&A&Be&x&A&B&A&B&A&Bt&x&A&B&A&B&A&B> &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bc&x&A&B&A&B&A&Bo&x&A&B&A&B&A&Bm&x&A&B&A&B&A&Bm&x&A&B&A&B&A&Ba&x&A&B&A&B&A&Bn&x&A&B&A&B&A&Bd&x&A&B&A&B&A&B>");
            player.sendMessage(usage);
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(warn + "That player does not exist.");
            return true;
        }

        if (args.length >= 2) {
            String user = args[0];
            Player cmd = Bukkit.getServer().getPlayer(user);
            if (cmd != null) {
                StringBuilder message = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }
                cmd.chat(message.toString());
                player.sendMessage(success + ColorUtils.colorize("&7executed command '&a"+message+"&7' to " + "&e"+target.getName()));
            }
        }
        return true;
    }
}