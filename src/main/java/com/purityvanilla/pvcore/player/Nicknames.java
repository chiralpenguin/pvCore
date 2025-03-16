package com.purityvanilla.pvcore.player;

import com.purityvanilla.pvcore.database.PlayerDataService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;

import java.util.*;

public class Nicknames {
    public static void setPlayerNickname(Player player, PlayerDataService playerData, Component nickname) {
        playerData.getPlayer(player.getUniqueId()).setNickname(nickname);
        player.displayName(nickname);
    }

    public static void removePlayerNickname(Player player, PlayerDataService playerData) {
        playerData.getPlayer(player.getUniqueId()).removeNickname();
        player.displayName(Component.text(player.getName()));
    }

    public static Set<CachedPlayer> getPlayersFromNickname(String nickname, Collection<? extends Player> players, PlayerDataService playerData) {
        Set<CachedPlayer> matchingPlayers = new HashSet<>();

        for (Player player : players) {
           CachedPlayer cPlayer = playerData.getPlayer(player.getUniqueId());
           if (!cPlayer.hasNick()) {
                continue;
           }

           String plainTextNick = PlainTextComponentSerializer.plainText().serialize(cPlayer.nickname());
           nickname = nickname.toLowerCase();
           if (plainTextNick.toLowerCase().contains(nickname)) {
                matchingPlayers.add(cPlayer);
           }
        }

        return matchingPlayers;
    }
}
