package com.purityvanilla.pvcore.listeners;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.player.CachedPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private PVCore plugin;

    public PlayerDeathListener(PVCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("pvcore.back.on-death")) {
            return;
        }

        CachedPlayer cPlayer = plugin.getPlayerData().getPlayer(player.getUniqueId());
        cPlayer.putLastLocation(event.getPlayer().getLocation());
    }
}
