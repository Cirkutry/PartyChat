package partyChat.command.subcommands;

import partyChat.object.Party;
import partyChat.PartyChat;
import partyChat.object.Invite;
import partyChat.command.PartySubCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import partyChat.config.Lang;
import partyChat.util.Chat;

import java.util.List;

public class InviteAccept extends PartySubCommand {
    public InviteAccept(PartyChat plugin) {
        super(plugin, "accept");
    }

    @Override
    public void execute(Player player, String[] args) {
        Invite theInvite = plugin.inviteHandler().getInvite(player.getUniqueId());

        if (theInvite == null) {
            utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NO_PENDING_INVITE.toString()));
            return;
        }

        Party party = theInvite.getParty();
        YamlConfiguration yml = plugin.getYml();

        List<String> members = yml.getStringList(party.name() + ".members");
        members.add(player.getUniqueId().toString());
        yml.set(party.name() + ".members", members);
        plugin.save();

        plugin.inviteHandler().removeInviteFromMap(theInvite);

        utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.WAIT_MESSAGE.toString()));

        Bukkit.getScheduler().runTaskLater(plugin, () ->
                utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), String.format(Lang.JOINED_PARTY.toString(), party.name()))), 80);

        Bukkit.getScheduler().runTaskLater(plugin, () ->
                utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.PARTY_CHAT_HINT.toString())), 240);

        party.broadcast(Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), String.format(Lang.PLAYER_JOINED_PARTY.toString(), player.getName())));
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
