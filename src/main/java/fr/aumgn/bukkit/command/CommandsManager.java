package fr.aumgn.bukkit.command;

import java.lang.reflect.Method;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandsManager {

    private JavaPlugin plugin;

    public CommandsManager(JavaPlugin plugin) {
        this.plugin = plugin; 
    }

    public void register(Commands commands) {
        for (Method method : commands.getClass().getMethods()) {
            Command cmdAnnotation = method.getAnnotation(Command.class);
            if (cmdAnnotation == null) {
                continue;
            }
            Class<?>[] params = method.getParameterTypes();
            if (params.length < 2) {
                continue;
            }
            if (!CommandSender.class.isAssignableFrom(params[0])) {
                continue;
            }
            if (!CommandArgs.class.isAssignableFrom(params[1])) {
                continue;
            }

            PluginCommand command = plugin.getCommand(cmdAnnotation.name());
            if (command != null) {
                command.setUsage(ChatColor.GREEN + "Utilisation : " + ChatColor.YELLOW + command.getUsage());
                command.setPermissionMessage(ChatColor.RED + "Vous n'avez pas la permission d'exécuter cette commande.");
                CommandExecutor executor = new MethodCommandExecutor(commands,
                    method, cmdAnnotation, Player.class.isAssignableFrom(params[0]));
                command.setExecutor(executor);
            }
        }
    }
}
