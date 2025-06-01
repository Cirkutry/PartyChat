package partyChat.command.subcommands;

import partyChat.PartyChat;
import partyChat.command.PartySubCommand;
import org.bukkit.entity.Player;
import partyChat.config.Lang;
import partyChat.util.Chat;

public class InfoCommand extends PartySubCommand {
    public InfoCommand(PartyChat plugin) {
        super(plugin, "info");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.USAGE_INFO_COMMAND.toString()));
            return;
        }

        String partyName = args[1];

        if (utils.getPartyByName(partyName) == null) {
            utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_PARTY_NOT_FOUND.toString()));
            return;
        }

        utils.getPartyByName(partyName).displayPartyInfo(player);
    }

    @Override
    public String description() {
        return "View information about a specific party.";
    }

    @Override
    public String usage() {
        return "/party info <party>";
    }
}

