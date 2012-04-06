package fr.aumgn.tobenamed.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.aumgn.bukkit.command.Command;
import fr.aumgn.bukkit.command.CommandArgs;
import fr.aumgn.bukkit.command.CommandError;
import fr.aumgn.bukkit.command.Commands;
import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.exception.PlayerNotInGame;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Spectators;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.region.TeamSpawn;
import fr.aumgn.tobenamed.region.Totem;

public class SpectatorsCommands extends Commands {

    @Command(name = "watch-game", max = 0)
    public void watchGame(Player player, CommandArgs args) {
        Game game = TBN.getGame();
        if (game.contains(player)) {
            throw new CommandError("Vous etes deja dans la partie.");
        }

        Spectators spectators = game.getSpectators();
        if (spectators.contains(player)) {
            throw new CommandError("Vous etes deja spectateur.");
        }

        spectators.add(player);
        player.sendMessage(ChatColor.GREEN + "Vous etes maintenant spectateur.");
    }

    private void ensureIsSpectator(Player player) {
        Game game = TBN.getGame();
        if (!game.getSpectators().contains(player)) {
            throw new CommandError("Cette commande n'est utilisable qu'en tant que spectateur.");
        }
    }

    @Command(name = "unwatch-game", max = 0)
    public void unwatchGame(Player player, CommandArgs args) {
        ensureIsSpectator(player);
        Game game = TBN.getGame();
        game.getSpectators().remove(player);
        player.sendMessage(ChatColor.GREEN + "Vous n'etes plus spectateur.");
    }

    @Command(name = "teleport-player", min = 1, max = 1)
    public void tpPlayer(Player player, CommandArgs args) {
        ensureIsSpectator(player);

        Game game = TBN.getGame();
        String arg = args.get(0);

        List<Player> players = Bukkit.matchPlayer(arg);
        if (players.size() > 1) {
            throw new CommandError("Plus d'1 joueur trouvés avec le motif " + arg + ".");
        } else if (players.size() < 1) {
            throw new CommandError("Aucun joueur trouvé.");
        }

        Player target = players.get(0);
        if (!game.contains(target)) {
            throw new PlayerNotInGame();
        }
        player.teleport(target);
        player.sendMessage("Poof !");
    }

    @Command(name = "teleport-team", min = 1, max = 1, flags = "sf")
    public void tpTeam(Player player, CommandArgs args) {
        ensureIsSpectator(player);

        Game game = TBN.getGame();
        String arg = args.get(0);

        Team team = game.getTeam(arg);
        if (args.hasFlag('f')) {
            Player foreman = team.getForeman();
            if (foreman == null) {
                throw new CommandError("Cette equipe n'a pas encore de chef d'equipe.");
            }
            player.teleport(foreman);
        } else if (args.hasFlag('s')) {
            TeamSpawn spawn = team.getSpawn();
            if (spawn == null) {
                throw new CommandError("Cette equipe n'a pas encore de spawn.");
            }
            Location loc = spawn.getMiddle().toLocation(game.getWorld());
            player.teleport(loc);
        } else {
            Totem totem = team.getTotem();
            if (totem == null) {
                throw new CommandError("Cette equipe n'a pas encore de totem.");
            }
            Location loc = totem.getMiddle().toLocation(game.getWorld());
            player.teleport(loc);
        }
        player.sendMessage("Poof !");
    }
}
