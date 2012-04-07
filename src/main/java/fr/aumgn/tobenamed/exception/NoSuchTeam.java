package fr.aumgn.tobenamed.exception;

import fr.aumgn.bukkit.command.CommandError;

public class NoSuchTeam extends CommandError {
    private static final long serialVersionUID = -359072155198800087L;

    public NoSuchTeam(String name) {
        super("Aucune équipe appelée " + name);
    }

}
