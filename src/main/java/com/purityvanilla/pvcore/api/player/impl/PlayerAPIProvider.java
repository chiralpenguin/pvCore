package com.purityvanilla.pvcore.api.player.impl;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.api.player.PlayerAPI;
import com.purityvanilla.pvcore.database.PlayerDataService;
import com.purityvanilla.pvcore.player.Nicknames;
import com.purityvanilla.pvcore.util.FormatCodeParser;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.*;

public class PlayerAPIProvider implements PlayerAPI {
    private final PlayerDataService playerData;
    private final LuckPerms luckPerms;

    public PlayerAPIProvider(PVCore plugin) {
        this.playerData = plugin.getPlayerData();
        this.luckPerms = plugin.getLuckPerms();
    }

    @Override
    public String getPlayerUsername(UUID uuid) {
        return playerData.getPlayer(uuid).name();
    }

    @Override
    public String getPlayerUsername(Player player) {
        return getPlayerUsername(player.getUniqueId());
    }

    @Override
    public Component getPlayerNickname(Player player) {
        return playerData.getPlayer(player.getUniqueId()).nickname();
    }

    @Override
    public void setPlayerNickname(Player player, Component nickname) {
        playerData.getPlayer(player.getUniqueId()).setNickname(nickname);
    }

    @Override
    public boolean playerHasNickname(Player player) {
        return playerData.getPlayer(player.getUniqueId()).hasNick();
    }

    @Override
    public void removePlayerNickname(Player player) {
        Nicknames.removePlayerNickname(player, playerData);
    }

    @Override
    public Component getPlayerPrefix(Player player) {
        return getPlayerMetaComponent(player, MetaComponent.PREFIX);
    }

    @Override
    public Component getPlayerSuffix(Player player) {
        return getPlayerMetaComponent(player, MetaComponent.SUFFIX);
    }

    @Override
    public Timestamp getPlayerLastSeen(UUID uuid) {
        return playerData.getPlayer(uuid).lastSeen();
    }

    @Override
    public Timestamp getPlayerLastSeen(Player player) {
        return getPlayerLastSeen(player.getUniqueId());
    }

    @Override
    public UUID getUUIDFromName(String name) {
        return playerData.getUUIDFromName(name);
    }

    @Override
    public boolean isPlayerIgnored(Player player, Player ignoredPlayer) {
        return playerData.getPlayer(player.getUniqueId()).ignores(ignoredPlayer.getUniqueId());
    }

    @Override
    public Set<UUID> getIgnoredPlayers(Player player) {
       return new HashSet<>(playerData.getPlayer(player.getUniqueId()).getIgnoredPlayers());
    }

    @Override
    public void ignorePlayer(Player player, Player ignoredPlayer) {
        playerData.getPlayer(player.getUniqueId()).ignorePlayer(ignoredPlayer.getUniqueId());
    }

    @Override
    public void unignorePlayer(Player player, Player ignoredPlayer) {
        playerData.getPlayer(player.getUniqueId()).unignorePlayer(ignoredPlayer.getUniqueId());
    }

    @Override
    public Set<String> getPlayerPermissions(UUID uuid) {
        Set<String> permissions = new HashSet<>();
        Collection<Node> nodes = luckPerms.getUserManager().getUser(uuid).resolveDistinctInheritedNodes(QueryOptions.defaultContextualOptions());

        for (Node node : nodes) {
            if (node.isNegated() || node.hasExpired()) continue;
            permissions.add(node.getKey());
        }

        return permissions;
    }

    @Override
    public Set<String> getPlayerPermissions(Player player) {
        return getPlayerPermissions(player.getUniqueId());
    }

    private enum MetaComponent {
        PREFIX,
        SUFFIX
    }

    private Component getPlayerMetaComponent(Player player, MetaComponent meta) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        if (user == null) {
            return Component.empty();
        }

        CachedMetaData metaData = user.getCachedData().getMetaData();
        String metaString = "";

        switch (meta) {
            case MetaComponent.PREFIX -> metaString = metaData.getPrefix();
            case MetaComponent.SUFFIX -> metaString = metaData.getSuffix();
        }

        if (metaString == null) {
            return Component.empty();
        }

        return FormatCodeParser.deserialiseFormatString(metaString);
    }
}