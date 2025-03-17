package com.purityvanilla.pvcore.api.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.UUID;

public interface PlayerAPI {

    Component getPlayerNickname(Player player);

    void setPlayerNickname(Player player, Component nickname);

    /**
     * Get the prefix from the player's primary group in LuckPerms
     * @param player
     * @return Component of prefix string parsed with either MiniMessage or legacy ampersand
     */
    Component getPlayerPrefix(Player player);

    /**
     * Get the suffix from the player's primary group in LuckPerms
     * @param player
     * @return Component of suffix string parsed with either MiniMessage or legacy ampersand
     */
    Component getPlayerSuffix(Player player);

    /**
     * Get the lastSeen value of a player from their UUID
     * @param uuid
     * @return Timestamp when player was last online
     */
    Timestamp getPlayerLastSeen(UUID uuid);
}
