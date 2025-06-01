package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import partyChat.command.PartySubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import partyChat.config.Lang;
import partyChat.util.Chat;

import java.util.List;

public class PromoteMember extends PartySubCommand {
	public PromoteMember(PartyChat plugin) {
		super(plugin, "promote");
	}

	@Override
	public void execute(Player player, String[] args) {
		if (utils.getPlayerParty(player.getUniqueId()) == null) {
			utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NOT_IN_PARTY.toString())); // Added prefix
			return;
		}

		Party party = utils.getPlayerParty(player.getUniqueId());

		if (!party.isLeader(player.getUniqueId())) {
			utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NOT_LEADER.toString())); // Added prefix
			return;
		}

		if (args.length == 1) {
			utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.USAGE_PROMOTE.toString())); // Added prefix
			return;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

		if (!target.hasPlayedBefore() && !target.isOnline()) {
			utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_PLAYER_NEVER_JOINED.toString())); // Added prefix
			return;
		}

		if (!party.getMembers().contains(target.getUniqueId())) {
			utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), String.format(Lang.ERROR_NOT_PARTY_MEMBER.toString(), target.getName()))); // Added prefix
			return;
		}

		if (party.isPartyStaff(target.getUniqueId())) {
			utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_CANNOT_PROMOTE_FURTHER.toString())); // Added prefix
			return;
		}

		List<String> list = plugin.getYml().getStringList(party.name() + ".captains");
		list.add(target.getUniqueId().toString());
		party.broadcast(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), String.format(Lang.PROMOTED_MESSAGE.toString(), target.getName(), player.getName()))); // Added prefix
		plugin.getYml().set(party.name() + ".captains", list);
		plugin.save();
	}

	@Override
	public String description() {
		return Lang.DESCRIPTION_PROMOTE.toString();
	}

	@Override
	public String usage() {
		return Lang.USAGE_PROMOTE.toString();
	}
}

