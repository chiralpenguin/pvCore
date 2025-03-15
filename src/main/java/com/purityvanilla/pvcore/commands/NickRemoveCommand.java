package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.player.Nicknames;
import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.util.CustomTagResolvers;
import com.purityvanilla.pvlib.commands.CommandGuard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NickRemoveCommand implements CommandExecutor {
    private final PVCore plugin;

    public NickRemoveCommand(PVCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (CommandGuard.senderNotPlayer(sender, plugin.config().getMessage("nick-remove-usage"))) return true;

            Player player = (Player) sender;
            Nicknames.removePlayerNickname(player, plugin.getPlayerData());
            player.sendMessage(plugin.config().getMessage("nick-removed"));
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(plugin.config().getMessage("player-not-found"));
            return true;
        }

        Nicknames.removePlayerNickname(target, plugin.getPlayerData());
        sender.sendMessage(plugin.config().getMessage("nick-removed-other", CustomTagResolvers.playerResolver(target.getName())));
        return true;
    }
}
