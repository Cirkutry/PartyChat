package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import partyChat.command.PartySubCommand;
import org.bukkit.entity.Player;
import partyChat.util.Chat;
import partyChat.config.Lang;

public class DisbandParty extends PartySubCommand {
    public DisbandParty(PartyChat plugin) {
        super(plugin, "disband");
    }

    @Override
    public void execute(Player player, String[] args) {
        Party party = utils.getPlayerParty(player.getUniqueId());

        if (party == null) {
            utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NOT_IN_PARTY.toString()));
            return;
        }

        if (!party.getLeader().equals(player.getUniqueId())) {
            utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NOT_LEADER.toString()));
            return;
        }

        if (args.length < 3) {
            player.sendMessage(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.WARNING_DISBAND_PARTY_START.toString(party.name())));
            player.sendMessage(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), String.valueOf(Lang.WARNING_DISBAND_PARTY_CONFIRM)));
        } else if (args[1].equalsIgnoreCase(party.name()) && args[2].equalsIgnoreCase("confirm")) {
            party.broadcast(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.INFO_PARTY_DISBANDED.toString()));
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
        return "/party disband <partyname> confirm";
    }
}

