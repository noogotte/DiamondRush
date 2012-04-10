package fr.aumgn.tobenamed.exception;

public class NotEnoughTeams extends TBNException {
    private static final long serialVersionUID = -1667774177005590103L;

    public NotEnoughTeams() {
        super("Il n'y a pas suffisamment d'équipes.");
    }
}
