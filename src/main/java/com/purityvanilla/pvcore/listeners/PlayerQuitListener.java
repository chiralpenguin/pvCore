package com.purityvanilla.pvcore.listeners;

import com.purityvanilla.pvcore.pvCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private pvCore plugin;

    public PlayerQuitListener(pvCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();


        // Remove player data from cache and save to database
    }
}
