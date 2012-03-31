package fr.aumgn.tobenamed.util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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


    public static void tpTo(Player player, World world, Vector pos) {
        player.teleport(new Location(world,  pos.getX() + 0.5, 
                pos.getY() + 0.5, pos.getZ() + 0.5));
    }
}
