package fr.aumgn.diamondrush.stage;

import org.bukkit.ChatColor;

import fr.aumgn.diamondrush.game.Game;

public class TransitionStage extends StaticStage {

    private Stage nextStage;
    private int duration;

    public TransitionStage(Game game, Stage nextStage, int duration) {
        super(game);
        this.nextStage = nextStage;
        this.duration = duration;
    }

    @Override
    public void start() {
        super.start();
        game.sendMessage(ChatColor.YELLOW + "C'est le moment de changer de canal.");
        scheduleNextStage(duration, nextStage);
    }
}
