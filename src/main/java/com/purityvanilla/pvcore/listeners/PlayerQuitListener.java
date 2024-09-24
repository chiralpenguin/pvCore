package com.purityvanilla.pvcore.listeners;

import com.purityvanilla.pvcore.pvCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final pvCore plugin;

    public PlayerQuitListener(pvCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Update player lastSeen
        plugin.getPlayerData().getPlayer(player.getUniqueId()).update();

        // Remove player data from cache and save to database
        plugin.getPlayerData().unloadPlayer(player.getUniqueId());
        if (plugin.config().verbose()) {
            plugin.getLogger().info(String.format("Player %s's data removed from cache", player.getName()));
        }
    }
}
