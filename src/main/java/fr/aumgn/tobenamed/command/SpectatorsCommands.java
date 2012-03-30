package fr.aumgn.tobenamed.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.aumgn.bukkit.command.Command;
import fr.aumgn.bukkit.command.CommandArgs;
import fr.aumgn.bukkit.command.CommandError;
import fr.aumgn.bukkit.command.Commands;
import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;

public class SpectatorsCommands extends Commands {

    @Command(name = "watch-game", max = 0)
    public void watchGame(Player player, CommandArgs args) {
        if (!TBN.isRunning()) {
            throw new CommandError("Aucune partie en cours.");
        }

        Game game = TBN.getStage().getGame();
        if (game.contains(player)) {
            throw new CommandError("Vous etes deja dans la partie.");
        }

        game.addSpectator(player);
        player.sendMessage(ChatColor.GREEN + "Vous etes maintenant spectateur.");
    }
}
