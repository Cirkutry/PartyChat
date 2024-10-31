package partyChat.object;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Represents a player inviting another player to their party.
 */
public record Invite(Party party, Player inviteSender, UUID invitedUUID) {

    public UUID inviteSenderID() {
        return inviteSender.getUniqueId();
    }

    public String invitedUsername() {
        return Bukkit.getOfflinePlayer(invitedUUID).getName();
    }

    public Party getParty() {
        return party;
    }

}
