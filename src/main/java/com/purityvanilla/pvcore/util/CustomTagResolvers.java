package com.purityvanilla.pvcore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class CustomTagResolvers {

    public static TagResolver playerResolver(String playerName) {
        return TagResolver.resolver(Placeholder.component("player", Component.text(playerName)));
    }

    public static TagResolver labelResolver(String locationLabel) {
        return TagResolver.resolver(Placeholder.component("label", Component.text(locationLabel)));
    }

    public static TagResolver seenResolver(String playerName, String dateString) {
        return TagResolver.resolver(Placeholder.component("player", Component.text(playerName)),
                Placeholder.component("date", Component.text(dateString)));
    }

    public static TagResolver databaseMigrationResolver(int currentVersion, int oldVersion) {
        return TagResolver.resolver(Placeholder.component("currentversion", Component.text(currentVersion)),
                Placeholder.component("dbversion", Component.text(oldVersion)));
    }
}
