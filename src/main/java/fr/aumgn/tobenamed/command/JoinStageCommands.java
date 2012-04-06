package fr.aumgn.tobenamed.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aumgn.bukkit.command.Command;
import fr.aumgn.bukkit.command.CommandArgs;
import fr.aumgn.bukkit.command.CommandError;
import fr.aumgn.bukkit.command.Commands;
import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.stage.JoinStage;
import fr.aumgn.tobenamed.stage.Stage;
import fr.aumgn.tobenamed.stage.TotemStage;
import fr.aumgn.tobenamed.util.TBNUtil;
import fr.aumgn.tobenamed.util.Vector;

public class JoinStageCommands extends Commands {

    @Command(name = "init-game", flags = "a")
    public void initGame(Player player, CommandArgs args) {
        if (TBN.isRunning()) {
            throw new CommandError("Une partie est déja en cours.");
        }

        Game game = new Game(args.asList(), player.getWorld(),
                new Vector(player.getLocation()));
        JoinStage stage = new JoinStage(game, args.hasFlag('a'));
        TBN.nextStage(stage);
    }

    @Command(name = "join-team", max = 1)
    public void joinTeam(Player player, CommandArgs args) {
        Stage stage = TBN.getStage();

        Game game = stage.getGame();
        if (game.contains(player)) {
            throw new CommandError("Vous etes deja dans la partie.");
        }

        boolean allowDirectTeamJoin = (stage instanceof JoinStage) 
                && !((JoinStage) stage).isRandom();
        if (args.length() > 0 && allowDirectTeamJoin) {
            Team team = game.getTeam(args.get(0));
            game.addPlayer(player, team);
        } else {
            game.addPlayer(player);
        }
    }

    @Command(name = "start-game", max = 0)
    public void startGame(CommandSender sender, CommandArgs args) {
        final Stage stage = TBN.getStage();

        if (!(stage instanceof JoinStage)) {
            throw new CommandError("Cette commande ne peut etre utilisé que durant la phase de join.");
        }

        for (Team team : stage.getGame().getTeams()) {
            if (team.size() < 1) {
                throw new CommandError("L'equipe " + team.getDisplayName() + " n'a aucun joueur");
            }
        }

        TBNUtil.scheduleDelayed(200, new Runnable() {
            @Override
            public void run() {
                TotemStage totemStage = new TotemStage(stage.getGame());
                TBN.nextStage(totemStage);
            }
        });
        stage.getGame().sendMessage(ChatColor.GREEN + "La partie commence dans 10 secondes !");
    }
}
