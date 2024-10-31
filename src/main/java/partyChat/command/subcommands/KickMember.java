package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import partyChat.command.PartySubCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class KickMember extends PartySubCommand {
    public KickMember(PartyChat plugin) {
        super(plugin, "kick");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (utils.getPlayerParty(player.getUniqueId()) == null) {
            utils.fail(player, "You are not in any party.");
            return;
        }

        Party party = utils.getPlayerParty(player.getUniqueId());

        if (!party.isLeader(player.getUniqueId())) {
            utils.fail(player, "Only the party leader can do that.");
            return;
        }

        if (args.length == 1) {
            utils.message(player, "Usage: /party kick <player>");
            return;
        }

        @SuppressWarnings("deprecation")
        OfflinePlayer toKick = Bukkit.getOfflinePlayer(args[1]);

        if (toKick.hasPlayedBefore() && !toKick.isOnline()) {
            utils.fail(player, "That's not a valid party member.");
            return;
        }

        if (!party.getMembers().contains(toKick.getUniqueId())) {
            utils.fail(player, "That player isn't a member of your party.");
            return;
        }

        if (toKick == player) {
            utils.message(player, "You cannot kick yourself!");
            return;
        }

        List<String> list = plugin.getYml().getStringList(party.name() + ".members");
        list.remove(toKick.getUniqueId().toString());
        plugin.getYml().set(party.name() + ".members", list);

        // we need to remove player from captain list, if they're a captain
        if (party.isPartyStaff(toKick.getUniqueId())) {
            List<String> captains = plugin.getYml().getStringList(party.name() + ".captains");
            captains.remove(toKick.getUniqueId().toString());
            plugin.getYml().set(party.name() + ".captains", captains);
        }

        if (toKick.isOnline()) {
            utils.message(((Player) toKick), "You have been kicked from the party.");
        }

        plugin.getYml().set(party.name() + ".members", list);
        plugin.save();
        party.broadcast(toKick.getName() + " has been kicked from the party.");
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
