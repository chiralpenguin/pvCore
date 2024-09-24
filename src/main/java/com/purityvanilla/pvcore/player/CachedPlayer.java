package com.purityvanilla.pvcore.player;

import java.sql.Timestamp;
import java.util.UUID;

public class CachedPlayer {
    private final UUID uuid;
    private String name;
    private Timestamp lastSeen;

    public CachedPlayer(UUID uuid, String name, Timestamp lastSeen) {
        this.uuid = uuid;
        this.name = name;
        this.lastSeen = lastSeen;
    }

    public UUID uuid() {
        return uuid;
    }

    public String name() {
        return name;
    }

    public Timestamp lastSeen() {
        return lastSeen;
    }

    public void update(String name) {
        this.name = name;
        this.lastSeen = new Timestamp(System.currentTimeMillis());
    }

    public void update() {
        update(this.name);
    }
}
