package com.purityvanilla.pvcore.api.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface PlayerAPI {

    Component getPlayerNickname(Player player);

    void setPlayerNickname(Player player, Component nickname);
}
