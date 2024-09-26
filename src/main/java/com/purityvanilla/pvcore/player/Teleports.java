package com.purityvanilla.pvcore.player;

import com.purityvanilla.pvcore.pvCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Teleports {
    public static void TeleportPlayerToPlayer(pvCore plugin, Player player, Player target) {
        player.teleportAsync(target.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);

        String rawMessage = plugin.config().getRawMessage("teleported-to-player");
        TagResolver resolver = TagResolver.resolver(Placeholder.component("target", target.displayName()));
        player.sendMessage(MiniMessage.miniMessage().deserialize(rawMessage, resolver));
    }

    public static void TeleportPlayerToLocation(pvCore plugin, Player player, Location location) {
        player.teleportAsync(location, PlayerTeleportEvent.TeleportCause.COMMAND);

        String rawMessage = plugin.config().getRawMessage("teleported-to-position");
        TagResolver resolver = TagResolver.resolver(Placeholder.component("world", Component.text(location.getWorld().getName())));
        player.sendMessage(MiniMessage.miniMessage().deserialize(rawMessage, resolver));
    }
}
