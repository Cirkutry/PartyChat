package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import partyChat.util.Chat;

/**
 * This class does not inherit PartySubCommand because this is just the /p command
 */
public class PartyChatCommand implements CommandExecutor {
    private final PartyChat plugin;

    public PartyChatCommand(PartyChat plugin) {
        this.plugin = plugin;

        plugin.getCommand("p").setExecutor(this);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(Chat.RED + "Sorry, that's a player only command.");
            return true;
        }

        Player player = (Player) sender;
        Party party = plugin.getUtils().getPlayerParty(player.getUniqueId());

        if (party == null || args.length == 0) {
            plugin.getUtils().displayMenu(player);
            return true;
        }

        StringBuilder message = new StringBuilder();

        for (String arg : args) {
            message.append(arg).append(" ");
        }

        party.broadcast(player.getName() + ": " + message);
        return false;
    }

}
