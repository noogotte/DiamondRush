package fr.aumgn.diamondrush.event.game;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.aumgn.diamondrush.event.DRGameEvent;
import fr.aumgn.diamondrush.game.Game;

public class DRGameStartEvent extends Event
        implements DRGameEvent, Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Game game;
    private boolean cancelled;

    public DRGameStartEvent(Game game) {
        super();
        this.game = game;
        this.cancelled = false;
    }

    @Override
    public Game getGame() {
        return game;
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
