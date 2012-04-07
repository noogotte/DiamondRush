package fr.aumgn.tobenamed.stage;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.stage.listeners.PauseListener;

public class PauseStage extends Stage{

    private Game game;
    private Stage oldStage;
    private PauseListener listener;
    private long time;

    public PauseStage(Game game, Stage oldStage){
        this.game = game;
        this.oldStage = oldStage;
        this.listener = new PauseListener(game);
        this.time = game.getWorld().getTime();
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
        game.sendMessage(ChatColor.RED +
                "TBN est en pause, vous ne pouvez rien faire !");
    }

    @Override
    public void stop() {
        game.getWorld().setTime(time);
    }

    public Stage getOldStage() {
        return oldStage;
    }
}
