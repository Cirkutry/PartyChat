package partyChat.util;

import partyChat.object.Party;
import partyChat.PartyChat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import partyChat.config.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartyChatUtils {
    private final PartyChat plugin;

    public PartyChatUtils(PartyChat plugin) {
        this.plugin = plugin;
    }

    /**
     * Sends an error message to the player using a standardized format.
     * @param player The player to send the message to.
     * @param reason The reason for failure.
     */
    public void fail(Player player, String reason) {
        String message = Lang.ERROR.toString(reason);
        player.sendMessage(message);
    }

    /**
     * Sends a regular message to the player with the PartyChat prefix.
     * @param player The player to send the message to.
     * @param msg The message to send.
     */
    public void message(Player player, String msg) {
        String message = Lang.PARTY_MESSAGE.toString(msg);
        player.sendMessage(message);
    }

    /**
     * Checks if a name is valid based on length and disallowed characters.
     * @param theName The name to validate.
     * @return True if valid, false otherwise.
     */
    public boolean isValidName(String theName) {
        if (theName.length() < 4 || theName.length() > 16) return false;
        return !Utils.chatInArray(theName, plugin.config().disallowedInName);
    }

    /**
     * Retrieves a list of party names.
     * @return A list of party names.
     */
    public List<String> getPartyNames() {
        List<String> results = new ArrayList<>();
        plugin.getParties().forEach(party -> results.add(party.name()));
        return results;
    }

    /**
     * Retrieves a party by its name.
     * @param name The name of the party.
     * @return The party object or null if not found.
     */
    public Party getPartyByName(String name) {
        for (Party party : plugin.getParties()) {
            if (party.name().equalsIgnoreCase(name)) {
                return party;
            }
        }
        return null;
    }

    /**
     * Retrieves the party a player is in based on their UUID.
     * @param uuid The UUID of the player.
     * @return The party object or null if the player is not in a party.
     */
    public Party getPlayerParty(UUID uuid) {
        for (Party party : plugin.getParties()) {
            if (party.getMembers().contains(uuid)) {
                return party;
            }
        }
        return null;
    }

    /**
     * Checks if a player is in a party.
     * @param uuid The UUID of the player.
     * @return True if the player is in a party, false otherwise.
     */
    public boolean playerInParty(UUID uuid) {
        return getPlayerParty(uuid) != null;
    }

    /**
     * Checks if a player has party chat enabled.
     * @param p The player.
     * @return True if party chat is enabled, false otherwise.
     */
    public boolean partyChatEnabled(Player p) {
        return plugin.getConfig().getBoolean("partyChat." + p.getUniqueId());
    }

    /**
     * Displays the party command menu to a CommandSender.
     * This method should be refactored to use a configurable message file in the future.
     * @param sender The CommandSender to send the menu to.
     */
    public void displayMenu(CommandSender sender) {
        sender.sendMessage(Chat.formatWithPrefix(String.valueOf(Lang.PARTY_TAG), Lang.HELP_TITLE.toString()));
        sender.sendMessage(Lang.HELP_CREATE.toString());
        sender.sendMessage(Lang.HELP_LEAVE.toString());
        sender.sendMessage(Lang.HELP_INVITE.toString());
        sender.sendMessage(Lang.HELP_KICK.toString());
        sender.sendMessage(Lang.HELP_DISBAND.toString());
        sender.sendMessage(Lang.HELP_PROMOTE.toString());
        sender.sendMessage(Lang.HELP_TOGGLE.toString());
        sender.sendMessage(Lang.HELP_MESSAGE.toString());
        sender.sendMessage(Lang.HELP_RELOAD.toString());
    }
}