package com.purityvanilla.pvcore.tabcompleters;

import com.purityvanilla.pvcore.player.Gamemodes;
import com.purityvanilla.pvcore.pvCore;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GamemodeCompleter implements TabCompleter {
    private pvCore plugin;

    public GamemodeCompleter(pvCore plugin) {
        this.plugin = plugin;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<String>();
        if (args.length == 1) {
            if (sender.hasPermission(Gamemodes.getGamemodePermission(GameMode.ADVENTURE))) {
                suggestions.add("adventure");
            }
            if (sender.hasPermission(Gamemodes.getGamemodePermission(GameMode.CREATIVE))) {
                suggestions.add("creative");
            }
            if (sender.hasPermission(Gamemodes.getGamemodePermission(GameMode.SURVIVAL))) {
                suggestions.add("survival");
            }
            if (sender.hasPermission(Gamemodes.getGamemodePermission(GameMode.SPECTATOR))) {
                suggestions.add("spectator");
            }
        }

        if (args.length == 2) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                suggestions.add(player.getName());
            }
        }

        return suggestions;
    }
}
