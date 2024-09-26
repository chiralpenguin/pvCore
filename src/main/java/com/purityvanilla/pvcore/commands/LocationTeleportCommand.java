package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvcore.pvCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LocationTeleportCommand implements CommandExecutor {
    private final pvCore plugin;

    public LocationTeleportCommand(pvCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.config().getMessage("player-only"));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(plugin.config().getMessage("location-teleport-usage"));
            return true;
        }

        String locationLabel = args[0].toLowerCase();
        SavedLocation location = plugin.getLocationData().getLocation(player.getUniqueId(), locationLabel);
        if (location == null) {
            sender.sendMessage(plugin.config().getMessage("location-not-found"));
            return true;
        }

        player.teleportAsync(location.getLocation(plugin));
        TagResolver resolver = TagResolver.resolver(Placeholder.component("label", Component.text(location.getLabel())));
        player.sendMessage(plugin.config().getMessage("location-teleported", resolver));
        return true;
    }
}
