package fr.aumgn.diamondrush.exception;

import fr.aumgn.bukkitutils.command.exception.CommandError;

public class NotEnoughTeams extends CommandError {
    private static final long serialVersionUID = -1667774177005590103L;

    public NotEnoughTeams() {
        super("Il n'y a pas suffisamment d'équipes.");
    }
}
