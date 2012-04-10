package fr.aumgn.tobenamed.command;

import org.bukkit.command.CommandSender;

import fr.aumgn.bukkit.command.Command;
import fr.aumgn.bukkit.command.CommandArgs;
import fr.aumgn.bukkit.command.Commands;
import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.TeamsView;

public class InfoCommands implements Commands {

    @Command(name = "show-teams", max = 0)
    public void showTeams(CommandSender sender, CommandArgs args) {
        Game game = TBN.getGame();
        TeamsView view = new TeamsView(game.getTeams());
        for (String message : view) {
            sender.sendMessage(message);
        }
    }
}
