package fr.aumgn.diamondrush.stage;

import org.bukkit.ChatColor;

import fr.aumgn.diamondrush.DiamondRush;

public class PauseStage extends StaticStage {

    private Stage oldStage;

    public PauseStage(DiamondRush dr, Stage oldStage){
        super(dr);
        this.oldStage = oldStage;
    }

    @Override
    public void start() {
        super.start();
        dr.getGame().sendMessage(ChatColor.RED +
                "Diamond Rush est en pause, vous ne pouvez rien faire !");
    }

    @Override
    public void stop() {
        super.stop();
        dr.getGame().sendMessage(ChatColor.GREEN + "La partie reprend !");
    }

    public Stage getOldStage() {
        return oldStage;
    }
}
