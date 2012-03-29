package fr.aumgn.bukkit.command;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Commands {

    public static void register(Commands commands) {
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

            org.bukkit.command.PluginCommand command = Bukkit.getPluginCommand(cmdAnnotation.name());
            if (command != null) {
                command.setUsage(ChatColor.GREEN + "Utilisation: " + ChatColor.YELLOW + command.getUsage());
                command.setPermissionMessage(ChatColor.RED + "Vous n'avez pas la permission d'executer cette commande.");
                CommandExecutor executor = new MethodCommandExecutor(commands,
                    method, cmdAnnotation, Player.class.isAssignableFrom(params[0]));
                command.setExecutor(executor);
            }
        }
    }
}
