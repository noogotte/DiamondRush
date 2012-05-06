package fr.aumgn.diamondrush.exception;

import fr.aumgn.bukkitutils.command.exception.CommandException;

public class DiamondRushException extends RuntimeException implements CommandException {
    private static final long serialVersionUID = -1793945331004595378L;

    public DiamondRushException(String message) {
        super(message);
    }
}
