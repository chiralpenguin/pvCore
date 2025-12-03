package com.purityvanilla.pvcore.api.impl;

import com.purityvanilla.pvcore.Config;
import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.api.PVCoreAPI;
import com.purityvanilla.pvcore.api.player.PlayerAPI;
import com.purityvanilla.pvcore.api.player.impl.PlayerAPIProvider;
import com.purityvanilla.pvlib.database.DatabaseConnector;
import com.purityvanilla.pvlib.util.FormatCodeParser;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

public class PVCoreAPIProvider implements PVCoreAPI {
    private final Supplier<Config> configSupplier;
    private final PlayerAPI playerAPI;
    private final DatabaseConnector database;
    private final LuckPerms luckPerms;

    public PVCoreAPIProvider(PVCore plugin, DatabaseConnector database) {
        this.configSupplier = plugin::config;
        this.playerAPI = new PlayerAPIProvider(plugin);
        this.database = database;
        this.luckPerms = plugin.getLuckPerms();
    }

    @Override
    public PlayerAPI getPlayerAPI() {
        return playerAPI;
    }

    @Override
    public DatabaseConnector getDatabase() {
        return database;
    }

    @Override
    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    @Override
    public Component parsePlayerFormatString(String rawString, Player player, FormatCodeParser.Context context) {
        return FormatCodeParser.parseString(rawString, player, context);
    }

    @Override
    public Component parsePlayerComponent(Component rawComponent, Player player, FormatCodeParser.Context context) {
        return FormatCodeParser.parseComponent(rawComponent, player, context);
    }
}
