package partyChat;

import partyChat.command.*;
import partyChat.command.subcommands.*;
import partyChat.config.ConfigHandler;
import partyChat.object.Party;
import partyChat.object.InviteHandler;
import partyChat.util.PartyChatUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PartyChat extends JavaPlugin implements CommandExecutor, TabCompleter {
    private static final List<Party> parties = new ArrayList<>();
    private final List<PartySubCommand> subCommands = new ArrayList<>();

    private ConfigHandler config;
    private InviteHandler inviteHandler;
    private PartyChatUtils partyChatUtils;
    private YamlConfiguration partyYml;

    private File partyFile;
    public static final String VERSION = "1.0-1.21.1";

    public void onEnable() {
        partyFile = new File(getDataFolder(), "parties.yml");
        partyYml = YamlConfiguration.loadConfiguration(partyFile);

        rebuildPartyList();

        config = new ConfigHandler();
        inviteHandler = new InviteHandler(this);
        partyChatUtils = new PartyChatUtils(this);

        TogglePartyChat togglePartyChat = new TogglePartyChat(this);

        subCommands.add(new CreateParty(this));
        subCommands.add(new DisbandParty(this));
        subCommands.add(new InviteMember(this));
        subCommands.add(new InviteAccept(this));
        subCommands.add(new InviteDecline(this));
        subCommands.add(new PromoteMember(this));
        subCommands.add(new DemoteMember(this));
        subCommands.add(new LeaveParty(this));
        subCommands.add(new KickMember(this));
        subCommands.add(new InfoCommand(this));

        new CommandBase(this);
        new PartyChatCommand(this);

        new ChatListener(this, togglePartyChat);

        this.getCommand("pc").setExecutor(togglePartyChat);

        new Metrics(this, 23689);

        getLogger().info("PartyChat v" + VERSION + " has loaded.");
    }

    public void onDisable() {
        getLogger().info("PartyChat v" + VERSION + " has unloaded.");
    }

    public PartyChatUtils getUtils() {
        return partyChatUtils;
    }

    public InviteHandler inviteHandler() {
        return inviteHandler;
    }

    public YamlConfiguration getYml() {
        return partyYml;
    }

    public ConfigHandler config() { return config; }

    public List<Party> getParties() {
        return parties;
    }

    public List<PartySubCommand> getCommands() {
        return subCommands;
    }

    /**
     * Save the YML and re-create party list
     */
    public void save() {
        try {
            partyYml.save(partyFile);
            partyYml = YamlConfiguration.loadConfiguration(partyFile);
            rebuildPartyList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Re-create party data from yml
     * This is called when saving (aka upon any party data change)
     */
    private void rebuildPartyList() {
        parties.clear();

        for (String key : partyYml.getKeys(false)) {
            ConfigurationSection theYml = partyYml.getConfigurationSection(key);

            Party aParty = new Party(Objects.requireNonNull(theYml));
            parties.add(aParty);
        }
    }
}
