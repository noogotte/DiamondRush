package fr.aumgn.diamondrush.event.team;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.aumgn.diamondrush.event.DRGameEvent;
import fr.aumgn.diamondrush.event.DRRegionEvent;
import fr.aumgn.diamondrush.event.DRTeamEvent;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.region.TeamSpawn;

public class DRTeamSpawnSetEvent extends Event
        implements DRGameEvent, DRTeamEvent, DRRegionEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Game game;
    private final Team team;
    private final TeamSpawn spawn;

    public DRTeamSpawnSetEvent(Game game, Team team, TeamSpawn spawn) {
        this.game = game;
        this.team = team;
        this.spawn = spawn;
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
    public TeamSpawn getRegion() {
        return spawn;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
