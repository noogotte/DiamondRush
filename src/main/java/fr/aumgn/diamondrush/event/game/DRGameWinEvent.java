package fr.aumgn.diamondrush.event.game;

import fr.aumgn.diamondrush.event.DRTeamEvent;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;

public class DRGameWinEvent extends DRGameStopEvent
        implements DRTeamEvent {

    private final Team team;

    public DRGameWinEvent(Game game, Team team) {
        super(game);
        this.team = team;
    }

    @Override
    public Team getTeam() {
        return team;
    }
}
