package fr.aumgn.diamondrush.stage;

import org.bukkit.ChatColor;

import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Game;

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
        scheduleNextStage(DiamondRush.getConfig().getTransitionDuration(), nextStage);
    }
}
