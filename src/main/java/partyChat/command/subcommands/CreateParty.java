package partyChat.command.subcommands;

import partyChat.PartyChat;
import partyChat.command.PartySubCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import partyChat.config.Lang;
import partyChat.util.Chat;
import partyChat.util.Utils;
import java.util.ArrayList;
import java.util.Collections;

public class CreateParty extends PartySubCommand {

    public CreateParty(PartyChat plugin) {
        super(plugin, "create");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (utils.getPlayerParty(player.getUniqueId()) != null) {
            utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_ALREADY_IN_PARTY.toString()));
            return;
        }

        if (args.length < 2) {
            utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.USAGE_PROMOTE.toString()));
            return;
        }

        if (!utils.isValidName(args[1])) {
            utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_NOT_VALID_MEMBER.toString()));
            return;
        }

        String name = args[1];
        YamlConfiguration yml = plugin.getYml();
        ConfigurationSection theParty = yml.getConfigurationSection(name);

        if (theParty != null) {
            utils.fail(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.ERROR_PARTY_ALREADY_EXISTS.toString(name)));
            return;
        }

        if (args.length < 3) {
            utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.WARNING_CREATE_PARTY_START.toString(name)));
            utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.WARNING_CREATE_PARTY_CONFIRM.toString()));
        } else if (args[2].equalsIgnoreCase("confirm")) {
            theParty = yml.createSection(name);

            theParty.set(".created", Utils.getCurrentDateString());
            theParty.set(".leader", player.getUniqueId().toString());
            theParty.set(".members", new ArrayList<>(Collections.singleton(player.getUniqueId().toString())));

            utils.message(player, Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.INFO_PARTY_CREATED.toString(name)));
            plugin.save();
        }
    }

    @Override
    public String description() {
        return "Create a new party";
    }

    @Override
    public String usage() {
        return Chat.formatWithPrefix(Lang.PARTY_TAG.toString(), Lang.USAGE_INVITE_MEMBER.toString());
    }
}
