package com.purityvanilla.pvcore.player;

import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class Vanish {
    public static boolean isVanished(Player player) {
        for (MetadataValue value : player.getMetadata("vanished")) {
            if (value.asBoolean()) {
                return true;
            }
        }

        return false;
    }

}
