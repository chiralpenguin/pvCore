package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.pvCore;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.purityvanilla.pvcore.player.Teleports.TeleportPlayerToPlayer;

public class TeleportHereCommand implements CommandExecutor {
    private pvCore plugin;

    public TeleportHereCommand(pvCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player target)) {
            sender.sendMessage(plugin.config().getMessage("player-only"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(plugin.config().getMessage("teleport-here-usage"));
            return true;
        }

        Player player = plugin.getServer().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(plugin.config().getMessage("player-not-found"));
            return true;
        }

        String rawMessage = plugin.config().getRawMessage("teleported-here");
        TagResolver resolver = TagResolver.resolver(Placeholder.component("player", player.displayName()));
        sender.sendMessage(MiniMessage.miniMessage().deserialize(rawMessage, resolver));

        TeleportPlayerToPlayer(plugin, player, target);
        return true;
    }
}
