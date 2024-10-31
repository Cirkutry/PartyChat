package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import partyChat.command.PartySubCommand;
import org.bukkit.entity.Player;
import partyChat.util.Chat;

public class DisbandParty extends PartySubCommand {
    public DisbandParty(PartyChat plugin) {
        super(plugin, "disband");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (utils.getPlayerParty(player.getUniqueId()) == null) {
            utils.fail(player, "You are not a member of any party.");
            return;
        }

        Party party = utils.getPlayerParty(player.getUniqueId());

        if (!party.getLeader().equals(player.getUniqueId())) {
            utils.fail(player, "Only the leader can disband the party.");
            return;
        }

        if (args.length < 3) {
            var WARN = Chat.RED + "[!] " + Chat.RESET;
            player.sendMessage(WARN + "You are about to disband " + Chat.GRAY + party.name() + Chat.WHITE + " forever.");
            player.sendMessage(WARN + "Are you sure? Type " + Chat.RED + "/party disband <partyName> confirm" + Chat.RESET + " to continue.");
        } else if (args[1].equals(party.name()) && args[2].equals("confirm")) {
            party.broadcast("Your party has been disbanded.");
            plugin.getYml().set(party.name(), null);
            plugin.save();
        }
    }


    @Override
    public String description() {
        return "Disband a party";
    }

    @Override
    public String usage() {
        return "/party disband";
    }
}
