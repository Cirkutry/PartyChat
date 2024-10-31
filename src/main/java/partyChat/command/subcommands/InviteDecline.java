package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import partyChat.object.Invite;
import partyChat.command.PartySubCommand;
import org.bukkit.entity.Player;

public class InviteDecline extends PartySubCommand {
    public InviteDecline(PartyChat plugin) {
        super(plugin, "decline");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (plugin.inviteHandler().getInvite(player.getUniqueId()) == null) {
            utils.message(player, "You do not have a pending invite.");
            return;
        }

		Invite theInvite = plugin.inviteHandler().getInvite(player.getUniqueId());
		Party party = theInvite.getParty();

        utils.message(player, "You declined the invite to join " + party.name() + ".");
        party.broadcast(player.getName() + " declined the invite to join " + party.name() + ".");

        plugin.inviteHandler().removeInviteFromMap(theInvite);
    }

    @Override
    public String description() {
        return "Decline an invite.";
    }

    @Override
    public String usage() {
        return "/party decline";
    }
}
