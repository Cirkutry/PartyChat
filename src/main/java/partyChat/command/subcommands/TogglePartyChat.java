package partyChat.command.subcommands;

import partyChat.PartyChat;
import partyChat.util.PartyChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import partyChat.util.Chat;
import java.util.UUID;
import java.util.HashMap;

public class TogglePartyChat implements CommandExecutor {
    private final PartyChat plugin;
    private final PartyChatUtils utils;

    // HashMap to store player chat modes in memory (true = Party Chat, false = Public Chat)
    private final HashMap<UUID, Boolean> playerChatModes = new HashMap<>();

    public TogglePartyChat(PartyChat plugin) {
        this.plugin = plugin;
        this.utils = plugin.getUtils(); // Get the utils from the plugin instance
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can toggle chat settings.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        if (utils.getPlayerParty(playerUUID) == null) {
            utils.fail(player, "You are not a member of any party.");
            return true;
        }

        boolean isPartyChatEnabled = playerChatModes.getOrDefault(playerUUID, false);

        if (isPartyChatEnabled) {
            // Switch to public chat mode
            playerChatModes.put(playerUUID, false);
            utils.message(player, "Default chat has been set to "
                    + Chat.GREEN + "Public Chat" + Chat.RESET + ".");
        } else {
            // Switch to party chat mode
            playerChatModes.put(playerUUID, true);
            utils.message(player, "Default chat has been set to " + Chat.AQUA + "Party Chat" + Chat.RESET + ".");
        }

        plugin.getConfig().set("partyChat." + playerUUID, playerChatModes.get(playerUUID));
        plugin.saveConfig();

        return true;
    }

    // Method to check if a player is currently in party chat mode
    public boolean isPlayerInPartyChat(UUID playerUUID) {
        return playerChatModes.getOrDefault(playerUUID, false);
    }
}
