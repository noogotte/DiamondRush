package fr.aumgn.diamondrush.event.players;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.aumgn.diamondrush.event.DRGameEvent;
import fr.aumgn.diamondrush.event.DRPlayerEvent;
import fr.aumgn.diamondrush.game.Game;

public class DRSpectatorJoinEvent extends Event
        implements DRGameEvent, DRPlayerEvent, Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Game game;
    private final Player spectator;
    private boolean cancelled;

    public DRSpectatorJoinEvent(Game game, Player player) {
        super();
        this.game = game;
        this.spectator = player;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public Player getPlayer() {
        return spectator;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
