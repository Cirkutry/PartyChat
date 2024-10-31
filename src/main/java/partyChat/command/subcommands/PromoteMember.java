package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import partyChat.command.PartySubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import partyChat.config.Lang;

import java.util.List;

public class PromoteMember extends PartySubCommand {
	public PromoteMember(PartyChat plugin) {
		super(plugin, "promote");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (utils.getPlayerParty(player.getUniqueId()) == null) {
			utils.message(player, "You are not in a party.");
			return;
		}

		Party party = utils.getPlayerParty(player.getUniqueId());

		if (!party.isLeader(player.getUniqueId())) {
			utils.fail(player, "Only the party leader can do that.");
			return;
		}

		if (args.length == 1) {
			utils.message(player, "Usage: /party promote <player>");
			return;
		}

		@SuppressWarnings("deprecation")
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

		if (!target.hasPlayedBefore() && !target.isOnline()) {
			utils.fail(player, "That player has never joined the server before.");
			return;
		}

		if (!party.getMembers().contains(target.getUniqueId())) {
			utils.message(player, target.getName() + " is not a member of your party.");
			return;
		}

		if (party.isPartyStaff(target.getUniqueId())) {
			utils.message(player, "You cannot promote that party member any further.");
			return;
		}

		List<String> list = plugin.getYml().getStringList(party.name() + ".captains");
		list.add(target.getUniqueId().toString());
		party.broadcast(target.getName() + " has been promoted to party " + Lang.TRUSTED_RANK + " by "
				+ player.getName() + ".");
		plugin.getYml().set(party.name() + ".captains", list);
		plugin.save();
	}

	@Override
	public String description() {
		return "Give a member invite and kick permissions.";
	}

	@Override
	public String usage() {
		return "/party promote <player>";
	}
}
