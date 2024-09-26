package com.purityvanilla.pvcore.tabcompleters;

import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvcore.pvCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LocationTabCompleter implements TabCompleter {
    pvCore plugin;

    public LocationTabCompleter(pvCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (!(sender instanceof Player player)) {
            return suggestions;
        }

        List<SavedLocation> locations = plugin.getLocationData().getPlayerLocations(player.getUniqueId());
        for (SavedLocation location : locations) {
            suggestions.add(location.getLabel());
        }
        return suggestions;
    }
}
