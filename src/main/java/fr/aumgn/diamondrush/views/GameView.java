package fr.aumgn.diamondrush.views;

import java.util.List;

import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.GameStatistics;

public class GameView extends MessagesView {

    public GameView(DiamondRush dr, boolean showTeams, boolean showBlocksBroken) {
        super();

        Game game = dr.getGame();
        headLn("Statistiques de la partie");
        if (showTeams) {
            entryLn("Nombres d'équipes restantes", game.getTeams().size());
        }

        GameStatistics stats = dr.getStatistics();
        merge(new StatisticsView(stats, false, showBlocksBroken));

        List<String> motg = stats.getMenOfTheGame();
        if (motg.size() == 0) {
        } else if (motg.size() == 1) {
            entryLn("Homme de la partie", motg.get(0));
        } else {
            StringBuilder builder = new StringBuilder();
            for (String manOfTheGame : motg) {
                builder.append(manOfTheGame);
                builder.append(" ");
            }
            entryLn("Hommes de la partie", builder.toString());
        }
    }
}
