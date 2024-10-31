package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import partyChat.command.PartySubCommand;
import org.bukkit.entity.Player;
import partyChat.util.Chat;

import java.util.List;

public class LeaveParty extends PartySubCommand {
	public LeaveParty(PartyChat plugin) {
		super(plugin, "leave");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (utils.getPlayerParty(player.getUniqueId()) == null) {
			utils.fail(player, "You are not in a party.");
			return;
		}

		Party party = utils.getPlayerParty(player.getUniqueId());

		if (party.getLeader().equals(player.getUniqueId())) {
			utils.fail(player, "You are the party leader. To disband your party, type /party disband.");
			return;
		}

		if (args.length < 2) {
			utils.message(player, "Are you sure you want to leave " + Chat.GRAY + party.name() + Chat.RESET
					+ "? Type " + Chat.RED + "/party leave confirm " + Chat.RESET + "to continue.");
		} else if (args[1].equalsIgnoreCase("confirm")) {
			List<String> listOfMembers = plugin.getYml().getStringList(party.name() + ".members");
			listOfMembers.remove(player.getUniqueId().toString());
			String memberPath = party.name() + ".members";
			plugin.getYml().set(memberPath, listOfMembers);
			party.broadcast(player.getName() + " has left the party.");
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
