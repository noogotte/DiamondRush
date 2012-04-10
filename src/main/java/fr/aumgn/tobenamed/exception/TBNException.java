package fr.aumgn.tobenamed.exception;

import fr.aumgn.bukkit.command.exception.CommandException;

public abstract class TBNException extends RuntimeException implements CommandException {
    private static final long serialVersionUID = -1461178660942653654L;

    public TBNException(String message) {
        super(message);
    }
}
