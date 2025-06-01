package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import partyChat.command.PartySubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import partyChat.config.Lang;
import partyChat.util.Chat;

public class DemoteMember extends PartySubCommand {

    public DemoteMember(PartyChat plugin) {
        super(plugin, "demote");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (utils.getPlayerParty(player.getUniqueId()) == null) {
            utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NOT_IN_PARTY.toString()));
            return;
        }

        Party party = utils.getPlayerParty(player.getUniqueId());

        if (!party.getLeader().equals(player.getUniqueId())) {
            utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NOT_LEADER.toString()));
            return;
        }

        if (args.length < 2) {
            utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.USAGE_PROMOTE.toString()));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

        if (!target.hasPlayedBefore() && !target.isOnline()) {
            utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_PLAYER_NOT_FOUND.toString()));
            return;
        }

        if (!party.getCaptains().contains(target.getUniqueId())) {
            utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_CANNOT_PROMOTE_FURTHER.toString()));
            return;
        }

        String path = party.name() + ".captains";
        party.getCaptains().remove(target.getUniqueId());
        plugin.getYml().set(path, party.getCaptains());
        plugin.save();

        utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(),
                Lang.PROMOTED_MESSAGE.toString(target.getName(), Lang.LEADER.toString(), player.getName())));

        if (target.isOnline()) {
            Player targetPlayer = target.getPlayer();
            if (targetPlayer != null) {
                utils.message(targetPlayer, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_CANNOT_PROMOTE_FURTHER.toString()));
            }
        }
    }

    @Override
    public String description() {
        return "Revoke invite and kick permissions.";
    }

    @Override
    public String usage() {
        return Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.USAGE_KICK_MEMBER.toString());
    }
}
