package com.purityvanilla.pvcore.api.impl;

import com.purityvanilla.pvcore.Config;
import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.api.PVCoreAPI;
import com.purityvanilla.pvcore.api.player.PlayerAPI;
import com.purityvanilla.pvcore.api.player.impl.PlayerAPIProvider;
import com.purityvanilla.pvlib.util.FormatCodeParser;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

public class PVCoreAPIProvider implements PVCoreAPI {
    private final Supplier<Config> configSupplier;
    private final PlayerAPI playerAPI;

    public PVCoreAPIProvider(PVCore plugin) {
        this.configSupplier = plugin::config;
        this.playerAPI = new PlayerAPIProvider(plugin);
    }

    @Override
    public PlayerAPI getPlayerAPI() {
        return playerAPI;
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
