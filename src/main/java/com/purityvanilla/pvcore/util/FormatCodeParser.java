package com.purityvanilla.pvcore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatCodeParser {
    private static final String COLOR_CHARS = "0123456789abcdef";
    private static final String FORMAT_CHARS = "lmnor";
    public static final String HEX_PERMISSION = "hex";

    private static final Pattern LEGACY_PATTERN = Pattern.compile(
            "&([0-9a-fl-or]|#[0-9A-Fa-f]{6}|x&[0-9A-Fa-f]&[0-9A-Fa-f]&[0-9A-Fa-f]&[0-9A-Fa-f]&[0-9A-Fa-f]&[0-9A-Fa-f])"
    );

    private static final String ROOT_PERMISSION_BASE = "pvcore.formatcodes.";

    public enum Context {
        ANVIL(ROOT_PERMISSION_BASE + "anvil"),
        CHAT(ROOT_PERMISSION_BASE + "chat"),
        NICKNAME(ROOT_PERMISSION_BASE + "nickname"),
        SIGN(ROOT_PERMISSION_BASE + "sign");

        private final String permissionNode;
        Context(String permissionNode) {
            this.permissionNode = permissionNode;
        }

        public String getPermissionNode() {
            return permissionNode;
        }

        public String getPermissionBase() {
            return permissionNode + ".";
        }
    }

    public static Component parseString(String rawString, Player player, Context context) {
        if (rawString == null || rawString.isEmpty()) {
            return Component.empty();
        }

        String filteredString = filterUnpermittedCodes(player, rawString, context);
        return LegacyComponentSerializer.legacyAmpersand().deserialize(filteredString);
    }

    public static boolean validCodeCharacter(char codeChar) {
        return COLOR_CHARS.indexOf(codeChar) > -1 || FORMAT_CHARS.indexOf(codeChar) > -1;
    }

    public static boolean hasCodePermission(Player player, String code, Context context) {
        if (code == null || code.isEmpty()) {
            return false;
        }

        // Handle global and context-specific wildcards
        if (player.hasPermission(ROOT_PERMISSION_BASE + "*") ||
                player.hasPermission(context.getPermissionBase() + "*")) {
            return true;
        }

        // Handle character codes
        if (code.length() == 1) {
            char codeChar = code.charAt(0);
            if (validCodeCharacter(codeChar)) {
                return player.hasPermission(context.getPermissionBase() + codeChar);
            }
        // Handle standard hex codes with fixed permission
        } else if (code.startsWith("#") || code.startsWith("x")) {
            return player.hasPermission(context.getPermissionBase() + HEX_PERMISSION);
        }

        return false;
    }

    private static String filterUnpermittedCodes(Player player, String rawString, Context context) {
        // Return original rawString if there are no format codes to process
        if (!rawString.contains("&")) {
            return rawString;
        }

        StringBuffer filteredString = new StringBuffer();
        Matcher matcher = LEGACY_PATTERN.matcher(rawString);

        while (matcher.find()) {
            // Extract single code character as first matcher group (matcher.group(0) returns full match with leading "&"
            String code = matcher.group(1);

            if (!hasCodePermission(player, code, context)) {
                matcher.appendReplacement(filteredString, "");
            }
        }

        matcher.appendTail(filteredString);
        return filteredString.toString();
    }

    // TODO use this method in dynamic /colours command to show what colour codes a player can use
    public static Set<String> getPermittedFormatCodes(Player player, Context context) {
        Set<String> permittedCodes = new HashSet<>();

        for (char codeChar : COLOR_CHARS.toCharArray()) {
            if (player.hasPermission(context.getPermissionBase() + codeChar)) {
               permittedCodes.add(String.valueOf(codeChar));
            }
        }

        for (char codeChar : FORMAT_CHARS.toCharArray()) {
            if (player.hasPermission(context.getPermissionBase() + codeChar)) {
                permittedCodes.add(String.valueOf(codeChar));
            }
        }

        if (player.hasPermission(context.getPermissionBase() + HEX_PERMISSION)) {
            permittedCodes.add(HEX_PERMISSION);
        }

        return permittedCodes;
    }
}
