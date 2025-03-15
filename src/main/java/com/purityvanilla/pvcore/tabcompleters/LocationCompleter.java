package com.purityvanilla.pvcore.tabcompleters;

import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvcore.PVCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LocationCompleter implements TabCompleter {
    PVCore plugin;

    public LocationCompleter(PVCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (!(sender instanceof Player player)) {
            return suggestions;
        }

        if (args.length == 1) {
            String partialInput = args[args.length - 1].toLowerCase();

            List<SavedLocation> locations = plugin.getLocationData().getPlayerLocations(player.getUniqueId());
            for (SavedLocation location : locations) {
                if (location.getLabel().startsWith(partialInput)) {
                    suggestions.add(location.getLabel());
                }
            }
        }
        return suggestions;
    }
}
