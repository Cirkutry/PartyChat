package partyChat.command.subcommands;

import partyChat.PartyChat;
import partyChat.command.PartySubCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
            utils.fail(player, "You are already in a party.");
            return;
        }

        if (args.length == 1) {
            utils.message(player, "Usage: /party create <name>");
            return;
        }

        if (!utils.isValidName(args[1])) {
            utils.fail(player, "That's not a valid party name. (Minimum 4 characters, maximum 16)");
            return;
        }

        String name = args[1];
        YamlConfiguration yml = plugin.getYml();
        ConfigurationSection theParty = yml.getConfigurationSection(name);

        if (theParty != null) {
            utils.fail(player, "The party \"" + Chat.GRAY + name + Chat.RESET + "\" already exists.");
            return;
        }

        if (args.length < 3) {
            utils.message(player, "You are about to create the party \"" + Chat.RED + name + Chat.WHITE + "\"" + ".");
            utils.message(player, "Are you sure? Type " + Chat.RED + "/party create <name> confirm " + Chat.WHITE + "to confirm.");
        } else if (args[2].equalsIgnoreCase("confirm")) {
            theParty = yml.createSection(name);

            theParty.set(".created", Utils.getCurrentDateString());
            theParty.set(".leader", player.getUniqueId().toString());
            theParty.set(".members", new ArrayList<>(Collections.singleton(player.getUniqueId().toString())));
            theParty.set(".isGuild", false);
            theParty.set(".pointBalance", 0.0);

            utils.message(player, "You have created the party " + name + "! Type /party help for a list of commands.");
            plugin.save();
        }

    }

    @Override
    public String description() {
        return "Create a new party";
    }

    @Override
    public String usage() {
        return "/party create <name>";
    }
}
