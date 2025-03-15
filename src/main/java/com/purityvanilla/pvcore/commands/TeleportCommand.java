package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvlib.commands.CommandGuard;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.purityvanilla.pvcore.player.Teleports.TeleportPlayerToLocation;
import static com.purityvanilla.pvcore.player.Teleports.TeleportPlayerToPlayer;

public class TeleportCommand implements CommandExecutor {
    private final PVCore plugin;

    public TeleportCommand(PVCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandGuard.senderNotPlayer(sender, plugin.config().getMessage("player-only"))) return true;
        if (CommandGuard.argsSizeInvalid(1, args, sender, plugin.config().getMessage("teleport-usage"))) return true;

        Player player = (Player) sender;
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target != null) {
            TeleportPlayerToPlayer(plugin, player, target);
            return true;
        }

        if (args.length == 1) {
            sender.sendMessage(plugin.config().getMessage("player-not-found"));
            return true;
        }
        // Following logic assumes position teleport
        try {
            World world;
            int xpos = Integer.parseInt(args[0]);
            int ypos = Integer.parseInt(args[1]);
            int zpos = Integer.parseInt(args[2]);

            if (args.length > 3) {
                world = plugin.getServer().getWorld(args[3]);
            } else {
                world = player.getWorld();
            }

            Location loc = new Location(world, xpos, ypos, zpos);
            TeleportPlayerToLocation(plugin, player, loc);
            return true;

        } catch (NumberFormatException | NullPointerException e) {
            sender.sendMessage(plugin.config().getMessage("teleport-position-usage"));
            return true;
        }
    }
}
