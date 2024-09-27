package org.main.uneton.vanillaCmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.Combat;
import org.main.uneton.utils.ColorUtils;

import java.util.Arrays;
import java.util.List;

import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Sudo implements CommandExecutor {

    private final List<String> allowed = Arrays.asList(
            "unetonn"
    );
    public static String success = ColorUtils.colorize("&3>&b> &x&8&8&8&3&A&4- ");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        if (!allowed.contains(player.getName())) {
            player.sendMessage(ColorUtils.colorize("&cIllegalAccessError: &7This command can only be executed as a specific person! Here is the list of players that can execute this command "+ allowed));
            playCancerSound(player);
            return true;
        }

        if (args.length == 0) {
            String usage = ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &x&8&8&8&3&A&4- &x&A&B&A&B&A&B/&x&A&B&A&B&A&Bs&x&A&B&A&B&A&Bu&x&A&B&A&B&A&Bd&x&A&B&A&B&A&Bo &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bt&x&A&B&A&B&A&Ba&x&A&B&A&B&A&Br&x&A&B&A&B&A&Bg&x&A&B&A&B&A&Be&x&A&B&A&B&A&Bt&x&A&B&A&B&A&B> &x&A&B&A&B&A&B<&x&A&B&A&B&A&Bc&x&A&B&A&B&A&Bo&x&A&B&A&B&A&Bm&x&A&B&A&B&A&Bm&x&A&B&A&B&A&Ba&x&A&B&A&B&A&Bn&x&A&B&A&B&A&Bd&x&A&B&A&B&A&B>");
            player.sendMessage(usage);
            return true;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ColorUtils.colorize("&c&lWHO?! &7Couldn't find a player with username "+ target.getName() +"!"));
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
                player.sendMessage(success + ColorUtils.colorize("&7executed command ' &f"+message+"&7' to " + "&a"+target.getName()));
            }
        }
        return true;
    }
}