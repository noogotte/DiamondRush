package fr.aumgn.diamondrush.command;

import org.bukkit.entity.Player;

import fr.aumgn.bukkitutils.command.Command;
import fr.aumgn.bukkitutils.command.CommandArgs;
import fr.aumgn.bukkitutils.command.NestedCommands;
import fr.aumgn.bukkitutils.command.Commands;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.event.players.DRPlayerJoinEvent;
import fr.aumgn.diamondrush.event.players.DRPlayerQuitEvent;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;

@NestedCommands(name = "diamondrush")
public class PlayerCommands implements Commands {

    private final DiamondRush dr;

    public PlayerCommands(DiamondRush diamondRush) {
        this.dr = diamondRush;
    }

    @Command(name = "join", max = 1)
    public void joinTeam(Player player, CommandArgs args) {
        Game game = dr.getGame();

        Team team;
        if (args.length() > 0) {
            team = game.getTeam(args.get(0));
        } else {
            team = game.getTeamWithMinimumPlayers();
        }

        DRPlayerJoinEvent event = new DRPlayerJoinEvent(game, team, player);
        dr.handlePlayerJoinEvent(event);
    }

    @Command(name = "quit")
    public void quitGame(Player player, CommandArgs args) {
        Game game = dr.getGame();

        DRPlayerQuitEvent event = new DRPlayerQuitEvent(game, player);
        dr.handlePlayerQuitEvent(event);
    }
}
