package fr.aumgn.diamondrush.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import fr.aumgn.bukkitutils.command.Command;
import fr.aumgn.bukkitutils.command.CommandArgs;
import fr.aumgn.bukkitutils.command.Commands;
import fr.aumgn.bukkitutils.command.NestedCommands;
import fr.aumgn.diamondrush.DiamondRushPlugin;

@NestedCommands(name = "diamondrush")
public class AdminCommands implements Commands {

    private final DiamondRushPlugin plugin;

    public AdminCommands(DiamondRushPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(name = "reload")
    public void reload(CommandSender sender, CommandArgs args) {
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration rechargée");
    }
}
