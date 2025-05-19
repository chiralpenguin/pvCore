package com.purityvanilla.pvcore.util;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class CacheHelper {
    public static Set<UUID> getOnlineUUIDs(JavaPlugin plugin) {
        HashSet<UUID> uuids = new HashSet<>();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            uuids.add(player.getUniqueId());
        }

        return uuids;
    }

    public static List<UUID> getAbsentUUIDs(Collection<UUID> cachedUUIDs, Set<UUID> onlineUUIDs) {
        ArrayList<UUID> absentUUIDs = new ArrayList<>();
        for (UUID uuid : cachedUUIDs) {
            if (!onlineUUIDs.contains(uuid)) {
                absentUUIDs.add(uuid);
            }
        }

        return  absentUUIDs;
    }

    public static List<UUID> getAbsentUUIDs(Collection<UUID> cachedUUIDs, JavaPlugin plugin) {
        return getAbsentUUIDs(cachedUUIDs, getOnlineUUIDs(plugin));
    }
}
