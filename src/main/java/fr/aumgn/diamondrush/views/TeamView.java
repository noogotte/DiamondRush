package fr.aumgn.diamondrush.views;

import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Statistics;
import fr.aumgn.diamondrush.game.Team;

public class TeamView extends MessagesView {

    public TeamView(DiamondRush dr, Team team) {
        super();

        headLn(team.getDisplayName());
        entryLn("Vies", team.getLives());
        Statistics stats = dr.getStatistics().get(team);
        merge(new StatisticsPartial(stats));
        entryLn("Surrenders", team.getSurrenders());
    }
}
