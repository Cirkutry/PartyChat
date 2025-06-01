package partyChat.command.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import partyChat.PartyChat;
import partyChat.config.Lang;
import partyChat.util.Chat;

public class ReloadCommand implements CommandExecutor {

    private final PartyChat plugin;

    public ReloadCommand(PartyChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            // Console execution
            if (plugin.reloadLang()) {
                sender.sendMessage(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.RELOAD_SUCCESS.toString()));
            } else {
                sender.sendMessage(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.RELOAD_FAILURE.toString()));
            }
            return true;
        }

        // If sender is a player, check for permission
        Player player = (Player) sender;
        if (!player.hasPermission("partychat.reload")) {
            player.sendMessage(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.RELOAD_NO_PERMISSION.toString()));
            return true;
        }

        // Reload the language files for the player
        if (plugin.reloadLang()) {
            player.sendMessage(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.RELOAD_SUCCESS.toString()));
        } else {
            player.sendMessage(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.RELOAD_FAILURE.toString()));
        }
        return true;
    }
}

