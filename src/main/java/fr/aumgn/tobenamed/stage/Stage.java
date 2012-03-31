package fr.aumgn.tobenamed.stage;

import java.util.List;

import org.bukkit.event.Listener;

import fr.aumgn.tobenamed.game.Game;

public abstract class Stage {

    public abstract List<Listener> getListeners();

    public abstract Game getGame();

    public void start() {
    }

    public void stop() {
    }
}
