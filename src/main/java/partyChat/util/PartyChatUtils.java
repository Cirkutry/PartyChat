package partyChat.util;

import partyChat.object.Party;
import partyChat.PartyChat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import partyChat.config.ConfigHandler;
import partyChat.config.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartyChatUtils {
    private final PartyChat plugin;

    public PartyChatUtils(PartyChat plugin) {
        this.plugin = plugin;
    }

    public void fail(Player player, String why) {
        player.sendMessage(Chat.RED + "Error: " + Chat.WHITE + why);
    }

    public void message(Player player, String msg) {
        player.sendMessage(Lang.PARTY_TAG + msg);
    }

    public boolean isValidName(String theName) {
        if (theName.length() < 4 || theName.length() > 16) return false;
        return !Utils.chatInArray(theName, plugin.config().disallowedInName);
    }

    public List<String> getPartyNames() {
        List<String> results = new ArrayList<>();
        plugin.getParties().forEach(party -> results.add(party.name()));
        return results;
    }

    public Party getPartyByName(String name) {
        Party theParty = null;
        for (Party party : plugin.getParties()) {
            if (party.name().equalsIgnoreCase(name)) {
                theParty = party;
            }
        }
        return theParty;
    }

    public Party getPlayerParty(UUID uuid) {
        Party theParty = null;
        for (Party party : plugin.getParties()) {
            if (party.getMembers().contains(uuid)) {
                theParty = party;
            }
        }
        return theParty;
    }

    public boolean playerInParty(UUID uuid) {
        Party theParty = null;
        for (Party party : plugin.getParties()) {
            if (party.getMembers().contains(uuid)) {
                theParty = party;
            }
        }
        return theParty != null;
    }

    public boolean partyChatEnabled(Player p) {
        return plugin.getConfig().getBoolean("partyChat." + p.getUniqueId());
    }

    // todo: do this via OOP
    public void displayMenu(CommandSender p) {
        String bar = Chat.GRAY + Chat.bar(30) + Chat.RESET;
        String arrow = Chat.DARK_GRAY + "> " + Chat.AQUA;
        String desc = Chat.WHITE + "- ";
        String command = Chat.WHITE + "";
        String arg = Chat.GRAY + "";
        p.sendMessage(bar + Chat.AQUA + " [Party Help] " + bar);
        p.sendMessage(arrow + "/party " + command + "create" + arg + " <name> " + desc + "Create a party for your friends.");
        p.sendMessage(arrow + "/party " + command + "leave " + arg + "<player> " + desc + "Leave your current party");
        p.sendMessage(arrow + "/party " + command + "invite " + arg + "<player> " + desc
                + "Invite a player to join your party");
        p.sendMessage(
                arrow + "/party " + command + "kick " + arg + "<player> " + desc + "Remove a player from your party");
        p.sendMessage(arrow + "/party " + command + "disband " + arg + "<name> " + desc + "Disband your party");
        p.sendMessage(
                arrow + "/party " + command + "promote " + arg + "<player> " + desc + "Promote a member to captain");
        p.sendMessage(
                arrow + "/pc " + desc + "Toggle default chat between party and public");
        p.sendMessage(arrow + "/p " + command + arg + "<message> " + desc + "Send a message to your party");
    }
}
