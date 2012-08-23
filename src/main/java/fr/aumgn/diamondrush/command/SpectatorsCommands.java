package fr.aumgn.diamondrush.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.aumgn.bukkitutils.command.Command;
import fr.aumgn.bukkitutils.command.args.CommandArgs;
import fr.aumgn.bukkitutils.command.NestedCommands;
import fr.aumgn.bukkitutils.command.exception.CommandError;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.exception.PlayerNotInGame;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.region.TeamSpawn;
import fr.aumgn.diamondrush.region.Totem;

@NestedCommands("diamondrush")
public class SpectatorsCommands extends DiamondRushCommands {

    public SpectatorsCommands(DiamondRush dr) {
        super(dr);
    }

    @Command(name = "watch")
    public void watchGame(Player player, CommandArgs args) {
        ensureIsRunning();
        dr.spectatorJoin(player);
    }

    private void ensureIsSpectator(Player player) {
        ensureIsRunning();
        Game game = dr.getGame();
        if (!game.getSpectators().contains(player)) {
            throw new CommandError("Cette commande n'est utilisable que pour un spectateur.");
        }
    }

    @Command(name = "unwatch", max = 1)
    public void unwatchGame(Player player, CommandArgs args) {
        ensureIsRunning();

        Player spectator;
        if (args.length() == 0) {
            spectator = player;
        } else {
            if (!player.hasPermission("dr.cmd.unwatch.others")) {
                throw new CommandError("Vous n'avez pas la permi");
            }
            spectator = args.getPlayer(0).value();
        }

        dr.spectatorQuit(spectator);
    }

    @Command(name = "tp-player", min = 1, max = 1)
    public void tpPlayer(Player player, CommandArgs args) {
        ensureIsSpectator(player);

        Game game = dr.getGame();
        Player target = args.getPlayer(0).value();
        if (!game.contains(target) || !game.getSpectators().contains(player)) {
            throw new PlayerNotInGame();
        }

        player.teleport(target);
        player.sendMessage(ChatColor.GREEN + "Poof !");
    }

    @Command(name = "tp-team", min = 1, max = 1, flags = "s")
    public void tpTeam(Player player, CommandArgs args) {
        ensureIsSpectator(player);

        Game game = dr.getGame();
        String arg = args.get(0);

        Team team = game.getTeam(arg);
        if (args.hasFlag('s')) {
            TeamSpawn spawn = team.getSpawn();
            if (spawn == null) {
                throw new CommandError("Cette équipe n'a pas encore de spawn.");
            }
            Location loc = spawn.getTeleportLocation(game.getWorld(), game.getSpawn());
            player.teleport(loc);
        } else {
            Totem totem = team.getTotem();
            if (totem == null) {
                throw new CommandError("Cette équipe n'a pas encore de totem.");
            }
            Location loc = totem.getTeleportLocation(game.getWorld(), game.getSpawn());
            player.teleport(loc);
        }
        player.sendMessage(ChatColor.GREEN + "Poof !");
    }

    @Command(name= "inventory", min = 1, max = 1)
    public void inv(Player spectator, CommandArgs args) {
        ensureIsSpectator(spectator);
        Player player = args.getPlayer(0).value();

        Game game = dr.getGame();
        if (!game.contains(player)) {
            throw new CommandError("Le joueur n'est pas en jeu.");
        }

        Inventory inventory = player.getInventory();
        spectator.openInventory(inventory);
    }
}
