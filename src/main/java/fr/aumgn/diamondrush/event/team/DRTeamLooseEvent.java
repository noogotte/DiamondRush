package fr.aumgn.diamondrush.event.team;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.aumgn.diamondrush.event.DRGameEvent;
import fr.aumgn.diamondrush.event.DRTeamEvent;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;

public class DRTeamLooseEvent extends Event
        implements DRGameEvent, DRTeamEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Game game;
    private final Team team;
    private final Team responsible;

    public DRTeamLooseEvent(Game game, Team team, Team responsible) {
        this.game = game;
        this.team = team;
        this.responsible = responsible;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public Team getTeam() {
        return team;
    }

    public Team getResponsible() {
        return responsible;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
