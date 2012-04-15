package fr.aumgn.diamondrush.exception;

import fr.aumgn.bukkit.command.CommandError;

public class NoGameRunning extends CommandError {

    private static final long serialVersionUID = -8107617954016679711L;

    public NoGameRunning() {
        super("Aucune partie en cours.");
    }
}
