package fr.aumgn.diamondrush.exception;

public class NoStatistics extends DiamondRushException {
    private static final long serialVersionUID = 6378934098876277942L;

    public NoStatistics() {
        super("Aucune statistique disponible.");
    }

    public NoStatistics(String target) {
        super("Aucune statistique disponible pour " + target + ".");
    }

    public static NoStatistics forPlayer(String name) {
        return new NoStatistics("le joueur " + name);
    }

    public static NoStatistics forTeam(String name) {
        return new NoStatistics("l'équipe " + name);
    }
}
