package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.pvCore;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.purityvanilla.pvcore.player.GameModes.requestGameMode;

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

        if (args.length == 0) {
            GameMode gameMode;
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
            requestGameMode(plugin, player, gameMode);
            return true;
        }

        return true;
    }

}
