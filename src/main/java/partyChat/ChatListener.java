package partyChat;

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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Party party = utils.getPlayerParty(p.getUniqueId());

        if (utils.playerInParty(p.getUniqueId()) && togglePartyChat.isPlayerInPartyChat(p.getUniqueId())) {
            e.setCancelled(true);

            party.broadcast(p.getName() + ": " + e.getMessage());
        }
    }
}
