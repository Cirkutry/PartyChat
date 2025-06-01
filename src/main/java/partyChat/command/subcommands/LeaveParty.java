package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import partyChat.command.PartySubCommand;
import org.bukkit.entity.Player;
import partyChat.config.Lang;
import partyChat.util.Chat;

import java.util.List;

public class LeaveParty extends PartySubCommand {
	public LeaveParty(PartyChat plugin) {
		super(plugin, "leave");
	}

	@Override
	public void execute(Player player, String[] args) {
		Party party = utils.getPlayerParty(player.getUniqueId());

		if (party == null) {
			utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NOT_IN_PARTY.toString()));
			return;
		}

		if (party.isLeader(player.getUniqueId())) {
			utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_LEADER_CANNOT_LEAVE.toString()));
			return;
		}

		if (args.length < 2) {
			utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), String.format(Lang.CONFIRM_LEAVE_PARTY.toString(), party.name())));
		} else if (args[1].equalsIgnoreCase("confirm")) {
			List<String> listOfMembers = plugin.getYml().getStringList(party.name() + ".members");
			listOfMembers.remove(player.getUniqueId().toString());
			plugin.getYml().set(party.name() + ".members", listOfMembers);
			party.broadcast(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), String.format(Lang.PLAYER_LEFT_PARTY.toString(), player.getName())));
			plugin.save();
		}
	}

	@Override
	public String description() {
		return "Leave a party.";
	}

	@Override
	public String usage() {
		return "/party leave";
	}
}
