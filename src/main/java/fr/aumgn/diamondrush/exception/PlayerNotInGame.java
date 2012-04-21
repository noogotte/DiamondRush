package fr.aumgn.diamondrush.exception;

import fr.aumgn.bukkitutils.command.exception.CommandError;

public class PlayerNotInGame extends CommandError {
    private static final long serialVersionUID = -3139364881212075182L;

    public PlayerNotInGame() {
        super("Ce joueur n'est pas dans la partie.");
    }
}
