package fr.aumgn.diamondrush.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aumgn.bukkitutils.command.Command;
import fr.aumgn.bukkitutils.command.CommandArgs;
import fr.aumgn.bukkitutils.command.NestedCommands;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.TeamsView;

@NestedCommands(name = "diamondrush")
public class InfoCommands extends DiamondRushCommands {

    public InfoCommands(DiamondRush dr) {
        super(dr);
    }

    @Command(name = "teams")
    public void showTeams(CommandSender sender, CommandArgs args) {
        Game game = dr.getGame();
        TeamsView view = new TeamsView(game.getTeams());
        for (String message : view) {
            sender.sendMessage(message);
        }
    }

    @Command(name = "spectators")
    public void spectators(CommandSender sender, CommandArgs args) {
        StringBuilder builder = new StringBuilder();
        for (Player spectator : dr.getGame().getSpectators()) {
            builder.append(spectator.getDisplayName());
            builder.append(" ");
        }

        sender.sendMessage("Spectateurs :");
        sender.sendMessage(builder.toString());
    }
}
