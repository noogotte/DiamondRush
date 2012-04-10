package fr.aumgn.tobenamed.exception;

public class PlayerNotInGame extends TBNException {
    private static final long serialVersionUID = -3139364881212075182L;

    public PlayerNotInGame() {
        super("Ce joueur n'est pas dans la partie.");
    }
}
