package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.database.LocationDataService;
import com.purityvanilla.pvcore.player.SavedLocation;
import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvlib.commands.CommandGuard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LocationRenameCommand implements CommandExecutor {
    private final PVCore plugin;

    public LocationRenameCommand(PVCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandGuard.senderNotPlayer(sender, plugin.config().getMessage("player-only"))) return true;
        if (CommandGuard.argsSizeInvalid(2, args, sender, plugin.config().getMessage("location-rename-usage"))) return true;

        Player player = (Player) sender;
        LocationDataService locationData = plugin.getLocationData();
        UUID playerID = player.getUniqueId();
        String oldLabel = args[0].toLowerCase();
        String locationLabel = args[1].toLowerCase();

        if (!locationData.locationExists(playerID, oldLabel)) {
            sender.sendMessage(plugin.config().getMessage("location-not-found"));
            return true;
        }
        
        SavedLocation location = locationData.getLocation(playerID, oldLabel);
        if (locationData.locationExists(playerID, locationLabel)) {
            location.playerSave(plugin, locationData.getLocation(playerID, oldLabel).getLocation(plugin));
            return true;
        }

        locationData.removeLocation(playerID, location);
        location.setLabel(locationLabel);
        locationData.addLocation(playerID, location);

        TagResolver resolver = TagResolver.resolver(Placeholder.component("oldlabel", Component.text(oldLabel)),
                Placeholder.component("label", Component.text(locationLabel)));
        player.sendMessage(plugin.config().getMessage("location-renamed", resolver));
        return true;
    }
}
