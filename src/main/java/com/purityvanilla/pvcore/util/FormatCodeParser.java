package com.purityvanilla.pvcore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatCodeParser {
    private static final String COLOR_CHARS = "0123456789abcdef";
    private static final String FORMAT_CHARS = "klmnor";
    public static final String HEX_PERMISSION = "hex";

    private static final Pattern LEGACY_PATTERN = Pattern.compile(
            "&([0-9a-fk-or]|#[0-9A-Fa-f]{6}|x&[0-9A-Fa-f]&[0-9A-Fa-f]&[0-9A-Fa-f]&[0-9A-Fa-f]&[0-9A-Fa-f]&[0-9A-Fa-f])"
    );

    private static final String CORE_PERMISSION_BASE = "pvcore.formatcodes.";
    private static final String CHAT_PERMISSION_BASE = "pvchat.formatcodes.";
    private static final String PERKS_PERMISSION_BASE = "pvperks.formatcodes.";

    public enum Context {
        ANVIL(PERKS_PERMISSION_BASE + "anvil"),
        CHAT(CHAT_PERMISSION_BASE + "chat"),
        NICKNAME(CORE_PERMISSION_BASE + "nickname"),
        SIGN(PERKS_PERMISSION_BASE + "sign");

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

    public static Component parseComponent(Component rawComponent, Player player, Context context) {
        return parseString(PlainTextComponentSerializer.plainText().serialize(rawComponent), player, context);
    }

    public static Component deserialiseFormatString(String rawString) {
        // Deserialise with MiniMessage if codes are found otherwise fallback to legacy ampersand
        if (rawString.contains("<") && rawString.contains(">")) {
            return MiniMessage.miniMessage().deserialize(rawString);
        } else {
            return LegacyComponentSerializer.legacyAmpersand().deserialize(rawString);
        }
    }

    public static boolean validCodeCharacter(char codeChar) {
        return COLOR_CHARS.indexOf(codeChar) > -1 || FORMAT_CHARS.indexOf(codeChar) > -1;
    }

    public static boolean hasCodePermission(Player player, String code, Context context) {
        if (code == null || code.isEmpty()) {
            return false;
        }

        // Handle magic (obfuscated) format code as special case not covered by catch-all ("*" will cover magic)
        if (code.equals("k")) {
            return player.hasPermission(CORE_PERMISSION_BASE + "magic") ||
                    player.hasPermission(context.getPermissionBase() + "magic");
        }

        // Handle global and context-specific catch-all perms
        if (player.hasPermission(CORE_PERMISSION_BASE + "all") ||
                player.hasPermission(context.getPermissionBase() + "all")) {
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
