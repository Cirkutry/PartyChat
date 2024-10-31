package partyChat.command;

import partyChat.PartyChat;
import partyChat.util.PartyChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public abstract class PartySubCommand {
    protected PartyChat plugin;
    protected PartyChatUtils utils;
    private final String name;

    public PartySubCommand(PartyChat partyChat, String name) {
        this.plugin = partyChat;
        this.name = name;
        this.utils = partyChat.getUtils();
    }

    public String getName() {
        return name;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }

    /*
    All party commands are player-only
     */
    public abstract void execute(Player player, String[] args);

    public abstract String description();

    public abstract String usage();
}
