package fr.aumgn.diamondrush.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;

import fr.aumgn.bukkitutils.util.Util;

public enum TeamColor {

    BLUE          (ChatColor.BLUE,          11,     "Bleue"         ),
    RED           (ChatColor.DARK_RED,      14,     "Rouge"        ),
    GREEN         (ChatColor.DARK_GREEN,    13,     "Verte"         ),
    ORANGE        (ChatColor.GOLD,           1,     "Orange"       ),
    DARK_PURPLE   (ChatColor.DARK_PURPLE,   10,     "Mauve"        ),
    WHITE         (ChatColor.WHITE,          0,     "Blanche"        ),
    GRAY          (ChatColor.DARK_GRAY,      8,     "Grise"         ),
    AQUA          (ChatColor.AQUA,           3,     "Turquoise"    ),
    LIGHT_GREEN   (ChatColor.GREEN,          5,     "Vert clair"   ),
    BLACK         (ChatColor.BLACK,         15,     "Noire"         ),
    YELLOW        (ChatColor.YELLOW,         4,     "Jaune"        );

    private final ChatColor chat;
    private final byte wool;
    private final String name;

    private TeamColor(ChatColor chat, int wool, String name) {
        this.chat = chat;
        this.wool = (byte) wool;
        this.name = name;
    }

    public ChatColor getChatColor() {
        return chat;
    }

    public byte getWoolColor() {
        return wool;
    }

    public String getColorName() {
        return name;
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
