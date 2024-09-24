package com.purityvanilla.pvcore.database.cache;

import java.sql.Timestamp;
import java.util.UUID;

public record CachedPlayer(UUID uuid, String name, Timestamp lastSeen) {

}
