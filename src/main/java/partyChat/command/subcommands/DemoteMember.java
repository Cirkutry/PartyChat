package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import partyChat.command.PartySubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import partyChat.config.Lang;

public class DemoteMember extends PartySubCommand {

    public DemoteMember(PartyChat plugin) {
        super(plugin, "demote");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (utils.getPlayerParty(player.getUniqueId()) == null) {
            utils.fail(player, "You are not a member of any party.");
            return;
        }

        Party party = utils.getPlayerParty(player.getUniqueId());

        if (!party.getLeader().equals(player.getUniqueId())) {
            utils.fail(player, "Only the party leader can do that.");
            return;
        }

        if (args.length == 1) {
            utils.message(player, "Usage: /party demote <" + Lang.TRUSTED_RANK + ">");
            return;
        }

        @SuppressWarnings("deprecation")
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]); // the captain to be kicked

        if (!target.hasPlayedBefore() && !target.isOnline()) {
            utils.fail(player, "Couldn't find that player.");
            return;
        }

        if (!party.getCaptains().contains(target.getUniqueId())) {
            utils.fail(player, "That player is not a " + Lang.TRUSTED_RANK + ".");
            return;
        }

        String path = party.name() + ".captains";
        utils.message(player, "You have demoted " + target.getName() + ".");
        plugin.getYml().set(path, plugin.getYml().getStringList(path).remove(target.getUniqueId().toString()));
        plugin.save();
    }

    @Override
    public String description() {
        return "Revoke invite and kick permissions.";
    }

    @Override
    public String usage() {
        return "/party demote <player>";
    }
}
