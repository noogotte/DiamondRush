package fr.aumgn.tobenamed.stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.aumgn.bukkit.command.CommandError;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;

public class RandomJoinStage extends JoinStage {

    private List<Player> players;

    public RandomJoinStage(Game game) {
        super(game);
        players = new ArrayList<Player>();
    }

    @Override
    public boolean contains(Player player) {
        return players.contains(player);
    }

    @Override
    public void addPlayer(Player player, Team team) {
        if (team != null) {
           player.sendMessage(ChatColor.RED + "Les équipes sont constituées aléatoirement.");
        }

        player.sendMessage(ChatColor.YELLOW + "Joueur actuelles : ");
        for (Player playerInStage : players) {
            player.sendMessage(ChatColor.YELLOW + " - " + playerInStage.getDisplayName());
            playerInStage.sendMessage(player.getDisplayName() + ChatColor.YELLOW
                    + " a rejoint la partie.");
        }
        players.add(player);
    }

    @Override
    public void ensureIsReady() {
        if (game.getTeams().size() > players.size()) {
            throw new CommandError("Il n'y a pas assez de joueur");
        }
    }

    @Override
    public void prepare() {
        Collections.shuffle(players);
        for (Player player : players) {
            game.addPlayer(player, null);
        }
    }
}
