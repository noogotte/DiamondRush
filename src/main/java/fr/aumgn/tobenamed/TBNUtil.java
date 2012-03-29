package fr.aumgn.tobenamed;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class TBNUtil {

    private TBNUtil() {
    }

    public static void broadcast(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    public static void broadcast(String permission, String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permission)) {
                player.sendMessage(message);
            }
        }
    }
}
