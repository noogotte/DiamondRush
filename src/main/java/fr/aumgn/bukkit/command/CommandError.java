package fr.aumgn.bukkit.command;

public class CommandError extends RuntimeException {
    private static final long serialVersionUID = -7087606268182929669L;

    public CommandError(String message) {
        super(message);
    }
}
