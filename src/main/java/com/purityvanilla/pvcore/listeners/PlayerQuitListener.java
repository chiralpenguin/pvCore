package com.purityvanilla.pvcore.listeners;

import com.purityvanilla.pvcore.PVCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {
    private final PVCore plugin;

    public PlayerQuitListener(PVCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Update player username and last seen
        plugin.getPlayerData().getPlayer(uuid).update(player.getName());
        plugin.getServer().getAsyncScheduler().runNow(plugin, (task) -> {
            unloadPlayerData(uuid, player.getName());
        });
    }

    private void unloadPlayerData(UUID uuid, String name) {
        // Remove player data from cache and save to database
        plugin.getPlayerData().unloadPlayer(uuid);
        int unloadedLocations = plugin.getLocationData().unloadAllPlayerLocations(uuid);
        if (plugin.config().verbose()) {
            plugin.getLogger().info(String.format("Player %s's data removed from cache", name));
            plugin.getLogger().info(String.format("Unloaded %s of player %s's locations", unloadedLocations, name));
        }
    }
}
