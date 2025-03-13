package com.purityvanilla.pvcore.util;

import com.purityvanilla.pvcore.pvCore;
import org.bukkit.entity.Player;

import java.util.*;

public class CacheHelper {
    private final pvCore plugin;

    public CacheHelper(pvCore plugin) {
        this.plugin = plugin;
    }

    public Set<UUID> getOnlineUUIDs() {
        HashSet<UUID> uuids = new HashSet<>();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            uuids.add(player.getUniqueId());
        }

        return uuids;
    }

    public List<UUID> getAbsentUUIDs(Collection<UUID> cachedUUIDs, Set<UUID> onlineUUIDs) {
        ArrayList<UUID> absentUUIDs = new ArrayList<>();
        for (UUID uuid : cachedUUIDs) {
            if (!onlineUUIDs.contains(uuid)) {
                absentUUIDs.add(uuid);
            }
        }

        return  absentUUIDs;
    }

    public List<UUID> getAbsentUUIDs(Collection<UUID> cachedUUIDs) {
        return getAbsentUUIDs(cachedUUIDs, getOnlineUUIDs());
    }
}
