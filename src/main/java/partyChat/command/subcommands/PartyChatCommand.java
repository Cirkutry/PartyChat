package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import partyChat.config.Lang;
import partyChat.util.Chat;

public class PartyChatCommand implements CommandExecutor {
    private final PartyChat plugin;

    public PartyChatCommand(PartyChat plugin) {
        this.plugin = plugin;
        plugin.getCommand("p").setExecutor(this);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_PLAYER_ONLY.toString())); // Added prefix
            return true;
        }

        Player player = (Player) sender;
        Party party = plugin.getUtils().getPlayerParty(player.getUniqueId());

        if (party == null) {
            plugin.getUtils().displayMenu(player);
            return true;
        }

        if (args.length == 0) {
            plugin.getUtils().message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NO_MESSAGE.toString())); // Added prefix
            return true;
        }

        String message = String.join(" ", args);
        party.broadcast(String.format(Lang.PARTY_CHAT_MESSAGE.toString(), player.getName(), message));
        return true;
    }
}

