package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvlib.commands.CommandGuard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LocationSaveCommand implements CommandExecutor {
    private final PVCore plugin;

    public LocationSaveCommand(PVCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandGuard.senderNotPlayer(sender, plugin.config().getMessage("player-only"))) return true;
        if (CommandGuard.argsSizeInvalid(1, args, sender, plugin.config().getMessage("location-save-usage"))) return true;

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        String locationLabel = args[0].toLowerCase();
        SavedLocation location = new SavedLocation(uuid, locationLabel, player.getLocation());
        location.playerSave(plugin, player.getLocation());
        return true;
    }


}
