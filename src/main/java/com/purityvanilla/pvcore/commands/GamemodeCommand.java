package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.pvCore;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.purityvanilla.pvcore.player.Gamemodes.*;

public class GamemodeCommand implements CommandExecutor {
    private final pvCore plugin;

    public GamemodeCommand(pvCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.config().getMessage("player-only"));
            return true;
        }

        // Shortcuts for changing own gamemode - changing gamemode of others requires full command
        GameMode gameMode;
        if (args.length == 0) {
            switch (label) {
                case "gma":
                    gameMode = GameMode.ADVENTURE;
                    break;
                case "gmc":
                    gameMode = GameMode.CREATIVE;
                    break;
                case "gms":
                    gameMode = GameMode.SURVIVAL;
                    break;
                case "gmsp":
                    gameMode = GameMode.SPECTATOR;
                    break;
                default:
                    sender.sendMessage(plugin.config().getMessage("gamemode-usage"));
                    return true;
            }

            requestGamemode(player, gameMode);
            return true;
        }

        // Full gamemode command logic below
        switch (args[0].toLowerCase()) {
            case "adventure":
                gameMode = GameMode.ADVENTURE;
                break;
            case "creative":
                gameMode = GameMode.CREATIVE;
                break;
            case "survival":
                gameMode = GameMode.SURVIVAL;
                break;
            case "spectator":
                gameMode = GameMode.SPECTATOR;
                break;
            default:
                sender.sendMessage(plugin.config().getMessage("gamemode-usage"));
                return true;
        }

        // If only gamemode is provided, set the player's gamemode
        if (args.length == 1) {
            requestGamemode(player, gameMode);
            return true;
        }

        if (!player.hasPermission("pvcore.gamemode.others") || !player.hasPermission(getGamemodePermission(gameMode))) {
            player.sendMessage(plugin.config().getMessage("no-permission"));
            return true;
        }

        // Try to obtain a Player from the second argument
        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(plugin.config().getMessage("player-not-found"));
            return true;
        }

        updateOthersGamemode(plugin, player, target, gameMode);
        return true;
    }

    public void requestGamemode(Player player, GameMode gameMode) {
        if (player.hasPermission(getGamemodePermission(gameMode))) {
            updateGamemode(plugin, player, gameMode);
        } else {
            player.sendMessage(plugin.config().getMessage("no-permission"));
        }
    }
}
