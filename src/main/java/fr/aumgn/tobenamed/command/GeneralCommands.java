package fr.aumgn.tobenamed.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import fr.aumgn.bukkit.command.Command;
import fr.aumgn.bukkit.command.CommandArgs;
import fr.aumgn.bukkit.command.CommandError;
import fr.aumgn.bukkit.command.Commands;
import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.stage.Stage;

public class GeneralCommands extends Commands {

    @Command(name = "stop-game", max = 0)
    public void stopGame(CommandSender sender, CommandArgs args) {
        Game game = TBN.getGame();
        TBN.forceStop();
        game.sendMessage(ChatColor.RED + "La partie a été arretée.");
    }

    @Command(name = "pause-game", max = 0)
    public void pauseGame(CommandSender sender, CommandArgs args) {
        Game game = TBN.getGame();
        if (game.isPaused()) {
            throw new CommandError("Le jeu est déjà en pause.");
        }
        game.pause();
    }

    @Command(name = "resume-game", max = 0)
    public void resumeGame(CommandSender sender, CommandArgs args) {
        final Game game = TBN.getGame();
        if (!game.isPaused()) {
            throw new CommandError("Le jeu n'est pas en pause.");
        }

        Stage stage = game.getStage();
        if (stage.hasNextStageScheduled()) {
            throw new CommandError(
                    "La partie est deja sur le point de redémarrer.");
        }

        game.sendMessage(ChatColor.GREEN + "La partie va reprendre");
        stage.scheduleNextStage(3, new Runnable() {
            public void run() {
                game.resume();
            }
        });
    }
}
