package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import partyChat.object.Invite;
import partyChat.command.PartySubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import partyChat.config.Lang;
import partyChat.util.Chat;

public class InviteMember extends PartySubCommand {
	public InviteMember(PartyChat plugin) {
		super(plugin, "invite");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (args.length < 2) {
			utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.USAGE_INVITE_MEMBER.toString()));
			return;
		}

		Party party = utils.getPlayerParty(player.getUniqueId());

		if (party == null) {
			utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NOT_IN_PARTY.toString()));
			return;
		}

		if (!party.isPartyStaff(player.getUniqueId())) {
			String message = Lang.ERROR_ONLY_LEADER_OR_STAFF_CAN_INVITE.toString()
					.replace("%trusted_rank%", Lang.TRUSTED_RANK.toString());
			utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), message));
			return;
		}

		Player invited = Bukkit.getPlayer(args[1]);

		if (invited == null) {
			utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_PLAYER_NOT_FOUND.toString()));
			return;
		}

		if (invited.equals(player)) {
			utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_CANNOT_INVITE_SELF.toString()));
			return;
		}

		if (utils.getPlayerParty(invited.getUniqueId()) != null) {
			utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_ALREADY_IN_PARTY.toString()));
			return;
		}

		if (plugin.inviteHandler().getInvite(invited.getUniqueId()) != null) {
			utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_EXISTING_INVITE.toString()));
			return;
		}

		plugin.inviteHandler().addInviteToMap(new Invite(party, player, invited.getUniqueId()));

		utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), String.format(Lang.INVITE_SENT.toString(), invited.getName(), party.name())));
		invited.sendMessage("");
		invited.sendMessage(Lang.PARTY_TAG.toString(), String.format(Lang.INVITE_RECEIVED.toString(), player.getName(), party.name()));
		invited.sendMessage(Lang.PARTY_TAG.toString(), Lang.INVITE_RESPOND.toString());
		invited.sendMessage("");
	}

	@Override
	public String description() {
		return "Invite a player to your party.";
	}

	@Override
	public String usage() {
		return "/party invite <player>";
	}
}

