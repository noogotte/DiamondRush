package fr.aumgn.diamondrush.views;

import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Statistics;

public class StatisticsView extends MessagesView {

    public static StatisticsView forGame(DiamondRush dr) {
        Statistics stats = dr.getStatistics();
        return new StatisticsView("Partie", stats);
    }

    public static StatisticsView forTeam(DiamondRush dr, String name) {
        Statistics stats = dr.getStatistics().getTeam(name);
        return new StatisticsView(name, stats);
    }

    public static StatisticsView forPlayer(DiamondRush dr, String name) {
        Statistics stats = dr.getStatistics().getPlayer(name);
        return new StatisticsView(name, stats);
    }

    public StatisticsView(String name, Statistics stats) {
       headLn(name);
       merge(new StatisticsPartial(stats));
    }

}
