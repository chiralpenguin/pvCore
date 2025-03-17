package com.purityvanilla.pvcore.database;

import com.purityvanilla.pvcore.player.CachedPlayer;
import com.purityvanilla.pvcore.PVCore;
import org.bukkit.OfflinePlayer;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataService extends DataService {
    private final PlayerOperator operator;
    private final ConcurrentHashMap<UUID, CachedPlayer> playerCache;

    public PlayerDataService(PVCore plugin, DatabaseConnector database) {
        super(plugin);
        operator = new PlayerOperator(database);

        playerCache = new ConcurrentHashMap<>();
    }

    @Override
    public void saveAll() {
        for (CachedPlayer cPlayer : playerCache.values()) {
            cPlayer.update();
            operator.savePlayerData(cPlayer);
        }
    }

    @Override
    public void cleanCache() {
        for (UUID absentPlayer : plugin.getCacheHelper().getAbsentUUIDs(playerCache.keySet())) {
            unloadPlayer(absentPlayer);
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
        if (player.getLastSeen() == 0) {
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

    public CachedPlayer getPlayer(String name) {
        UUID uuid = operator.getUUIDFromName(name);

        // Player name does not exist in database
        if (uuid == null) {
            return null;
        }

        return getPlayer(uuid);
    }

    public void unloadPlayer(UUID uuid) {
        CachedPlayer cPlayer = getPlayer(uuid);
        operator.savePlayerData(cPlayer);
        playerCache.remove(uuid);
    }
}
