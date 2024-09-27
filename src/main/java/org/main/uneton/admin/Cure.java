package org.main.uneton.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

import java.util.Arrays;

import static org.main.uneton.Combat.perm;
import static org.main.uneton.utils.SoundsUtils.playCancerSound;

public class Cure implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("combat.heal.sv")) {
            player.sendMessage(ColorUtils.colorize(Arrays.toString(perm) + command.getName()));
            playCancerSound(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ColorUtils.colorize("&d&lHEALED! &7You healed yourself, :)"));
            player.setHealth(20.0);
            player.setFoodLevel(20);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(ColorUtils.colorize("&c&lWHO?! &7Couldn't find a player with username "+ target.getName() +"!"));
                return true;
            }

            target.setHealth(20.0);
            player.setFoodLevel(20);
            String success = ColorUtils.colorize("&x&2&E&2&E&2&E&l>&x&2&0&8&1&8&A&l>&x&3&6&D&D&E&E&l> &x&8&8&8&3&A&4- ");
            target.sendMessage(success + ColorUtils.colorize("&7You have been healed by &f" + player.getName() + "&7."));
            player.sendMessage(success + ColorUtils.colorize("&7You have healed &f" + target.getName() + "&7."));
            return true;
        }
        return false;
    }
}
