package fr.aumgn.diamondrush.exception;

public class PlayerNotInGame extends DiamondRushException {
    private static final long serialVersionUID = -3139364881212075182L;

    public PlayerNotInGame() {
        super("Ce joueur n'est pas dans la partie.");
    }
}
