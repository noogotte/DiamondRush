package fr.aumgn.diamondrush.event.spectators;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.aumgn.diamondrush.event.DRGameEvent;
import fr.aumgn.diamondrush.event.DRSpectatorEvent;
import fr.aumgn.diamondrush.game.Game;

public class DRSpectatorQuitEvent extends Event implements DRGameEvent, DRSpectatorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Game game;
    private final Player spectator;

    public DRSpectatorQuitEvent(Game game, Player player) {
        super();
        this.game = game;
        this.spectator = player;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public Player getSpectator() {
        return spectator;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
