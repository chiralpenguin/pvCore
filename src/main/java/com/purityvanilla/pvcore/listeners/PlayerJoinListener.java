package com.purityvanilla.pvcore.listeners;

import com.purityvanilla.pvcore.database.cache.CachedPlayer;
import com.purityvanilla.pvcore.pvCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.format.DateTimeFormatter;

public class PlayerJoinListener implements Listener {
    private final pvCore plugin;

    public PlayerJoinListener(pvCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Call getPlayer() to load player data into cache, initialising if necessary
        CachedPlayer cPlayer = plugin.getPlayerData().getPlayer(player.getUniqueId());
        if (plugin.config().verbose()) {
            plugin.getLogger().info(String.format("Player %s's data was cached (Last seen %s)",
                    player.getName(),
                    cPlayer.lastSeen().toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        }

        // Update CachedPlayer lastSeen and set name to current username
       cPlayer.update(player.getName());
    }
}
