package fr.aumgn.diamondrush.event.team;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.aumgn.diamondrush.event.DRGameEvent;
import fr.aumgn.diamondrush.event.DRRegionEvent;
import fr.aumgn.diamondrush.event.DRTeamEvent;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.region.Totem;

public class DRTotemSetEvent extends Event
        implements DRGameEvent, DRTeamEvent, DRRegionEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Game game;
    private final Team team;
    private final Totem totem;

    public DRTotemSetEvent(Game game, Team team, Totem totem) {
        this.game = game;
        this.team = team;
        this.totem = totem;
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
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
