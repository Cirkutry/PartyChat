package partyChat.util;

import org.bukkit.ChatColor;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface Chat {
    SimpleDateFormat niceLookingDate = new SimpleDateFormat("yyyy-MM-dd");
    String format = niceLookingDate.format(new Date());

    /**
     * Send a simple formatted message using legacy color codes.
     * @param rawMessage The raw message with &-based color codes.
     * @return String The formatted message.
     */
    static String formatMessage(String rawMessage) {
        return ChatColor.translateAlternateColorCodes('&', rawMessage);
    }

    /**
     * Format a message with a prefix using legacy color codes.
     * @param prefix The prefix to be added before the message.
     * @param message The message to be formatted.
     * @return String The message with the formatted prefix.
     */
    static String formatWithPrefix(String prefix, String message) {
        String formattedPrefix = prefix + "&r ";
        return formatMessage(formattedPrefix + message);
    }
}