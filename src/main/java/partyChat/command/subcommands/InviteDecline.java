package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import partyChat.object.Invite;
import partyChat.command.PartySubCommand;
import org.bukkit.entity.Player;
import partyChat.config.Lang;
import partyChat.util.Chat;

public class InviteDecline extends PartySubCommand {
    public InviteDecline(PartyChat plugin) {
        super(plugin, "decline");
    }

    @Override
    public void execute(Player player, String[] args) {
        Invite theInvite = plugin.inviteHandler().getInvite(player.getUniqueId());

        if (theInvite == null) {
            utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NO_PENDING_INVITE.toString()));
            return;
        }

        Party party = theInvite.getParty();
        utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), String.format(Lang.INVITE_DECLINED.toString(), party.name())));
        party.broadcast(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), String.format(Lang.PLAYER_DECLINED_INVITE.toString(), player.getName(), party.name())));

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

