package com.purityvanilla.pvcore.api.player.impl;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvcore.api.player.PlayerAPI;
import com.purityvanilla.pvcore.database.PlayerDataService;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class PlayerAPIProvider implements PlayerAPI {
    private PlayerDataService playerData;

    public PlayerAPIProvider(PVCore plugin) {
        this.playerData = plugin.getPlayerData();
    }

    @Override
    public Component getPlayerNickname(Player player) {
        return playerData.getPlayer(player.getUniqueId()).nickname();
    }

    @Override
    public void setPlayerNickname(Player player, Component nickname) {
        playerData.getPlayer(player.getUniqueId()).nickname();
    }
}
