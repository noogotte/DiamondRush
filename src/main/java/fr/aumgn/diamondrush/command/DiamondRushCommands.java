package fr.aumgn.diamondrush.command;

import java.util.List;

import org.bukkit.entity.Player;

import fr.aumgn.bukkitutils.command.Commands;
import fr.aumgn.bukkitutils.command.exception.CommandError;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.Util;
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

    public Player matchPlayer(String name) {
        List<Player> players = Util.matchPlayer(name);
        if (players.size() > 1) {
            throw new CommandError("Plus d'un joueur trouvés avec le motif " + name + ".");
        } else if (players.size() < 1) {
            throw new CommandError("Aucun joueur trouvé.");
        }

        return players.get(0);
    }


}
