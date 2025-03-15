package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.player.CachedPlayer;
import com.purityvanilla.pvcore.player.Nicknames;
import com.purityvanilla.pvcore.pvCore;
import com.purityvanilla.pvlib.commands.CommandGuard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RealnameCommand implements CommandExecutor {
    private final pvCore plugin;

    public RealnameCommand(pvCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandGuard.argsSizeInvalid(1, args, sender, plugin.config().getMessage("realname-usage"))) return true;

        // Get list of players whose nickname matches the search string
        String nickname = args[0];
        List<CachedPlayer> matchingPlayers = Nicknames.getPlayersFromNickname(nickname,
                plugin.getServer().getOnlinePlayers(), plugin.getPlayerData()
        );

        // Add any players whose username matches the search string
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.getName().toLowerCase().contains(nickname)) {
                matchingPlayers.add(plugin.getPlayerData().getPlayer(player.getUniqueId()));
            }
        }

        if (matchingPlayers.isEmpty()) {
            sender.sendMessage(plugin.config().getMessage("player-not-found"));
            return true;
        }

        // Build message with list of players and their nicknames
        Component realnameMessage = plugin.config().getMessage("realname-header",
                TagResolver.resolver(Placeholder.component("nickname", Component.text(nickname)))
        );

        for (CachedPlayer cPlayer : matchingPlayers) {
            Component username = Component.text(cPlayer.name());
            Component displayNick = (cPlayer.hasNick()) ? cPlayer.nickname() : username;

            Component entry = plugin.config().getMessage("realname-entry",
                    TagResolver.resolver(
                            Placeholder.component("nickname", displayNick),
                            Placeholder.component("username", username)
                    )
            );
            realnameMessage = realnameMessage.appendNewline().append(entry);
        }

        sender.sendMessage(realnameMessage);
        return true;
    }
}
