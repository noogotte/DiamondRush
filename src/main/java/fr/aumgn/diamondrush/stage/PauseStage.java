package fr.aumgn.diamondrush.stage;

import org.bukkit.ChatColor;

import fr.aumgn.diamondrush.game.Game;

public class PauseStage extends StaticStage {

    private Stage oldStage;

    public PauseStage(Game game, Stage oldStage){
        super(game);
        this.oldStage = oldStage;
    }

    @Override
    public void start() {
        super.start();
        game.sendMessage(ChatColor.RED +
                "TBN est en pause, vous ne pouvez rien faire !");
    }

    @Override
    public void stop() {
        super.stop();
        game.sendMessage(ChatColor.GREEN + "La partie reprend !");
    }

    public Stage getOldStage() {
        return oldStage;
    }
}
