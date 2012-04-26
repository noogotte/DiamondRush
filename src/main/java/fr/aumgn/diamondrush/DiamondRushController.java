package fr.aumgn.diamondrush;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.aumgn.bukkitutils.command.exception.CommandError;
import fr.aumgn.diamondrush.event.game.DRGameStartEvent;
import fr.aumgn.diamondrush.event.game.DRGameWinEvent;
import fr.aumgn.diamondrush.event.players.DRPlayerJoinEvent;
import fr.aumgn.diamondrush.event.players.DRPlayerQuitEvent;
import fr.aumgn.diamondrush.event.team.DRTeamLooseEvent;
import fr.aumgn.diamondrush.event.team.DRTotemMinedEvent;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.region.Totem;
import fr.aumgn.diamondrush.stage.JoinStage;
import fr.aumgn.diamondrush.stage.Stage;
import fr.aumgn.diamondrush.stage.StartStage;
import fr.aumgn.diamondrush.stage.TotemStage;

public class DiamondRushController {

    private Game game;
    private Stage stage;

    public DiamondRushController(Game game) {
        this.game = game;
        this.stage = game.getStage();
    }

    public void handlePlayerJoinEvent(DRPlayerJoinEvent event) {
        Util.callEvent(event);
        if (!event.isCancelled()) {
            Player player = event.getPlayer();
            if (game.contains(player)) {
                player.sendMessage(ChatColor.RED + 
                        "Vous êtes déjà dans la partie.");
            } else {
                game.addPlayer(player, event.getTeam());
                Util.broadcast(player.getDisplayName() + ChatColor.YELLOW +
                        " a rejoint l'équipe " + event.getTeam().getDisplayName());
            }
        }
    }

    public void handlePlayerQuitEvent(DRPlayerQuitEvent event) {
        Util.callEvent(event);
        if (!event.isCancelled()) {
            Player player = event.getPlayer();
            if (game.contains(player)) {
                game.removePlayer(player);
                Util.broadcast(player.getDisplayName() + ChatColor.YELLOW +
                        " a quitté la partie.");
            } else {
                player.sendMessage(ChatColor.RED + 
                        "Vous n'êtes pas dans la partie.");
            }
        }
    }

    public void handleGameStartEvent(DRGameStartEvent event) {
        if (!(stage instanceof JoinStage)) {
            throw new CommandError("Cette commande ne peut être utilisée que durant la phase de join.");
        }

        if (stage.hasNextStageScheduled()) {
            throw new CommandError(
                    "La partie est déjà sur le point de démarrer.");
        }

        Util.callEvent(event);
        if (!event.isCancelled()) {
            game.sendMessage(ChatColor.GREEN + "La partie va commencer.");
            game.nextStage(new StartStage(game, new TotemStage(game)));
        }
    }

    public void handleTotemMinedEvent(DRTotemMinedEvent event) {
        Util.callEvent(event);
        if (!event.isCancelled()) {
            Team team = event.getTeam();
            team.decreaseLives();
            if (team.getLives() == 0) {
                Team responsible = game.getTeam(event.getPlayer());
                Totem totem = event.getRegion();
                DRTeamLooseEvent teamLooseEvent = 
                        new DRTeamLooseEvent(game, team, responsible, totem);
                handleTeamLooseEvent(teamLooseEvent);
            } else {
                game.sendMessage(ChatColor.YELLOW + "L'équipe " + team.getDisplayName() + 
                        ChatColor.YELLOW + " a perdu une vie. " + team.getLives() + " restantes.");
            }
        }
    }

    public void handleTeamLooseEvent(DRTeamLooseEvent event) {
        Util.callEvent(event);
        Team team = event.getTeam();
        game.removeTeam(team);
        if (game.getTeams().size() == 1) {
            DRGameWinEvent winEvent = new DRGameWinEvent(game, team);
            handleGameWinEvent(winEvent);
        } else {
            String msg = ChatColor.RED +"L'équipe " + team.getDisplayName() 
                    + ChatColor.RED + " a perdu la partie.";
            game.sendMessage(msg);
            team.sendMessage(msg);
        }
    }

    public void handleGameWinEvent(DRGameWinEvent event) {
        Util.callEvent(event);
        Team winningTeam = game.getTeams().get(0);
        String msg = ChatColor.GREEN +
                "L'équipe " + winningTeam.getDisplayName() +
                ChatColor.GREEN + " a gagné la partie.";
        game.sendMessage(msg);
        DiamondRush.forceStop();
    }
}
