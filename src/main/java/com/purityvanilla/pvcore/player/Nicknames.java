package com.purityvanilla.pvcore.player;

import com.purityvanilla.pvcore.database.PlayerDataService;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Nicknames {
    public static void setPlayerNickname(Player player, PlayerDataService playerData, Component nickname) {
        playerData.getPlayer(player.getUniqueId()).setNickname(nickname);
        player.displayName(nickname);
    }

    public static void removePlayerNickname(Player player, PlayerDataService playerData) {
        playerData.getPlayer(player.getUniqueId()).removeNickname();
        player.displayName(Component.text(player.getName()));
    }
}
