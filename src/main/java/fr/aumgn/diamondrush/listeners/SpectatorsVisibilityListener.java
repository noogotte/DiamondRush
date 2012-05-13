package fr.aumgn.diamondrush.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.event.game.DRGameStopEvent;
import fr.aumgn.diamondrush.event.players.DRPlayerJoinEvent;
import fr.aumgn.diamondrush.event.players.DRPlayerQuitEvent;
import fr.aumgn.diamondrush.event.players.DRSpectatorJoinEvent;
import fr.aumgn.diamondrush.event.players.DRSpectatorQuitEvent;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Spectators;
import fr.aumgn.diamondrush.game.Team;

public class SpectatorsVisibilityListener implements Listener {

    private final Game game;
    private final Spectators spectators;

    public SpectatorsVisibilityListener(DiamondRush dr) {
        this.game = dr.getGame();
        this.spectators = game.getSpectators();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (spectators.containsByName(player)) {
            spectators.update(player);
            for (Team team : game.getTeams()) {
                for (Player playerInGame : team.getPlayers()) {
                    playerInGame.hidePlayer(player);
                }
            }
        } else if (game.contains(player)) {
            for (Player spectator : spectators) {
                player.hidePlayer(spectator);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSpectatorJoin(DRSpectatorJoinEvent event) {
        Player spectator = event.getPlayer();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showPlayer(spectator);
        }

        for (Team team : game.getTeams()) {
            for (Player player : team.getPlayers()) {
                player.hidePlayer(spectator);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSpectatorQuit(DRSpectatorQuitEvent event) {
        Player spectator = event.getPlayer();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showPlayer(spectator);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRealPlayerJoinGame(DRPlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Player spectator : spectators) {
            player.hidePlayer(spectator);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuitGame(DRPlayerQuitEvent event) {
       Player player = event.getPlayer();
       for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
           player.showPlayer(onlinePlayer);
       }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGameStop(DRGameStopEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Player spectator : spectators) {
                player.showPlayer(spectator);
            }
        }
    }
}
