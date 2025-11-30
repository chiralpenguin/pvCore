package com.purityvanilla.pvcore.commands;

import com.purityvanilla.pvcore.player.Nicknames;
import com.purityvanilla.pvcore.PVCore;
import com.purityvanilla.pvlib.commands.CommandGuard;
import com.purityvanilla.pvlib.util.FormatCodeParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NicknameCommand implements CommandExecutor {
    private final PVCore plugin;

    public NicknameCommand(PVCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandGuard.senderNotPlayer(sender, plugin.config().getMessage("player-only"))) return true;
        if (CommandGuard.argsSizeInvalid(1, args, sender, plugin.config().getMessage("nick-usage"))) return true;

        String rawNickString = args[0];
        // Could replace with regex matching if performance becomes a concern - but this is more robust
        Component unparsedNick = LegacyComponentSerializer.legacyAmpersand().deserialize(rawNickString);
        String plaintextNick = PlainTextComponentSerializer.plainText().serialize(unparsedNick);

        // Handle nickname length limit
        int maxNickLength = plugin.config().getMaxNicknameLength();
        if (plaintextNick.length() > maxNickLength) {
            TagResolver resolver = TagResolver.resolver(Placeholder.component("limit", Component.text(maxNickLength)));
            sender.sendMessage(plugin.config().getMessage("nick-exceeds-limit", resolver));
            return true;
        }

        // Handle nickname character restriction
        String allowedCharsPattern = "[A-Za-z0-9_\\-]+";
        if (!plaintextNick.matches(allowedCharsPattern)) {
            sender.sendMessage(plugin.config().getMessage("nick-invalid-chars"));
            return true;
        }

        Player player = (Player) sender;
        Component nickname = FormatCodeParser.parseString(rawNickString, player, FormatCodeParser.Context.NICKNAME);
        Nicknames.setPlayerNickname(player, plugin.getPlayerData(), nickname);

        TagResolver resolver = TagResolver.resolver(Placeholder.component("nickname", nickname));
        sender.sendMessage(plugin.config().getMessage("nick-updated", resolver));

        return true;
    }
}
