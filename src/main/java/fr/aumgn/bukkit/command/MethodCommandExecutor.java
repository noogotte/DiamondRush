package fr.aumgn.bukkit.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aumgn.bukkit.command.exception.CommandError;
import fr.aumgn.bukkit.command.exception.CommandUnhandledException;
import fr.aumgn.bukkit.command.exception.CommandUsageError;

public class MethodCommandExecutor implements CommandExecutor {

    private final Commands instance;
    private final Method method;
    private final int min;
    private final int max;
    private final Set<Character> flags;
    private final boolean isPlayerCommand;

    public MethodCommandExecutor(Commands instance, Method method, Command command, boolean isPlayerCommand) {
        this.instance = instance;
        this.method = method;

        this.min = command.min();
        this.max = command.max();
        this.flags = new HashSet<Character>();
        for (char flag : command.flags().toCharArray()) {
            this.flags.add(flag);
        }
        this.isPlayerCommand = isPlayerCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String lbl, String[] rawArgs) {
        try {
            ensureHasValidSender(sender);
            CommandArgs args = new CommandArgs(rawArgs, flags, min, max);
            callCommand(lbl, sender, args);
        } catch (CommandUsageError exc) {
            sender.sendMessage(ChatColor.RED + exc.getMessage());
            return false;
        } catch (CommandException exc) {
            sender.sendMessage(ChatColor.RED + exc.getMessage());
        }
        return true;
    }

    private void ensureHasValidSender(CommandSender sender) {
        if (isPlayerCommand && !(sender instanceof Player)) {
            throw new CommandError("Cette commande n'est utilisable qu'en tant que joueur.");
        }
    }

    private void callCommand(String name, CommandSender sender, CommandArgs args) {
        try {
            method.invoke(instance, sender, args);
        } catch (IllegalArgumentException exc) {
            unexpectedError(exc);
        } catch (IllegalAccessException exc) {
            unexpectedError(exc);
        } catch (InvocationTargetException exc) {
            if (exc.getCause() instanceof CommandError) {
                throw (CommandError) exc.getCause();
            }
            if (exc.getCause() instanceof CommandUsageError) {
                throw (CommandUsageError) exc.getCause();
            }
            throw new CommandUnhandledException(exc.getCause());
        }
    }

    private void unexpectedError(Exception exc) {
        throw new CommandError(
                "Erreur inattendue lors de l'exécution de la commande. "
                + exc.getClass().getSimpleName() + ": " + exc.getMessage());
    }
}
