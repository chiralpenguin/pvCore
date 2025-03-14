package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.pvCore;
import com.purityvanilla.pvlib.commands.CommandGuard;
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
    private final pvCore plugin;

    public TeleportHereCommand(pvCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandGuard.senderNotPlayer(sender, plugin.config().getMessage("player-only"))) return true;
        if (CommandGuard.argsSizeInvalid(1, args, sender, plugin.config().getMessage("teleport-here-usage"))) return true;

        Player player = (Player) sender;
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(plugin.config().getMessage("player-not-found"));
            return true;
        }

        TeleportPlayerToPlayer(plugin, target, player);
        TagResolver resolver = TagResolver.resolver(Placeholder.component("player", target.displayName()));
        sender.sendMessage(plugin.config().getMessage("teleported-here", resolver));
        return true;
    }
}
