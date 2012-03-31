package fr.aumgn.tobenamed.util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aumgn.tobenamed.TBN;

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

    public static <T> T pickRandom(List<T> from) {
        return from.get(TBN.getRandom().nextInt(from.size()));
    }
}
