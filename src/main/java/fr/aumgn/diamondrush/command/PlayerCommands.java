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

    @Command(name = "join", max = 1)
    public void joinTeam(Player player, CommandArgs args) {
        Game game = DiamondRush.getGame();

        Team team;
        if (args.length() > 0) {
            team = game.getTeam(args.get(0));
        } else {
            team = game.getTeamWithMinimumPlayers();
        }

        DRPlayerJoinEvent event = new DRPlayerJoinEvent(game, team, player);
        DiamondRush.getController().handlePlayerJoinEvent(event);
        /*if (stage instanceof JoinStage) {
            JoinStage joinStage = (JoinStage) stage;
            if (joinStage.contains(player)) {
                throw new CommandError("Vous êtes déjà dans la partie.");
            }

            ((JoinStage) stage).addPlayer(player, team);
        } else {
            if (game.contains(player)) {
                throw new CommandError("Vous êtes déjà dans la partie.");
            }

            game.addPlayer(player, team);
            team = game.getTeam(player);
            Vector pos;
            if (team.getTotem() != null) {
                pos = team.getTotem().getTeleportPoint();
            } else {
                pos = new Vector(team.getForeman().getLocation());
            } 
            player.teleport(pos.toLocation(game.getWorld()));
            game.sendMessage(player.getDisplayName() + ChatColor.YELLOW +
<<<<<<< HEAD
                    " a rejoint l'équipe " + team.getDisplayName() + ChatColor.YELLOW + ".");
        }
=======
                    " a rejoint l'équipe " + team.getDisplayName());
        }*/
    }

    @Command(name = "quit")
    public void quitGame(Player player, CommandArgs args) {
        Game game = DiamondRush.getGame();

        DRPlayerQuitEvent event = new DRPlayerQuitEvent(game, player);
        DiamondRush.getController().handlePlayerQuitEvent(event);
        /*if (stage instanceof JoinStage) {
            JoinStage joinStage = (JoinStage) stage;
            if (!joinStage.contains(player)) {
                throw new CommandError("Vous n'êtes pas dans la partie.");
            }

            joinStage.removePlayer(player);
        } else {
            if (!game.contains(player)) {
                throw new CommandError("Vous n'êtes pas dans la partie.");
            }

            game.removePlayer(player);
            player.sendMessage(ChatColor.GREEN + "Vous avez quitté la partie.");
        }*/
    }
}
