package fr.aumgn.bukkit.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MethodCommandExecutor implements CommandExecutor {

    private final Commands instance;
    private final Method method;
    private final int min;
    private final int max;
    private final boolean isPlayerCommand;

    public MethodCommandExecutor(Commands instance, Method method, Command command, boolean isPlayerCommand) {
        this.instance = instance;
        this.method = method;
        this.min = command.min();
        this.max = command.max();
        this.isPlayerCommand = isPlayerCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String lbl, String[] args) {
        try {
            ensureHasValidSender(sender);
            ensureHasValidArgsCount(args);
            callCommand(sender, args);
        } catch (CommandUsageError exc) {
            return false;
        } catch (CommandError exc) {
            sender.sendMessage(ChatColor.RED + exc.getMessage());
        }
        return true;
    }

    private void ensureHasValidSender(CommandSender sender) {
        if (isPlayerCommand && !(sender instanceof Player)) {
            throw new CommandError("Cette commande n'est utilisable qu'en tant que joueur.");
        }
    }

    private void ensureHasValidArgsCount(String[] args) {
        if (args.length < min || (max != -1 && args.length > max)) {
            throw new CommandUsageError();
        }
    }

    private void callCommand(CommandSender sender, String[] args) {
        try {
            method.invoke(instance, sender, args);
        } catch (IllegalArgumentException exc) {
            unexpectedError(exc);
        } catch (IllegalAccessException exc) {
            unexpectedError(exc);
        } catch (InvocationTargetException exc) {
            unexpectedError(exc);
        }
    }

    private void unexpectedError(Exception exc) {
        throw new CommandError("Erreur inattendu lors de l'execution de la commande."
                + exc.getClass().getSimpleName());
    }
}
