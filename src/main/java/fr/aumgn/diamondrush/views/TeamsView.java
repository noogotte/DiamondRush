package fr.aumgn.diamondrush.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.aumgn.diamondrush.game.Team;

public class TeamsView implements Iterable<String> {

    private List<String> messages;

    public TeamsView(List<Team> teams) {
        this.messages = new  ArrayList<String>();
        messages.add("Equipes : ");
        for (Team team : teams) {
            StringBuilder message = new StringBuilder();
            message.append(" - ");
            message.append(ChatColor.BOLD);
            message.append(team.getDisplayName());
            message.append(ChatColor.BLUE);
            message.append(" (");
            message.append(team.getLives());
            message.append(")");
            message.append(ChatColor.RESET);
            if (team.size() > 0) {
                message.append(" : ");
                Player foreman = team.getForeman();
                if (foreman != null) {
                    message.append(ChatColor.BOLD);
                    if (!foreman.isOnline()) {
                        message.append(ChatColor.ITALIC);
                    }
                    message.append(foreman.getDisplayName());
                    message.append(" ");
                }
                for (Player player : team.getPlayers()) {
                    if (player.equals(foreman)) {
                        continue;
                    }
                    if (!player.isOnline()) {
                        message.append(ChatColor.ITALIC);
                    }
                    message.append(player.getDisplayName());
                    message.append(" ");
                }
            }
            messages.add(message.toString());
        }
    }

    @Override
    public Iterator<String> iterator() {
        return messages.iterator();
    }
}