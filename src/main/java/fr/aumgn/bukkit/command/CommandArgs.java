package fr.aumgn.bukkit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandArgs {

    private Set<Character> flags;
    private String[] args;

    public CommandArgs(String[] tokens, Set<Character> allowedFlag, int min, int max) {
        flags = new HashSet<Character>();
        List<String> argsList = new ArrayList<String>(tokens.length);
        for (String token : tokens) {
            if (token.charAt(0) == '-' && token.length() > 1) {
                for (char flag : token.substring(1).toCharArray()) {
                    if (!allowedFlag.contains(flag)) {
                        throw new CommandUsageError("Flag invalide : -" + flag);
                    }
                    flags.add(flag);
                }
            } else if (!token.isEmpty()) {
                argsList.add(token);
            }
        }
        if (argsList.size() < min) {
            throw new CommandUsageError(
                    "Argument(s) manquant(s) (" + argsList.size() + " / " + min + " minimum)");
        }
        if (max != -1 && argsList.size() > max) {
            throw new CommandUsageError(
                    "Argument(s) en trop (" + argsList.size() + " / " + max + " maximum)");
        }
        args = argsList.toArray(new String[0]);
    }

    public boolean hasFlags() {
        return !flags.isEmpty();
    }

    public boolean hasFlag(char character) {
        return flags.contains(character);
    }

    public int length() {
        return args.length;
    }

    public String get(int index) {
        return args[index];
    }

    public String get(int index, int endIndex) {
        StringBuilder builder = new StringBuilder();
        for (int i = index; i < endIndex; i++) {
            builder.append(args[i]);
            builder.append(" ");
        }
        builder.append(args[endIndex]);
        return builder.toString();
    }

    public List<String> asList() {
        return Arrays.asList(args);
    }

    public List<String> asList(int index, int endIndex) {
        return asList().subList(index, endIndex);
    }
}
