package fr.aumgn.tobenamed.stage;

import org.bukkit.ChatColor;

import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;

public class TransitionStage extends StaticStage {

    private Stage nextStage;

    public TransitionStage(Game game, Stage nextStage) {
        super(game);
        this.nextStage = nextStage;
    }

    @Override
    public void start() {
        super.start();
        game.sendMessage(ChatColor.YELLOW + "C'est le moment de changer de channel.");
        scheduleNextStage(TBN.getConfig().getTransitionDuration(), nextStage);
    }
}
