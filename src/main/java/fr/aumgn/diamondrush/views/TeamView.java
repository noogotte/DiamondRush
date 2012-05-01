package fr.aumgn.diamondrush.views;

import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Statistics;
import fr.aumgn.diamondrush.game.Team;

public class TeamView extends MessagesView {

    public TeamView(Game game, Team team) {
        super();

        headLn(team.getDisplayName());
        entryLn("Vies", team.getLives());
        Statistics stats = game.getStatistics().get(team);
        merge(new StatisticsView(stats));
        entryLn("Surrenders", team.getSurrenders());
    }
}
