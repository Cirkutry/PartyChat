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

public class KickMember extends PartySubCommand {
    public KickMember(PartyChat plugin) {
        super(plugin, "kick");
    }

    @Override
    public void execute(Player player, String[] args) {
        Party party = utils.getPlayerParty(player.getUniqueId());

        if (party == null) {
            utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NOT_IN_PARTY.toString()));
            return;
        }

        if (!party.isLeader(player.getUniqueId())) {
            utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_LEADER_CANNOT_KICK.toString()));
            return;
        }

        if (args.length < 2) {
            utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.USAGE_KICK_MEMBER.toString()));
            return;
        }

        OfflinePlayer toKick = Bukkit.getOfflinePlayer(args[1]);

        if (!toKick.hasPlayedBefore() && !toKick.isOnline()) {
            utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NOT_VALID_MEMBER.toString()));
            return;
        }

        if (!party.getMembers().contains(toKick.getUniqueId())) {
            utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NOT_A_MEMBER.toString()));
            return;
        }

        if (toKick.getUniqueId().equals(player.getUniqueId())) {
            utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_CANNOT_KICK_SELF.toString()));
            return;
        }

        List<String> list = plugin.getYml().getStringList(party.name() + ".members");
        list.remove(toKick.getUniqueId().toString());
        plugin.getYml().set(party.name() + ".members", list);

        if (party.isPartyStaff(toKick.getUniqueId())) {
            List<String> captains = plugin.getYml().getStringList(party.name() + ".captains");
            captains.remove(toKick.getUniqueId().toString());
            plugin.getYml().set(party.name() + ".captains", captains);
        }

        if (toKick.isOnline()) {
            utils.message((Player) toKick, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.MEMBER_KICKED.toString()));
        }

        plugin.save();
        party.broadcast(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), String.format(Lang.PLAYER_KICKED.toString(), toKick.getName())));
    }

    @Override
    public String description() {
        return "Kick a player from the party.";
    }

    @Override
    public String usage() {
        return "/party kick <player>";
    }
}

