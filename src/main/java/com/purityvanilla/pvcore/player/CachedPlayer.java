package com.purityvanilla.pvcore.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CachedPlayer {
    private final UUID uuid;
    private String name;
    private Timestamp lastSeen;
    private Component nickname;
    private Set<UUID> ignoredPlayers;

    public CachedPlayer(UUID uuid, String name, Timestamp lastSeen, String nickString, Set<UUID> ignoredPlayers) {
        this.uuid = uuid;
        this.name = name;
        this.lastSeen = lastSeen;
        this.nickname = (nickString == null) ? null : MiniMessage.miniMessage().deserialize(nickString);
        this.ignoredPlayers = (ignoredPlayers == null) ? new HashSet() : ignoredPlayers;
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

    public boolean hasNick() {
        return nickname != null;
    }

    public Component nickname() {
        return nickname;
    }

    public String getNickString() {
        if (nickname == null) {
            return null;
        }
        return MiniMessage.miniMessage().serialize(nickname);
    }

    public Set<UUID> getIgnoredPlayers() {
        return ignoredPlayers;
    }

    public void update(String name) {
        this.name = name;
        this.lastSeen = new Timestamp(System.currentTimeMillis());
    }

    public void update() {
        update(this.name);
    }

    public void setNickname(Component nickname) {
        this.nickname = nickname;
    }

    public void removeNickname() {
        this.nickname = null;
    }

    public void ignorePlayer(UUID uuid) {
        this.ignoredPlayers.add(uuid);
    }

    public void unignorePlayer(UUID uuid) {
        this.ignoredPlayers.remove(uuid);
    }
}
