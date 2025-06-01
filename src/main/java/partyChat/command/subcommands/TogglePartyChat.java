package partyChat.command.subcommands;

import partyChat.PartyChat;
import partyChat.util.Chat;
import partyChat.util.PartyChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import partyChat.config.Lang;
import java.util.UUID;
import java.util.HashMap;

public class TogglePartyChat implements CommandExecutor {
    private final PartyChat plugin;
    private final PartyChatUtils utils;

    private final HashMap<UUID, Boolean> playerChatModes = new HashMap<>();

    public TogglePartyChat(PartyChat plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NO_PERMISSIONS.toString()));
            return true;
        }

        UUID playerUUID = player.getUniqueId();

        if (utils.getPlayerParty(playerUUID) == null) {
            utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NOT_IN_PARTY.toString())); // Added prefix
            return true;
        }

        boolean isPartyChatEnabled = playerChatModes.getOrDefault(playerUUID, false);

        if (isPartyChatEnabled) {
            playerChatModes.put(playerUUID, false);
            utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.CHAT_MODE_PUBLIC.toString())); // Added prefix
        } else {
            playerChatModes.put(playerUUID, true);
            utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.CHAT_MODE_PARTY.toString())); // Added prefix
        }

        plugin.getConfig().set("partyChat." + playerUUID, playerChatModes.get(playerUUID));
        plugin.saveConfig();

        return true;
    }

    public boolean isPlayerInPartyChat(UUID playerUUID) {
        return playerChatModes.getOrDefault(playerUUID, false);
    }
}

