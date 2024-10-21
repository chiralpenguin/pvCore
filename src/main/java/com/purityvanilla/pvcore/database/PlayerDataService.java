package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.player.CachedPlayer;
import com.purityvanilla.pvcore.pvCore;
import org.bukkit.OfflinePlayer;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataService extends DataService {
    private final PlayerOperator operator;
    private final HashMap<UUID, CachedPlayer> playerCache;

    public PlayerDataService(pvCore plugin, DatabaseConnector database) {
        super(plugin);
        operator = new PlayerOperator(database);

        playerCache = new HashMap<>();
    }

    @Override
    public void saveAll() {
        for (CachedPlayer cPlayer : playerCache.values()) {
            operator.savePlayerData(cPlayer);
        }
    }

    public boolean isCached(UUID uuid) {
        return playerCache.containsKey(uuid);
    }

    public CachedPlayer getPlayer(UUID uuid) {
        if (isCached(uuid)) {
            return playerCache.get(uuid);
        }

        OfflinePlayer player = plugin.getServer().getOfflinePlayer(uuid);
        // If UUID invalid or player has not joined server
        if (player.getName() == null) {
            return null;
        }

        CachedPlayer cPlayer = operator.getPlayerData(uuid);
        // Initialise player if valid but not yet stored in database
        if (cPlayer == null) {
            cPlayer = new CachedPlayer(player.getUniqueId(), player.getName(), new Timestamp(player.getLastSeen()));
            operator.savePlayerData(cPlayer);
        }

        playerCache.put(uuid, cPlayer);
        return cPlayer;
    }

    public void unloadPlayer(UUID uuid) {
        CachedPlayer cPlayer = getPlayer(uuid);
        operator.savePlayerData(cPlayer);
        playerCache.remove(uuid);
    }
}
