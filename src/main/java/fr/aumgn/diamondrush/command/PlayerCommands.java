package fr.aumgn.diamondrush.command;

import org.bukkit.entity.Player;

import fr.aumgn.bukkitutils.command.Command;
import fr.aumgn.bukkitutils.command.CommandArgs;
import fr.aumgn.bukkitutils.command.NestedCommands;
import fr.aumgn.bukkitutils.command.exception.CommandError;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;

@NestedCommands(name = "diamondrush")
public class PlayerCommands extends DiamondRushCommands {

    public PlayerCommands(DiamondRush dr) {
        super(dr);
    }

    @Command(name = "join", max = 1)
    public void joinTeam(Player player, CommandArgs args) {
        ensureIsRunning();

        Game game = dr.getGame();
        Team team;
        if (args.length() > 0) {
            team = game.getTeam(args.get(0));
        } else {
            team = game.getTeamWithMinimumPlayers();
        }

        dr.playerJoin(team, player);
    }

    @Command(name = "quit", max = 1)
    public void quitGame(Player player, CommandArgs args) {
        ensureIsRunning();

        Player quitting;
        if (args.length() == 0) {
            quitting = player;
        } else {
            if (!player.hasPermission("dr.cmd.quit.others")) {
                throw new CommandError("Vous n'avez pas la permission de faire ca.");
            }
            quitting = matchPlayer(args.get(0));
        }

        dr.playerQuit(quitting);
    }
}
