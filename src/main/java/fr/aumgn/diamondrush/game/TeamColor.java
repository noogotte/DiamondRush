package fr.aumgn.diamondrush.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;

import fr.aumgn.diamondrush.util.Util;

public enum TeamColor {

    BLUE          (ChatColor.BLUE,          11),
    RED           (ChatColor.DARK_RED,      14),
    GREEN         (ChatColor.DARK_GREEN,    13),
    PURPLE        (ChatColor.LIGHT_PURPLE,   2),
    ORANGE        (ChatColor.GOLD,           1),
    WHITE         (ChatColor.WHITE,          0),
    GRAY          (ChatColor.DARK_GRAY,      8),
    AQUA          (ChatColor.AQUA,           3),
    DARK_PURPLE   (ChatColor.DARK_PURPLE,   10),
    LIGHT_GREEN   (ChatColor.GREEN,          5),
    BLACK         (ChatColor.BLACK,         15),
    YELLOW        (ChatColor.YELLOW,         4);

    private final ChatColor chat;
    private final byte wool;

    private TeamColor(ChatColor chat, int wool) {
        this.chat = chat;
        this.wool = (byte) wool;
    }

    public ChatColor getChatColor() {
        return chat;
    }

    public byte getWoolColor() {
        return wool;
    }

    public static List<TeamColor> getRandomColors(int count) {
        List<TeamColor> list = new ArrayList<TeamColor>(count);
        TeamColor[] colors = values();
        for (int i = 0; i < count; i++) {
            list.add(colors[i]);
        }
        Collections.shuffle(list, Util.getRandom());
        return list;
    }
}
