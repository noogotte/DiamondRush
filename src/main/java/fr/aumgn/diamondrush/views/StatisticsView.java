package fr.aumgn.diamondrush.views;

import fr.aumgn.diamondrush.game.Statistics;

public class StatisticsView extends MessagesView {

    public StatisticsView(Statistics stats) {
        this(stats, true, true);
    }

    public StatisticsView(Statistics stats, boolean showDeaths, boolean showBlocksBroken) {
        super();
        entryLn("Kills", stats.getKills());
        if (showDeaths) {
            entryLn("Morts", stats.getDeaths());
        }
        if (showBlocksBroken) {
            entryLn("Blocs minés", stats.getBlocksBroken());
        }
    }

}
