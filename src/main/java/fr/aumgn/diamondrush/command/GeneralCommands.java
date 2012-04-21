package fr.aumgn.diamondrush.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import fr.aumgn.bukkitutils.command.Command;
import fr.aumgn.bukkitutils.command.CommandArgs;
import fr.aumgn.bukkitutils.command.NestedCommands;
import fr.aumgn.bukkitutils.command.exception.CommandError;
import fr.aumgn.bukkitutils.command.Commands;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.stage.Stage;

@NestedCommands(name = "diamondrush")
public class GeneralCommands implements Commands {

    @Command(name = "stop", max = 0)
    public void stopGame(CommandSender sender, CommandArgs args) {
        Game game = DiamondRush.getGame();
        DiamondRush.forceStop();
        game.sendMessage(ChatColor.RED + "La partie a été arretée.");
    }

    @Command(name = "pause", max = 0)
    public void pauseGame(CommandSender sender, CommandArgs args) {
        Game game = DiamondRush.getGame();
        if (game.isPaused()) {
            throw new CommandError("Le jeu est déjà en pause.");
        }
        game.pause();
    }

    @Command(name = "resume", max = 0)
    public void resumeGame(CommandSender sender, CommandArgs args) {
        final Game game = DiamondRush.getGame();
        if (!game.isPaused()) {
            throw new CommandError("Le jeu n'est pas en pause.");
        }

        Stage stage = game.getStage();
        if (stage.hasNextStageScheduled()) {
            throw new CommandError(
                    "La partie est déjà sur le point de redémarrer.");
        }

        game.sendMessage(ChatColor.GREEN + "La partie va reprendre");
        stage.schedule(3, new Runnable() {
            public void run() {
                game.resume();
            }
        });
    }
}
