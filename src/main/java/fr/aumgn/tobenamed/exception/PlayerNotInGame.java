package fr.aumgn.tobenamed.exception;

import fr.aumgn.bukkit.command.CommandError;

public class PlayerNotInGame extends CommandError {
    private static final long serialVersionUID = -3139364881212075182L;

    public PlayerNotInGame() {
        super("Ce joueur n'est pas dans la partie.");
    }
}
