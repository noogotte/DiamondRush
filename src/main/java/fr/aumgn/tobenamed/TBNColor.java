package fr.aumgn.tobenamed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;

public enum TBNColor {

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

    private TBNColor(ChatColor chat, int wool) {
        this.chat = chat;
        this.wool = (byte) wool;
    }

    public ChatColor getChatColor() {
        return chat;
    }

    public byte getWoolColor() {
        return wool;
    }

    public static List<TBNColor> getRandomColors(int count) {
        List<TBNColor> list = new ArrayList<TBNColor>(count);
        TBNColor[] colors = values();
        for (int i = 0; i < count; i++) {
            list.add(colors[i]);
        }
        Collections.shuffle(list, TBN.getRandom());
        return list;
    }
}
