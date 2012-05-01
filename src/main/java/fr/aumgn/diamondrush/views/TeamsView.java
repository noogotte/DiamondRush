package fr.aumgn.diamondrush.views;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.aumgn.diamondrush.game.Team;

public class TeamsView extends MessagesView {

    public TeamsView(List<Team> teams) {
        super();

        headLn("Equipes");
        for (Team team : teams) {
            append(" - ");
            append(ChatColor.BOLD);
            append(team.getDisplayName());
            append(ChatColor.BLUE);
            append(" (");
            append(team.getLives());
            append(")");
            append(ChatColor.RESET);
            if (team.size() > 0) {
                append(" :");
                for (Player player : team.getPlayers()) {
                    append(" ");
                    if (!player.isOnline()) {
                        append(ChatColor.ITALIC);
                    }
                    append(player.getDisplayName());
                }
            }
            nl();
        }
    }
}
