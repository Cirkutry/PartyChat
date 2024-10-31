package partyChat.command.subcommands;

import partyChat.PartyChat;
import partyChat.command.PartySubCommand;
import org.bukkit.entity.Player;

public class InfoCommand extends PartySubCommand {
    public InfoCommand(PartyChat party) {
        super(party, "info");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            utils.message(player, "Usage: /party info <party>");
            return;
        }

        if (utils.getPartyByName(args[1]) == null) {
            utils.message(player, "Couldn't find a party by that name.");
            return;
        }

        utils.getPartyByName(args[1]).displayPartyInfo(player);
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
