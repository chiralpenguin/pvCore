package com.purityvanilla.pvcore.api.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

public interface PlayerAPI {
    /**
     * Get the most recent username of a player from their UUID
     * @param uuid
     * @return String username
     */
    String getPlayerUsername(UUID uuid);

    /**
     * Get the most recent username from a Player
     * @param player
     * @return String username
     */
    String getPlayerUsername(Player player);

    Component getPlayerNickname(Player player);

    void setPlayerNickname(Player player, Component nickname);

    /**
     * Get the current nickname status of a Player
     * @param player
     * @return false if the player's nickname is `null`, true otherwise
     */
    boolean playerHasNickname(Player player);

    /**
     * Remove the nickname of the current player (set it to null)
     * @param player
     */
    void removePlayerNickname(Player player);

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

    /**
     * Get the lastSeen value of a player
     * @param player
     * @return Timestamp when player was last online
     */
    Timestamp getPlayerLastSeen(Player player);

    /**
     * Get the UUID of the player who most recently connected from a given username
     * @param name username to search
     * @return UUID of player
     */
    UUID getUUIDFromName(String name);

    /**
     * Check if the player has another player in their ignore list
     * @param player player whose ignore list is checked
     * @param ignoredPlayer player to check presence of
     * @return True if ignoredUuid exists within the player's ignore list
     */
    boolean isPlayerIgnored(Player player, Player ignoredPlayer);

    /**
     * Get a copy of the player's ignore list for iteration
     * @param player player whose ignore list is retrieved
     * @return Set of player UUIDs
     */
    Set<UUID> getIgnoredPlayers(Player player);

    /**
     * Add a new player a player's ignore list
     * @param player player whose ignore list is updated
     * @param ignoredPlayer player to ignore
     */
    void ignorePlayer(Player player, Player ignoredPlayer);

    /**
     * Remove a player from a player's ignore list
     * @param player player whose ignore list is updated
     * @param ignoredPlayer player to unignore
     */
    void unignorePlayer(Player player, Player ignoredPlayer);

    Set<String> getPlayerPermissions(UUID uuid);

    Set<String> getPlayerPermissions(Player player);
}
