package fr.aumgn.tobenamed.stage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.aumgn.bukkit.command.CommandError;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;

public class SimpleJoinStage extends JoinStage {

    public SimpleJoinStage(Game game) {
        super(game);
    }

    @Override
    public boolean contains(Player player) {
        return game.contains(player);
    }

    @Override
    public void addPlayer(Player player, Team team) {
        game.addPlayer(player, team);
        team = game.getTeam(player);
        game.sendMessage(player.getDisplayName() + ChatColor.YELLOW +
                " a rejoint l'équipe " + team.getDisplayName());
    }

    @Override
    public void removePlayer(Player player) {
        game.removePlayer(player);
        game.sendMessage(player.getDisplayName() + ChatColor.YELLOW +
                " a quitté la partie.");
    }

    @Override
    public void ensureIsReady() {
        for (Team team : game.getTeams()) {
            if (team.size() < 1) {
                throw new CommandError("L'équipe " + 
                        team.getDisplayName() + " n'a aucun joueur.");
            }
        }
    }
}
