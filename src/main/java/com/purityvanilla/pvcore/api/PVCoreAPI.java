package com.purityvanilla.pvcore.api;

import com.purityvanilla.pvcore.api.player.PlayerAPI;
import com.purityvanilla.pvlib.database.DatabaseConnector;
import com.purityvanilla.pvlib.util.FormatCodeParser;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import org.bukkit.entity.Player;

public interface PVCoreAPI {

    PlayerAPI getPlayerAPI();

    DatabaseConnector getDatabase();

    LuckPerms getLuckPerms();

    /**
     * Parses a player-submitted string for legacy ('&') format codes and returns a permission-based formatted Component
     * @param rawString The unparsed string containing format codes
     * @param player The player whose permissions will be checked for formatting
     * @param context The context of the message
     * @return Component containing the stripped and formatted messages
     */
    Component parsePlayerFormatString(String rawString, Player player, FormatCodeParser.Context context);

    Component parsePlayerComponent(Component rawComponent, Player player, FormatCodeParser.Context context);
}
