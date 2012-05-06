package fr.aumgn.diamondrush.exception;

public class NoSuchTeam extends DiamondRushException {
    private static final long serialVersionUID = -359072155198800087L;

    public NoSuchTeam(String name) {
        super("Aucune équipe appelée " + name);
    }

}
