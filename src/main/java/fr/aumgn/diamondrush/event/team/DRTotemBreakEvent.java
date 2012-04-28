package fr.aumgn.diamondrush.event.team;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.aumgn.diamondrush.event.DRGameEvent;
import fr.aumgn.diamondrush.event.DRPlayerEvent;
import fr.aumgn.diamondrush.event.DRRegionEvent;
import fr.aumgn.diamondrush.event.DRTeamEvent;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.region.Totem;

public class DRTotemBreakEvent extends Event
        implements DRGameEvent, DRTeamEvent, DRRegionEvent, DRPlayerEvent, Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Game game;
    private final Team team;
    private final Totem totem;
    private final Player player;
    private boolean cancelled;

    public DRTotemBreakEvent(Game game, Team team, Totem totem, Player player) {
        this.game = game;
        this.team = team;
        this.totem = null;
        this.player = player;
        this.cancelled = false;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public Team getTeam() {
        return team;
    }

    @Override
    public Totem getRegion() {
        return totem;
    }

    @Override
    public Player getPlayer() {
        return player;
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
