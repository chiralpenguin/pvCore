package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.pvCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

public class TeleportCommand implements CommandExecutor {
    private final pvCore plugin;

    public TeleportCommand(pvCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.config().getMessage("player-only"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(plugin.config().getMessage("teleport-usage"));
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target != null) {
            String rawMessage = plugin.config().getRawMessage("teleported-to-player");
            TagResolver resolver = TagResolver.resolver(Placeholder.component("target", target.displayName()));
            player.sendMessage(MiniMessage.miniMessage().deserialize(rawMessage, resolver));
            player.teleportAsync(target.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
            return true;
        }

        if (args.length == 1) {
            sender.sendMessage(plugin.config().getMessage("player-not-found"));
            return true;
        }
        // Following logic assumes position teleport
        try {
            World world;
            int xpos = Integer.parseInt(args[0]);
            int ypos = Integer.parseInt(args[1]);
            int zpos = Integer.parseInt(args[2]);

            if (args.length > 3) {
                world = plugin.getServer().getWorld(args[3]);
            } else {
                world = player.getWorld();
            }

            Location loc = new Location(world, xpos, ypos, zpos);
            String rawMessage = plugin.config().getRawMessage("teleported-to-position");
            TagResolver resolver = TagResolver.resolver(Placeholder.component("world", Component.text(world.getName())));
            player.sendMessage(MiniMessage.miniMessage().deserialize(rawMessage, resolver));
            player.teleportAsync(loc);
            return true;

        } catch (NumberFormatException | NullPointerException e) {
            sender.sendMessage(plugin.config().getMessage("teleport-position-usage"));
            return true;
        }
    }
}
