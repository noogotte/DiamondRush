package fr.aumgn.diamondrush.command;

import fr.aumgn.bukkitutils.command.Commands;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.exception.NoGameRunning;

public abstract class DiamondRushCommands implements Commands {

    protected final DiamondRush dr;

    public DiamondRushCommands(DiamondRush dr) {
        this.dr = dr;
    }

    public void ensureIsRunning() {
        if (!dr.isRunning()) {
            throw new NoGameRunning();
        }
    }
}
