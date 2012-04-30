package fr.aumgn.diamondrush.stage;

import org.bukkit.ChatColor;

import fr.aumgn.diamondrush.DiamondRush;

public class TransitionStage extends StaticStage {

    protected Stage nextStage;
    private int duration;

    public TransitionStage(DiamondRush dr, Stage nextStage, int duration) {
        super(dr);
        this.nextStage = nextStage;
        this.duration = duration;
    }

    @Override
    public void start() {
        super.start();
        dr.getGame().sendMessage(ChatColor.YELLOW + "C'est le moment de changer de canal.");
        scheduleNextStage(duration, nextStage);
    }
}
