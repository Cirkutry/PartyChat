package partyChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import partyChat.config.Lang;
import partyChat.object.Party;
import partyChat.util.PartyChatUtils;
import partyChat.command.subcommands.TogglePartyChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final PartyChatUtils utils;
    private final TogglePartyChat togglePartyChat;

    public ChatListener(PartyChat plugin, TogglePartyChat togglePartyChat) {
        this.utils = plugin.getUtils();
        this.togglePartyChat = togglePartyChat;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // This handles the case where a player has toggled default chat to party chat
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        Party party = utils.getPlayerParty(player.getUniqueId());

        if (utils.playerInParty(player.getUniqueId()) && togglePartyChat.isPlayerInPartyChat(player.getUniqueId())) {
            e.setCancelled(true);

            String messageTemplate = Lang.PARTY_CHAT_MESSAGE.toString();
            String message = String.format(messageTemplate, player.getName(), e.getMessage());
            String formattedMessageInGame = Lang.formatColors(message);
            party.broadcast(formattedMessageInGame);

            String formattedMessageConsole = ChatColor.stripColor(formattedMessageInGame);
            Bukkit.getLogger().info("[PartyChatLogger] " + formattedMessageConsole);
        }
    }
}
