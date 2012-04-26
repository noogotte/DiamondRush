package fr.aumgn.diamondrush.exception;

import fr.aumgn.bukkitutils.command.exception.CommandError;

public class NotEnoughPlayers extends CommandError{
    private static final long serialVersionUID = -4851504257504536826L;

    public NotEnoughPlayers() {
        this("Il n'y a pas assez de joueur.");
    }

    public NotEnoughPlayers(String message) {
        super(message);
    }
}
