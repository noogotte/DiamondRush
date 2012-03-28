package fr.aumgn.tobenamed.stage;

import java.util.List;

import org.bukkit.event.Listener;

public abstract class Stage {

    public abstract List<Listener> getListeners();

    public void start() {
    }

    public void stop() {
    }
}
