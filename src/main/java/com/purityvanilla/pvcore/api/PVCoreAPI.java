package com.purityvanilla.pvcore.api;

import com.purityvanilla.pvcore.api.player.PlayerAPI;
import com.purityvanilla.pvcore.util.FormatCodeParser;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface PVCoreAPI {

    PlayerAPI getPlayerAPI();

    Component parsePlayerFormatString(String rawString, Player player, FormatCodeParser.Context context);
}
