package fr.aumgn.diamondrush.command;

import org.bukkit.command.CommandSender;

import fr.aumgn.bukkitutils.command.Command;
import fr.aumgn.bukkitutils.command.CommandArgs;
import fr.aumgn.bukkitutils.command.Commands;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.TeamsView;

public class InfoCommands implements Commands {

    @Command(name = "show-teams", max = 0)
    public void showTeams(CommandSender sender, CommandArgs args) {
        Game game = DiamondRush.getGame();
        TeamsView view = new TeamsView(game.getTeams());
        for (String message : view) {
            sender.sendMessage(message);
        }
    }
}
