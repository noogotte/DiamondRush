package fr.aumgn.bukkit.command.exception;

public class CommandUnhandledException extends RuntimeException {
    private static final long serialVersionUID = 1760382462081928711L;

    public CommandUnhandledException(Throwable throwable) {
        super(throwable);
    }
}
