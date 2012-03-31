package fr.aumgn.tobenamed.stage;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import static fr.aumgn.tobenamed.util.TBNUtil.*;
import fr.aumgn.tobenamed.util.Vector;

public class TotemStage extends Stage {

    private Game game;

    public TotemStage(Game game) {
        this.game = game;
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.<Listener>emptyList();
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void start() {
        List<Team> teams = game.getTeams();
        Iterator<Vector> directions = game.getSpawn().
                getDirections(teams.size()).iterator();
        for (Team team : teams) {
            Player foreman = pickRandom(team.getPlayers());
            team.setForeman(foreman);
            tpTo(foreman, foreman.getWorld(), directions.next());
        }
    }
}
