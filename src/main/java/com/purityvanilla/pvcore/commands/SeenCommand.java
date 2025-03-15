package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.player.CachedPlayer;
import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.util.CustomTagResolvers;
import com.purityvanilla.pvlib.commands.CommandGuard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;

public class SeenCommand implements CommandExecutor {
    private final PVCore plugin;

    public SeenCommand(PVCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandGuard.argsSizeInvalid(1, args, sender, plugin.config().getMessage("seen-usage"))) return true;

        CachedPlayer cPlayer = plugin.getPlayerData().getPlayer(args[0]);
        if (cPlayer == null) {
            sender.sendMessage(plugin.config().getMessage("player-not-found"));
            return true;
        }

        String dateString = cPlayer.lastSeen().toLocalDateTime().format((DateTimeFormatter.ofPattern("d MMMM yyyy")));
        sender.sendMessage(plugin.config().getMessage("player-seen", CustomTagResolvers.seenResolver(cPlayer.name(), dateString)));
        return true;
    }
}
