package fr.aumgn.tobenamed.stage;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.stage.listeners.FightListener;

public class FightStage extends Stage {

    private Game game;
    private FightListener listener;

    public FightStage(Game game) {
        this.game = game;
        this.listener = new FightListener(this);
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.<Listener>singletonList(listener);
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void start() {
        game.sendMessage(ChatColor.GREEN + "La phase de combat commence.");
        scheduleNextStage(300, new Runnable() {
            public void run() {
                game.nextStage(new DevelopmentStage(game));
            }
        });
    }
}
