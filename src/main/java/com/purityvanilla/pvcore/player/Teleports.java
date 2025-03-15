package com.purityvanilla.pvcore.player;

import com.purityvanilla.pvcore.PVCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Teleports {
    public static void TeleportPlayerToPlayer(PVCore plugin, Player player, Player target) {
        player.teleportAsync(target.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
        TagResolver resolver = TagResolver.resolver(Placeholder.component("target", target.displayName()));
        player.sendMessage(plugin.config().getMessage("teleported-to-player", resolver));
    }

    public static void TeleportPlayerToLocation(PVCore plugin, Player player, Location location) {
        player.teleportAsync(location, PlayerTeleportEvent.TeleportCause.COMMAND);
        TagResolver resolver = TagResolver.resolver(Placeholder.component("world", Component.text(location.getWorld().getName())));
        player.sendMessage(plugin.config().getMessage("teleported-to-position", resolver));
    }
}
