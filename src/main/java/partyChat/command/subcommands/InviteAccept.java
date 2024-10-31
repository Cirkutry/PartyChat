package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import partyChat.object.Invite;
import partyChat.command.PartySubCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import partyChat.util.Chat;

import java.util.List;

public class InviteAccept extends PartySubCommand {
    public InviteAccept(PartyChat plugin) {
        super(plugin, "accept");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (plugin.inviteHandler().getInvite(player.getUniqueId()) == null) {
            utils.message(player, "You do not have a pending invite.");
            return;
        }

        Invite theInvite = plugin.inviteHandler().getInvite(player.getUniqueId());

        YamlConfiguration yml = plugin.getYml();

        Party party = theInvite.getParty();

        List<String> l = yml.getStringList(party.name() + ".members");
        l.add(player.getUniqueId().toString());
        yml.set(party.name() + ".members", l);
        plugin.save();

        plugin.inviteHandler().removeInviteFromMap(theInvite);

        utils.message(player, "Please wait...");

        Bukkit.getScheduler().runTaskLater(plugin, () ->
                utils.message(player, "You have joined the party" + party.name() + "!"), 80);

        Bukkit.getScheduler().runTaskLater(plugin, () ->
                utils.message(player, "Hint: " + Chat.GRAY
                        + "Type /party <message> to talk within your party."), 240);

        party.broadcast(player.getName() + " has joined the party!");
    }

    @Override
    public String description() {
        return "Accept an invite.";
    }

    @Override
    public String usage() {
        return "/party accept";
    }
}
