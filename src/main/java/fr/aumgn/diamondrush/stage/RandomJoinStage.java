package fr.aumgn.diamondrush.stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.Util;
import fr.aumgn.diamondrush.event.game.DRGameStartEvent;
import fr.aumgn.diamondrush.event.players.DRPlayerJoinEvent;
import fr.aumgn.diamondrush.event.players.DRPlayerQuitEvent;
import fr.aumgn.diamondrush.event.players.DRSpectatorJoinEvent;
import fr.aumgn.diamondrush.exception.NotEnoughPlayers;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.views.TeamsView;

public class RandomJoinStage extends JoinStage implements Listener {

    private List<Player> players;
    private boolean starting;

    public RandomJoinStage(DiamondRush dr) {
        super(dr);
        players = new ArrayList<Player>();
        starting = false;
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.<Listener>singletonList(this);
    }

    @EventHandler(ignoreCancelled = true)
    public void addPlayer(DRPlayerJoinEvent event) {
        if (starting) {
            return;
        }

        Player player = event.getPlayer();
        if (players.contains(player)) {
            player.sendMessage("Vous êtes déjà dans la partie.");
        } else {
            Util.broadcast(player.getDisplayName() +
                    ChatColor.YELLOW + " a rejoint la partie.");
            if (players.size() > 0) {
                player.sendMessage(ChatColor.YELLOW + "Joueur actuels : ");
            }
            for (Player playerInStage : players) {
                player.sendMessage(ChatColor.YELLOW + " - " +
                        playerInStage.getDisplayName());
            }
            players.add(player);
        }

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void removePlayer(DRPlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (players.remove(player)) {
            String msg = player.getDisplayName() + ChatColor.YELLOW +
                    " a quitté la partie.";
            Util.broadcast(msg);
        } else {
            player.sendMessage("Vous n'êtes pas dans la partie.");
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSpectatorJoin(DRSpectatorJoinEvent event) {
        Player spectator = event.getPlayer();
        if (players.contains(spectator)) {
            spectator.sendMessage("Vous êtes déjà dans la partie.");
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onGameStart(DRGameStartEvent event) {
        if (dr.getGame().getTeams().size() > players.size()) {
            event.setCancelled(true);
            throw new NotEnoughPlayers();
        }

        starting = true;
        Collections.shuffle(players);
        for (Player player : players) {
            Team team = dr.getGame().getTeamWithMinimumPlayers();
            dr.playerJoin(team, player);
        }
        TeamsView view = new TeamsView(dr.getGame().getTeams());
        for (String message : view) {
            dr.getGame().sendMessage(message);
        }
    }
}
