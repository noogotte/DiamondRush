package fr.aumgn.diamondrush.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aumgn.bukkitutils.command.Command;
import fr.aumgn.bukkitutils.command.CommandArgs;
import fr.aumgn.bukkitutils.command.NestedCommands;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.exception.PlayerNotInGame;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.views.PlayerView;
import fr.aumgn.diamondrush.views.TeamsView;

@NestedCommands(name = "diamondrush")
public class InfoCommands extends DiamondRushCommands {

    public InfoCommands(DiamondRush dr) {
        super(dr);
    }

    @Command(name = "teams")
    public void showTeams(CommandSender sender, CommandArgs args) {
        ensureIsRunning();

        Game game = dr.getGame();
        TeamsView view = new TeamsView(game.getTeams());
        for (String message : view) {
            sender.sendMessage(message);
        }
    }

    @Command(name = "spectators")
    public void spectators(CommandSender sender, CommandArgs args) {
        ensureIsRunning();

        StringBuilder builder = new StringBuilder();
        for (Player spectator : dr.getGame().getSpectators()) {
            builder.append(spectator.getDisplayName());
            builder.append(" ");
        }

        sender.sendMessage("Spectateurs :");
        sender.sendMessage(builder.toString());
    }

    @Command(name = "stats", min = 1, max = 1)
    public void stats(Player player, CommandArgs args) {
        ensureIsRunning();

        Game game = dr.getGame();
        Player target = matchPlayer(args.get(0));
        if (!game.contains(player)) {
            throw new PlayerNotInGame();
        }

        PlayerView view = new PlayerView(game, target);
        for (String message : view) {
            player.sendMessage(message);
        }
    }
}
