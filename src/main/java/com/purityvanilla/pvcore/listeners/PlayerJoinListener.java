package com.purityvanilla.pvcore.listeners;

import com.purityvanilla.pvcore.player.CachedPlayer;
import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvlib.util.FormatCodeParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PlayerJoinListener implements Listener {
    private final PVCore plugin;

    public PlayerJoinListener(PVCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        // Call getPlayer() to load player data into cache, initialising if necessary
        CachedPlayer cPlayer = plugin.getPlayerData().getPlayer(uuid);
        int loadedLocations = plugin.getLocationData().loadAllPlayerLocations(uuid);
        if (plugin.config().verbose()) {
            plugin.getLogger().info(String.format("Player %s's data was cached (Last seen %s)",
                    player.getName(),
                    cPlayer.lastSeen().toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME)));
            plugin.getLogger().info(String.format("Loaded %s locations for player %s",
                    loadedLocations, player.getName()));
        }

        // Update CachedPlayer lastSeen and set name to current username
       cPlayer.update(player.getName());

        if (cPlayer.hasNick()) {
            applyNickname(cPlayer, player);
        }
    }

    private void applyNickname(CachedPlayer cPlayer, Player player) {
        // Ensure player still has permission for any format codes present in nickname
        Component nickname = cPlayer.nickname();
        String nickString = LegacyComponentSerializer.legacyAmpersand().serialize(nickname);
        Component updatedNickname = FormatCodeParser.parseString(nickString, player, FormatCodeParser.Context.NICKNAME);

        if (!nickname.equals(updatedNickname)) {
            cPlayer.setNickname(updatedNickname);
        }

        // Set displayname to nickname if it exists and the player is permitted
        if (player.hasPermission("pvcore.nickname")) {
            player.displayName(updatedNickname);
        }
    }
}
