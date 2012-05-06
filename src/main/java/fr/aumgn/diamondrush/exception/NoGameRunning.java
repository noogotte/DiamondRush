package fr.aumgn.diamondrush.exception;

public class NoGameRunning extends DiamondRushException {

    private static final long serialVersionUID = -8107617954016679711L;

    public NoGameRunning() {
        super("Aucune partie en cours.");
    }
}
