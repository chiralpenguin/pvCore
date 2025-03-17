package com.purityvanilla.pvcore.api.player.impl;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.api.player.PlayerAPI;
import com.purityvanilla.pvcore.database.PlayerDataService;
import com.purityvanilla.pvcore.util.FormatCodeParser;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

import java.sql.Timestamp;

public class PlayerAPIProvider implements PlayerAPI {
    private final PlayerDataService playerData;
    private final LuckPerms luckPerms;

    public PlayerAPIProvider(PVCore plugin) {
        this.playerData = plugin.getPlayerData();
        this.luckPerms = plugin.getLuckPerms();
    }

    @Override
    public Component getPlayerNickname(Player player) {
        return playerData.getPlayer(player.getUniqueId()).nickname();
    }

    @Override
    public void setPlayerNickname(Player player, Component nickname) {
        playerData.getPlayer(player.getUniqueId()).nickname();
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
    public Timestamp getPlayerLastSeen(Player player) {
        return playerData.getPlayer(player.getUniqueId()).lastSeen();
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