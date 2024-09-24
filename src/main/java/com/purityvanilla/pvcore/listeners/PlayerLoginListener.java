package com.purityvanilla.pvcore.listeners;

import com.purityvanilla.pvcore.pvCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {
    private pvCore plugin;

    public PlayerLoginListener(pvCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        // Load player data into cache, initialising if necessary
    }
}
