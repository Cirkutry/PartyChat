package partyChat;

import partyChat.command.*;
import partyChat.command.subcommands.*;
import partyChat.config.ConfigHandler;
import partyChat.config.Lang;
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
    public static final String VERSION = "1.1-1.21.1";

    @Override
    public void onLoad() {
        loadLangFile();
        getLogger().info("Language file loaded successfully!");
    }

    @Override
    public void onEnable() {
        partyFile = new File(getDataFolder(), "parties.yml");
        partyYml = YamlConfiguration.loadConfiguration(partyFile);
        rebuildPartyList();

        config = new ConfigHandler();
        inviteHandler = new InviteHandler(this);
        partyChatUtils = new PartyChatUtils(this);

        // Register party subcommands
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

        this.getCommand("partyreload").setExecutor(new ReloadCommand(this));

        new ChatListener(this, togglePartyChat);
        Objects.requireNonNull(this.getCommand("pc")).setExecutor(togglePartyChat);

        new Metrics(this, 23689);

        getLogger().info("PartyChat v" + VERSION + " has loaded.");
    }

    @Override
    public void onDisable() {
        if (partyYml != null) {
            save();
        } else {
            getLogger().warning("Could not save parties.yml because it was not initialized.");
        }
        getLogger().info("PartyChat v" + VERSION + " has unloaded.");
    }

    public boolean reloadLang() {
        try {
            loadLangFile();
            return true;
        } catch (Exception e) {
            getLogger().severe("Failed to reload language file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void loadLangFile() {
        File langFile = new File(getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            saveResource("lang.yml", false);
        }
        YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
        Lang.setConfig(langConfig);
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

    public ConfigHandler config() {
        return config;
    }

    public List<Party> getParties() {
        return parties;
    }

    public List<PartySubCommand> getCommands() {
        return subCommands;
    }

    public void save() {
        if (partyYml != null && partyFile != null) {
            try {
                partyYml.save(partyFile);
                partyYml = YamlConfiguration.loadConfiguration(partyFile);
                rebuildPartyList();
            } catch (IOException e) {
                getLogger().severe("Failed to save the parties.yml file: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            getLogger().warning("parties.yml file is null, cannot save.");
        }
    }

    private void rebuildPartyList() {
        parties.clear();
        for (String key : partyYml.getKeys(false)) {
            ConfigurationSection theYml = partyYml.getConfigurationSection(key);
            if (theYml == null) {
                getLogger().warning("Invalid party data for key: " + key);
                continue;
            }
            try {
                Party aParty = new Party(theYml);
                parties.add(aParty);
            } catch (Exception e) {
                getLogger().severe("Failed to load party data for key: " + key);
                e.printStackTrace();
            }
        }
    }
}