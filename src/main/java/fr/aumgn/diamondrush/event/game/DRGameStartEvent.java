package fr.aumgn.diamondrush.event.game;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.aumgn.diamondrush.event.DRGameEvent;
import fr.aumgn.diamondrush.game.Game;

public class DRGameStartEvent extends Event
        implements DRGameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Game game;

    public DRGameStartEvent(Game game) {
        super();
        this.game = game;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
