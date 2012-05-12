package fr.aumgn.diamondrush.views;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Statistics;

public class PlayerView extends MessagesView {

    public PlayerView(DiamondRush dr, Player player) {
        super();

        Game game = dr.getGame();
        head(player.getDisplayName());
        append(ChatColor.YELLOW);
        append(" (");
        append(game.getTeam(player).getDisplayName());
        append(ChatColor.YELLOW);
        append(")");
        nl();

        entryLn("Vies", player.getHealth() + " / 20");
        entryLn("Faim", player.getFoodLevel() + " / 20");

        Statistics stats = dr.getStatistics().get(player);
        merge(new StatisticsPartial(stats));
    }
}
