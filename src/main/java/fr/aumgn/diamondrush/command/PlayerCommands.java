package fr.aumgn.diamondrush.command;

import org.bukkit.entity.Player;

import fr.aumgn.bukkitutils.command.Command;
import fr.aumgn.bukkitutils.command.CommandArgs;
import fr.aumgn.bukkitutils.command.NestedCommands;
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

    @Command(name = "quit")
    public void quitGame(Player player, CommandArgs args) {
        ensureIsRunning();
        dr.playerQuit(player);
    }
}
