package fr.aumgn.tobenamed.exception;

public class NoGameRunning extends TBNException {

    private static final long serialVersionUID = -8107617954016679711L;

    public NoGameRunning() {
        super("Aucune partie en cours.");
    }
}
