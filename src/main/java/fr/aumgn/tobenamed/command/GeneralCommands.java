package fr.aumgn.tobenamed.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import fr.aumgn.bukkit.command.Command;
import fr.aumgn.bukkit.command.CommandArgs;
import fr.aumgn.bukkit.command.Commands;
import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;

public class GeneralCommands extends Commands {

    @Command(name = "stop-game", max = 0)
    public void stopGame(CommandSender sender, CommandArgs args) {
        Game game = TBN.getGame();
        TBN.forceStop();
        game.sendMessage(ChatColor.RED + "La partie a été arreté.");
    }
}
