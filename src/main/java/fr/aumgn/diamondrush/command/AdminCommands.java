package fr.aumgn.diamondrush.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import fr.aumgn.bukkitutils.command.Command;
import fr.aumgn.bukkitutils.command.CommandArgs;
import fr.aumgn.bukkitutils.command.Commands;
import fr.aumgn.bukkitutils.command.NestedCommands;
import fr.aumgn.diamondrush.DiamondRush;

@NestedCommands(name = "diamondrush")
public class AdminCommands implements Commands {

    @Command(name = "reload")
    public void reload(CommandSender sender, CommandArgs args) {
        DiamondRush.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration rechargée");
    }
}
