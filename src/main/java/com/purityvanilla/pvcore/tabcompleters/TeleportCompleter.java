package com.purityvanilla.pvcore.tabcompleters;

import com.purityvanilla.pvcore.PVCore;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeleportCompleter implements TabCompleter {
    private PVCore plugin;

    public TeleportCompleter(PVCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();
        switch (args.length) {
            case 1:
                String partialInput = args[args.length - 1].toLowerCase();

                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    String playerName = player.getName();
                    if (playerName.startsWith(partialInput)) {
                        suggestions.add(playerName);
                    }
                }
                break;
            case 4:
                for (World world : plugin.getServer().getWorlds()) {
                    suggestions.add(world.getName());
                }
                break;
        }
        return suggestions;
    }
}
