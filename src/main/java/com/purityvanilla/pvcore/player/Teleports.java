package com.purityvanilla.pvcore.player;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.util.CustomTagResolvers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Teleports {
    public static void TeleportPlayerToPlayer(PVCore plugin, Player player, Player target) {
        Location location = target.getLocation();
        handleTeleport(plugin, player, location);
        TagResolver resolver = TagResolver.resolver(Placeholder.component("target", target.displayName()));
        player.sendMessage(plugin.config().getMessage("teleported-to-player", resolver));
    }

    public static void TeleportPlayerToLocation(PVCore plugin, Player player, Location location) {
        handleTeleport(plugin, player, location);
        TagResolver resolver = TagResolver.resolver(Placeholder.component("world", Component.text(location.getWorld().getName())));
        player.sendMessage(plugin.config().getMessage("teleported-to-position", resolver));
    }

    public static void TeleportPlayerToSavedLocation(PVCore plugin, Player player, Location location, String locationLabel) {
        handleTeleport(plugin, player, location);
        TagResolver resolver = CustomTagResolvers.locationLabelResolver(locationLabel);
        player.sendMessage(plugin.config().getMessage("location-teleported", resolver));
    }

    private static void handleTeleport(PVCore plugin, Player player, Location location) {
        if (player.hasPermission("pvcore.back") && !location.equals(player.getLocation())) {
            CachedPlayer cPlayer = plugin.getPlayerData().getPlayer(player.getUniqueId());
            cPlayer.putLastLocation(player.getLocation());
        }

        player.teleportAsync(location, PlayerTeleportEvent.TeleportCause.COMMAND);
    }
}
