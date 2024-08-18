package org.main.uneton.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;
import org.main.uneton.utils.ScoreboardUtils;

public class Guide implements CommandExecutor {

    public static String title = ColorUtils.colorize(" &x&4&D&9&8&F&B[&x&6&6&A&6&F&B[ &x&F&F&F&F&F&F&lQ&x&F&F&F&F&F&F&lu&x&F&F&F&F&F&F&lo&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&ll&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&le&x&F&F&F&F&F&F&lt&x&F&F&F&F&F&F&l &x&4&D&9&8&F&B]&x&6&6&A&6&F&B] &8");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        switch (args.length) {
            case 1:
                if ("komennot".equalsIgnoreCase(args[0])) {
                    sendBasicCommands(player);
                    break;
                }

            case 2:
                if ("oikeudet".equalsIgnoreCase(args[0])) {
                    sendPermissions(player);
                    break;
                }

            case 3:
                if ("admin".equalsIgnoreCase(args[0])) {
                    if (player.hasPermission("combat.view.commands")) {
                        sendAdminCommands(player);
                        break;
                    } else {
                        player.sendMessage(ColorUtils.colorize("&b----------------------------------------------------"));
                        player.sendMessage(ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &8+ &7You don't have permission to view that"));
                        player.sendMessage(ColorUtils.colorize("&b----------------------------------------------------"));
                        return true;
                    }
                }

            default:
                sendUsage(player);
                break;
        }
        return true;
    }

    private void sendAdminCommands(Player player) {
        player.sendMessage(ColorUtils.colorize("&b----------------------------------------------------"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/cage <player> | Create a cube around the player that the player cannot break."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/crash <player> | Crashes the specified player's game"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/freeze <player> | Cancels the player's movement."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/magictrick <player> | a cool magictrick thats worth it :)"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/gm <player> | Makes the player invulnerable to all faults."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/heal <player> | Heals the player's hearts."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/invsee <player> | See the inventory of other players."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/launch <player> | Throws the player high into the air"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/setspawn | Sets the server spawn point!"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/slippery <player> | Drops Items from the player."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/vanish | Toggles vanish mode."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/time <day|night> Sets the time to day or night."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/tp <player> <target> | Teleports to or from the player."));
        player.sendMessage(ColorUtils.colorize("&b----------------------------------------------------"));
    }

    private void sendBasicCommands(Player player) {
        player.sendMessage(ColorUtils.colorize("&b----------------------------------------------------"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/spawn | Teleport to the spawnpoint."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/daily | Command that canbe used daily for a reward!"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/enderchest | Opens your personal Enderchest."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/guide <text> | Show this list"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/ping <player> | Check player ping."));
        //player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/playtime <player> | Total playtime of a specified player in the game."));

        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/unblock <player> | Unblock a player."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/blocklist | List blocked players."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/block <help> | Block a player."));

        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/puu | Spawns an oak tree at the player's location."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/repair | Repairs the durability of your armor."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/rules | Original Rules Of The Server."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/sign | Spawns you a sign. (Can be clicked!)"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/sudo <target> <command> | Executes another player perform a task."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/trash | Put your stuff in the menu and close it!"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/msg <player> <message> | Send a whisper to the player."));
        player.sendMessage(ColorUtils.colorize("&b----------------------------------------------------"));
    }
    
    private void sendPermissions(Player player) {
        player.sendMessage(ColorUtils.colorize("&b----------------------------------------------------"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.view.commands"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.bypass.afkkick"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.pl.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.cage.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.crash.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.freeze.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.gm.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.heal.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.invsee.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.launch.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.slippery.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.vanish.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.setspawn.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.tp.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.time.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &dcombat.tp.sv"));
        player.sendMessage(ColorUtils.colorize("&b----------------------------------------------------"));
    }

    private void sendUsage(Player player) {
        player.sendMessage(ColorUtils.colorize("&b----------------------------------------------------"));
        String usage = ColorUtils.colorize("&3>&b> &8+ &7usage: &f/guide <komennot, oikeudet, admin>");
        player.sendMessage(usage);
        player.sendMessage(ColorUtils.colorize("&b----------------------------------------------------"));
    }
}
