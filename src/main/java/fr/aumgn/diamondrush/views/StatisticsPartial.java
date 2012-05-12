package fr.aumgn.diamondrush.views;

import fr.aumgn.diamondrush.game.Statistics;

public class StatisticsPartial extends MessagesView {

    public StatisticsPartial(Statistics stats) {
        this(stats, true, true);
    }

    public StatisticsPartial(Statistics stats, boolean showDeaths, boolean showBlocksBroken) {
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
