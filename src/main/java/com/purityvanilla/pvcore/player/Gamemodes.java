package com.purityvanilla.pvcore.player;

import com.purityvanilla.pvcore.PVCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Gamemodes {
    private static HashMap<GameMode, String> permissionMap = new HashMap<>();

    static {
        permissionMap.put(GameMode.ADVENTURE, "pvcore.gamemode.adventure");
        permissionMap.put(GameMode.CREATIVE, "pvcore.gamemode.creative");
        permissionMap.put(GameMode.SURVIVAL, "pvcore.gamemode.survival");
        permissionMap.put(GameMode.SPECTATOR, "pvcore.gamemode.spectator");
    }

    public static String getGamemodePermission(GameMode gameMode) {
        return permissionMap.get(gameMode);
    }

    public static void updateGamemode(PVCore plugin, Player player, GameMode gamemode) {
        player.setGameMode(gamemode);
        TagResolver resolver = TagResolver.resolver(Placeholder.component("gamemode", Component.text(gamemode.name().toLowerCase())));
        player.sendMessage(plugin.config().getMessage("gamemode-updated", resolver));
    }

    public static void updateOthersGamemode(PVCore plugin, Player player, Player target, GameMode gamemode) {
        updateGamemode(plugin, target, gamemode);
        TagResolver resolver = TagResolver.resolver(
                Placeholder.component("player", Component.text(target.getName())),
                Placeholder.component("gamemode", Component.text(gamemode.name().toLowerCase()))
        );
        player.sendMessage(plugin.config().getMessage("gamemode-updated-other", resolver));
    }
}
