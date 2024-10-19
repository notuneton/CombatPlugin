package org.main.uneton.vanillaCmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.main.uneton.utils.ColorUtils;

public class Guide implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        switch (args.length) {
            case 1:
                if ("commands".equalsIgnoreCase(args[0])) {
                    sendBasicCommands(player);
                    break;
                }

            case 2:
                if ("perms".equalsIgnoreCase(args[0])) {
                    sendPermissions(player);
                    break;
                }

            case 3:
                if ("adminCommands".equalsIgnoreCase(args[0])) {
                    if (player.hasPermission("combat.view.commands")) {
                        sendAdminCommands(player);
                        break;
                    } else {
                        player.sendMessage(ColorUtils.colorize("&6l----------------------------------------------------"));
                        player.sendMessage(ColorUtils.colorize("&x&2&C&0&9&1&6&l>&x&5&C&1&2&2&F&l>&x&C&7&5&3&4&7&l> &7You don't have permission to view that!"));
                        player.sendMessage(ColorUtils.colorize("&6l----------------------------------------------------"));
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
        player.sendMessage(ColorUtils.colorize("&6l----------------------------------------------------"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/cage <player> | Create a cube around the player that the player cannot break."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/clear <player> | Clears items from player inventory, including items."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/crash <player> | Crashes the specified player's game"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/freeze <player> | Cancels the player's movement."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/magictrick <player> | a cool magictrick thats worth it :)"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/gm <player> | Makes the player invulnerable to all faults."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/cure <player> | Heals the player's hearts."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/invsee <player> | See the inventory of other players."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/launch <player> | Throws the player high into the air"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/setspawn | Sets the server spawn point!"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/slippery <player> | Drops Items from the player."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/vanish | Toggles vanish mode."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/wipeprofile | Completely removes all player's own made data and progress from their uuid!"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/time <day|night> Sets the time to day or night."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/tp <player> <target> | Teleports to or from the player."));
        player.sendMessage(ColorUtils.colorize("&6l----------------------------------------------------"));
    }

    private void sendBasicCommands(Player player) {
        player.sendMessage(ColorUtils.colorize("&6l----------------------------------------------------"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/spawn | Teleport to the spawn."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/daily | Command that canbe used daily for a reward!"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/guide <text> | Show this list"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/ping <player> | Check player ping."));

        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/unblock <player> | Unblock a player."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/blocklist | List blocked players."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/block <help> | Block a player."));

        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/puu | Spawns an oak tree at the player's location."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/repair | Repairs the durability of your armor."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/sign | Spawns you a sign. (Can be clicked!)"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/sudo <target> <command> | Executes another player perform a task."));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/trash | Put your stuff in the menu and close it!"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &7/msg <player> <message> | Send a whisper to the player."));
        player.sendMessage(ColorUtils.colorize("&6l----------------------------------------------------"));
    }
    
    private void sendPermissions(Player player) {
        player.sendMessage(ColorUtils.colorize("&6l----------------------------------------------------"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.view.commands"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.see.messages.*"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.cage.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.crash.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.freeze.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.gm.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.heal.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.invsee.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.launch.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.slippery.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.vanish.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.setspawn.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.tp.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.time.sv"));
        player.sendMessage(ColorUtils.colorize("&x&2&E&2&E&2&E&l- &acombat.tp.sv"));
        player.sendMessage(ColorUtils.colorize("&6l----------------------------------------------------"));
    }

    private void sendUsage(Player player) {
        player.sendMessage(ColorUtils.colorize("&6l----------------------------------------------------"));
        String usage = ColorUtils.colorize("&3>&b> &f/guide <commands, perms, adminCommands>");
        player.sendMessage(usage);
        player.sendMessage(ColorUtils.colorize("&6l----------------------------------------------------"));
    }
}
