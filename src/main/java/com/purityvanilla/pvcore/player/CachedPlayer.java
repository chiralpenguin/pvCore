package com.purityvanilla.pvcore.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;

import java.sql.Timestamp;
import java.util.UUID;

public class CachedPlayer {
    private final UUID uuid;
    private String name;
    private Timestamp lastSeen;
    private Component nickname;

    public CachedPlayer(UUID uuid, String name, Timestamp lastSeen, String nickString) {
        this.uuid = uuid;
        this.name = name;
        this.lastSeen = lastSeen;

        if (nickString != null) {
            this.nickname = MiniMessage.miniMessage().deserialize(nickString);
        }
    }

    public CachedPlayer(UUID uuid, String name, Timestamp lastSeen) {
        this(uuid, name, lastSeen, null);
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
        return MiniMessage.miniMessage().serialize(nickname);
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
}
