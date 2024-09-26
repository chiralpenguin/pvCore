package com.purityvanilla.pvcore.listeners;

import com.purityvanilla.pvcore.pvCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {
    private final pvCore plugin;

    public PlayerQuitListener(pvCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Update player lastSeen
        plugin.getPlayerData().getPlayer(uuid).update();

        // Remove player data from cache and save to database
        plugin.getPlayerData().unloadPlayer(uuid);
        int unloadedLocations = plugin.getLocationData().unloadAllPlayerLocations(uuid);
        if (plugin.config().verbose()) {
            plugin.getLogger().info(String.format("Player %s's data removed from cache", player.getName()));
            plugin.getLogger().info(String.format("Unloaded %s of player %s's locations",
                    unloadedLocations, player.getName()));
        }
    }
}
