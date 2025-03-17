package com.purityvanilla.pvcore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class CustomTagResolvers {

    public static TagResolver playerResolver(String playerName) {
        return TagResolver.resolver(Placeholder.component("player", Component.text(playerName)));
    }

    public static TagResolver locationLabelResolver(String locationLabel) {
        return TagResolver.resolver(Placeholder.component("label", Component.text(locationLabel)));
    }

    public static TagResolver playerDateResolver(String playerName, String dateString) {
        return TagResolver.resolver(Placeholder.component("player", Component.text(playerName)),
                Placeholder.component("date", Component.text(dateString)));
    }
}