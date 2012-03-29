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
            if (token.length() == 2 && token.charAt(0) == '-') {
                char flag = token.charAt(1);
                if (!allowedFlag.contains(flag)) {
                    throw new CommandUsageError("Flag invalide : -" + flag);
                }
                flags.add(flag);
            } else if (!token.isEmpty()) {
                argsList.add(token);
            }
        }
        if (argsList.size() < min) {
            throw new CommandUsageError("Arguments manquant(s) (" + argsList.size() + " / " + min + " minimum)");
        }
        if (max != -1 && argsList.size() > max) {
            throw new CommandUsageError("Arguments en trop (" + argsList.size() + " / " + max + " maximum)");
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
        StringBuffer buffer = new StringBuffer();
        for (int i = index; i < endIndex; i++) {
            buffer.append(args[i]);
            buffer.append(" ");
        }
        buffer.append(args[endIndex]);
        return buffer.toString();
    }

    public List<String> asList() {
        return Arrays.asList(args);
    }

    public List<String> asList(int index, int endIndex) {
        return asList().subList(index, endIndex);
    }
}
