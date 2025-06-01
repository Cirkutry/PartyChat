package partyChat.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public enum Lang {

    // Constants for customizable messages
    // Prefixes and Titles
    PARTY_TAG("party.prefix", "&7[&9PartyChat&7]"),
    HELP_TITLE("help-menu.title", "&fParty Commands:"),

    // Help Menu Commands
    HELP_CREATE("help-menu.commands.create", "&f/party create <name> - &7Create a party for your friends."),
    HELP_LEAVE("help-menu.commands.leave", "&f/party leave - &7Leave your current party."),
    HELP_INVITE("help-menu.commands.invite", "&f/party invite <player> - &7Invite a player to join your party."),
    HELP_KICK("help-menu.commands.kick", "&f/party kick <player> - &7Remove a player from your party."),
    HELP_DISBAND("help-menu.commands.disband", "&f/party disband - &7Disband your current party."),
    HELP_PROMOTE("help-menu.commands.promote", "&f/party promote <player> - &7Promote a member to captain."),
    HELP_TOGGLE("help-menu.commands.toggle", "&f/pc - &7Toggle default chat between party and public."),
    HELP_MESSAGE("help-menu.commands.message", "&f/p <message> - &7Send a message to your party."),
    HELP_RELOAD("help-menu.commands.reload", "&f/partyreload - &7Reload language file."),

    // Roles and Attributes
    LEADER("leader", "&2Leader"),
    TRUSTED_RANK("trusted-rank", "&2Captain"),
    CREATED_ON("created-on", "&2Created on"),
    MEMBERS("members", "&2Members"),
    PLAYER_NAME_LABEL("name", "&2Name"),

    // Usage
    USAGE_PROMOTE("usage.promote", "&2Usage: /party promote <player>"),
    USAGE_KICK_MEMBER("usage.kick-member", "&2Usage: /party kick <player>"),
    USAGE_INVITE_MEMBER("usage.invite-member", "&2Usage: /party invite <player>"),
    USAGE_INFO_COMMAND("commands.info.usage", "&2Usage: /party info <party>"),

    // Errors
    ERROR_NOT_IN_PARTY("errors.not-in-party", "&cYou are not a member of any party."),
    ERROR_NOT_LEADER("errors.not-leader", "&cOnly the party leader can do that."),
    ERROR_PLAYER_NEVER_JOINED("errors.player.never-joined", "&cThat player has never joined the server before."),
    ERROR_NOT_PARTY_MEMBER("errors.not-party-member", "&c%s is not a member of your party."),
    ERROR_CANNOT_PROMOTE_FURTHER("errors.cannot-promote-further", "&cYou cannot promote that party member any further."),
    ERROR_PLAYER_ONLY("errors.player-only", "&cSorry, that's a player-only command."),
    ERROR_NO_MESSAGE("errors.no-message", "&cYou need to provide a message to send!"),
    ERROR_LEADER_CANNOT_LEAVE("errors.leader-cannot-leave", "&cYou are the party leader. To disband your party, type /party disband."),
    ERROR_LEADER_CANNOT_KICK("errors.leader-cannot-kick", "&cOnly the party leader can do that."),
    ERROR_NOT_VALID_MEMBER("errors.not-valid-member", "&cThat's not a valid party member."),
    ERROR_NOT_A_MEMBER("errors.not-a-member", "&cThat player isn't a member of your party."),
    ERROR_CANNOT_KICK_SELF("errors.cannot-kick-self", "&cYou cannot kick yourself!"),
    ERROR_ONLY_LEADER_OR_STAFF_CAN_INVITE("errors.only-leader-or-staff-can-invite", "&cOnly a leader or party %trusted_rank% can invite players."),
    ERROR_PLAYER_NOT_FOUND("errors.player-not-found", "&cCouldn't find that player. Are they online?"),
    ERROR_CANNOT_INVITE_SELF("errors.cannot-invite-self", "&cYou're already a member of your party."),
    ERROR_ALREADY_IN_PARTY("errors.already-in-party", "&cThat player is already a member of a party."),
    ERROR_EXISTING_INVITE("errors.existing-invite", "&cThat player already has an existing invite."),
    ERROR_NO_PENDING_INVITE("errors.no-pending-invite", "&cYou do not have a pending invite."),
    ERROR_PARTY_NOT_FOUND("errors.party-not-found", "Couldn't find a party by that name."),
    ERROR_NO_PERMISSIONS("errors.no-permissions", "&cYou don't have permission to do that!"),
    ERROR_PARTY_ALREADY_EXISTS("errors.party.already-exists", "&cThe party \"%s\" already exists."),

    // Messages
    CHAT_MODE_PUBLIC("chat.mode.public", "&2Default chat has been set to Public Chat."),
    CHAT_MODE_PARTY("chat.mode.party", "&2Default chat has been set to Party Chat."),
    PROMOTED_MESSAGE("messages.promoted", "&2%s has been promoted to party %s by %s."),
    PLAYER_LEFT_PARTY("messages.player-left-party", "&2%s has left the party."),
    MEMBER_KICKED("messages.member-kicked", "&2You have been kicked from the party."),
    PLAYER_KICKED("messages.player-kicked", "&2%s has been kicked from the party."),
    INVITE_SENT("messages.invite-sent", "&2You have sent %s an invite to join %s."),
    INVITE_RECEIVED("messages.invite-received", "&2You have been invited by %s to join %s!"),
    INVITE_RESPOND("messages.invite-respond", "&2Respond with /party accept or /party decline."),
    INVITE_DECLINED("messages.invite-declined", "&2You declined the invite to join %s."),
    PLAYER_DECLINED_INVITE("messages.player-declined-invite", "&2%s declined the invite to join %s."),
    JOINED_PARTY("messages.joined-party", "&2You have joined the party %s!"),
    PARTY_CHAT_MESSAGE("messages.party-chat", "&f%s: &7%s"),
    PARTY_MESSAGE("party.message", "&f%s"),
    PLAYER_JOINED_PARTY("messages.player-joined-party", "&2%s has joined the party!"),
    PARTY_CHAT_HINT("messages.party-chat-hint", "&2Hint: Type /party <message> to talk within your party."),
    WAIT_MESSAGE("messages.wait-message", "&2Please wait..."),
    DESCRIPTION_PROMOTE("description.promote", "&2Give a member invite and kick permissions."),

    // Warnings
    WARNING_DISBAND_PARTY_START("warnings.disband-party.start", "&cYou are about to disband %s forever."),
    WARNING_DISBAND_PARTY_CONFIRM("warnings.disband-party.confirm", "&cAre you sure? Type /party disband <partyName> confirm to continue."),
    WARNING_CREATE_PARTY_START("warnings.create-party.start", "&2You are about to create the party \"%s\"."),
    WARNING_CREATE_PARTY_CONFIRM("warnings.create-party.confirm", "&2Type /party create <name> confirm to confirm."),

    // Info and Confirmation
    INFO_PARTY_DISBANDED("info.party.disbanded", "&cYour party has been disbanded."),
    INFO_PARTY_CREATED("info.party.created", "&2You have created the party %s! Type /party help for a list of commands."),
    ERROR("error", "&cError: %s"),
    CONFIRM_LEAVE_PARTY("confirm.leave-party", "&2Are you sure you want to leave %s? Type /party leave confirm to continue."),

    // Reload Messages
    RELOAD_SUCCESS("reload.success", "&2Language file reloaded successfully!"),
    RELOAD_FAILURE("reload.failure", "&cFailed to reload the language file."),
    RELOAD_NO_PERMISSION("reload.no-permission", "&cYou don't have permission to use this command.");

    private final String path;
    private final String defaultMessage;
    private static YamlConfiguration LANG;

    // Enum constructor
    Lang(String path, String defaultMessage) {
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    // Method to set the configuration
    public static void setConfig(YamlConfiguration config) {
        LANG = config;
    }

    // Check if LANG has been initialized
    private void checkInitialized() {
        if (LANG == null) {
            throw new IllegalStateException("Language configuration is not initialized. Make sure to call Lang.setConfig() before using Lang values.");
        }
    }

    // toString() method to return the message or the default value
    @Override
    public String toString() {
        checkInitialized();
        String message = LANG.getString(this.path, this.defaultMessage);
        return formatColors(message);
    }

    // Overloaded toString method to support formatted strings
    public String toString(Object... args) {
        checkInitialized();
        String message = LANG.getString(this.path, this.defaultMessage);
        try {
            return formatColors(String.format(message, args));
        } catch (IllegalFormatException e) {
            return message + " (Error formatting with arguments: " + Arrays.toString(args) + ")";
        }
    }

    // Method to replace both &-based and hex color codes
    public static String formatColors(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        return translateHexColorCodes(message);
    }

    private static String translateHexColorCodes(String message) {
        // Pattern to match hex color codes in the format &#rrggbb
        Pattern hexPattern = Pattern.compile("(?i)&#([a-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder();

        // Loop through all matches and replace with Minecraft's color format
        while (matcher.find()) {
            String hexCode = matcher.group(1);
            StringBuilder replacement = new StringBuilder("§x");
            for (char ch : hexCode.toCharArray()) {
                replacement.append("§").append(ch);
            }
            matcher.appendReplacement(buffer, replacement.toString());
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

}
