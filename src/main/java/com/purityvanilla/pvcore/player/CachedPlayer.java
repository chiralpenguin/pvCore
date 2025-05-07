package com.purityvanilla.pvcore.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;

import java.sql.Timestamp;
import java.util.*;

public class CachedPlayer {
    private final UUID uuid;
    private String name;
    private Timestamp lastSeen;
    private Component nickname;
    private Set<UUID> ignoredPlayers;
    private List<Location> previousLocations;

    public CachedPlayer(UUID uuid, String name, Timestamp lastSeen, String nickString, Set<UUID> ignoredPlayers) {
        this.uuid = uuid;
        this.name = name;
        this.lastSeen = lastSeen;
        this.nickname = (nickString == null) ? null : MiniMessage.miniMessage().deserialize(nickString);
        this.ignoredPlayers = (ignoredPlayers == null) ? new HashSet<>() : ignoredPlayers;

        // Transitory Attributes
        this.previousLocations = new ArrayList<>();
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

    public boolean ignores(UUID uuid) {
        return ignoredPlayers.contains(uuid);
    }

    public boolean hasPreviousLocation() {
        return !this.previousLocations.isEmpty();
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

    public void putLastLocation(Location location) {
        this.previousLocations.add(location);

        // Enforce list length cap
        if (previousLocations.size() > 512) {
            previousLocations.removeFirst();
        }
    }

    public Location removeLastLocation() {
        return this.previousLocations.removeLast();
    }
}
