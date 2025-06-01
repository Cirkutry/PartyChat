package partyChat.object;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import partyChat.util.Chat;
import partyChat.config.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Party {
    private final List<UUID> captainUUIDS = new ArrayList<>();
    private final List<UUID> memberUUIDS = new ArrayList<>();

    private final String createdOn;
    private String name;
    private UUID leaderUUID;

    public Party(ConfigurationSection yml) {
        name = yml.getName();
        createdOn = yml.getString(".created");
        leaderUUID = UUID.fromString(yml.getString(".leader"));

        yml.getStringList(".members").forEach(string -> memberUUIDS.add(UUID.fromString(string)));
        yml.getStringList(".captains").forEach(string -> captainUUIDS.add(UUID.fromString(string)));
    }

    /**
     * Get the name of the party
     *
     * @return name of the party
     */
    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get total amount of members, including leader and captains
     *
     * @return size of the party
     */
    public int size() {
        return memberUUIDS.size();
    }

    /**
     * Get the date the party was created on
     *
     * @return mm/dd/yyyy string
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     * Gets all captain UUIDs, not including the leader
     *
     * @return List of UUIDs
     */
    public List<UUID> getCaptains() {
        return captainUUIDS;
    }

    /**
     * Gets all member UUIDs, including the leader and captains
     *
     * @return List of UUIDs
     */
    public List<UUID> getMembers() {
        return memberUUIDS;
    }

    /**
     * Broadcast a message to all online party members
     *
     * @param message: The message to broadcast to all online party members
     */
    public void broadcast(String message) {
        for (UUID memberId : memberUUIDS) {
            Player member = Bukkit.getPlayer(memberId);
            if (member != null) {
                member.sendMessage(message);
            }
        }
    }

    /**
     * Obtain a list of the party member's usernames; This INCLUDES captains and the leader
     *
     * @return A String list of usernames
     */
    public List<String> getMemberUsernames() {
        var usernames = new ArrayList<String>();

        memberUUIDS.forEach(uuid -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            usernames.add(player.getName());
        });

        return usernames;
    }

    /**
     * Obtain a list of the party member's usernames; This does NOT include captains or the leader
     *
     * @return usernames of non-captain, leader members
     */
    public List<String> getLowerClassMembersUsernames() {
        var usernames = new ArrayList<String>();

        memberUUIDS.stream().filter(m -> !isPartyStaff(m)).forEach(uuid -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            usernames.add(player.getName());
        });

        return usernames;
    }

    public List<String> getCaptainUsernames() {
        var usernames = new ArrayList<String>();

        captainUUIDS.forEach(uuid -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            usernames.add(player.getName());
        });

        return usernames;
    }

    public String captainsAsString() {
        return getUsernames(captainUUIDS);
    }

    public String getMembersAsString() {
        return getUsernames(memberUUIDS);
    }

    /*
    Internal method for the above two methods
     */
    private String getUsernames(List<UUID> memberUUIDS) {
        StringBuilder list = new StringBuilder();

        for (UUID id : memberUUIDS) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(id);
            if (!list.isEmpty()) {
                list.append(", ");
            }
            list.append(p.getName());
        }
        return list.toString();
    }

    public List<Player> getOnlineMembers() {
        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> memberUUIDS.contains(p.getUniqueId())).collect(Collectors.toList());
    }

    /**
     * Is this player a party captain or party leader?
     *
     * @param uuid: The UUID of the player to check
     * @return boolean value
     */
    public boolean isPartyStaff(UUID uuid) {
        return leaderUUID.equals(uuid) || captainUUIDS.contains(uuid);
    }

    public boolean isLeader(UUID uuid) {
        return leaderUUID.equals(uuid);
    }

    public UUID getLeader() {
        return leaderUUID;
    }

    public void setLeaderUUID(UUID uuid) {
        leaderUUID = uuid;
    }

    public void displayPartyInfo(CommandSender s) {
        s.sendMessage(Chat.formatWithPrefix(String.valueOf(Lang.PARTY_TAG), "Party Info"));
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_NAME_LABEL.toString() + name));
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.LEADER + ": &7" + Bukkit.getOfflinePlayer(leaderUUID).getName()));
        if (!captainUUIDS.isEmpty()) {
            s.sendMessage(ChatColor.translateAlternateColorCodes('&', StringUtils.capitalize(Lang.TRUSTED_RANK + "s") + ": &7" + captainsAsString()));
        }
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.CREATED_ON + ": &7" + createdOn));
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.MEMBERS + ": &7(" + memberUUIDS.size() + ") &r" + getMembersAsString()));
    }

}
