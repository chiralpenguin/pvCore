package com.purityvanilla.pvcore.listeners;

import com.purityvanilla.pvcore.player.CachedPlayer;
import com.purityvanilla.pvcore.pvCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PlayerJoinListener implements Listener {
    private final pvCore plugin;

    public PlayerJoinListener(pvCore plugin) {
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
    }
}
