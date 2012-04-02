package fr.aumgn.tobenamed.stage;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.util.TBNUtil;
import fr.aumgn.tobenamed.util.Vector;

public class TotemStage extends PositioningStage {

    public TotemStage(Game game) {
        super(game);
    }

    @Override
    public void start() {
        List<Team> teams = game.getTeams();
        Iterator<Vector> directions = game.getSpawn().
                getDirections(teams.size()).iterator();
        for (Team team : teams) {
            initTeam(team, game.getWorld(), directions.next());
        }

        game.getSpawn().create(game.getWorld());
        game.getWorld().setTime(0);
        game.sendMessage(ChatColor.GREEN + "La partie débute !");
        TBN.scheduleDelayed(600, new Runnable() {
            @Override
            public void run() {
                TBN.nextStage(new SpawnStage(game));
            }
        });
    }

    private void initTeam(Team team, World world, Vector pos) {
        Player foreman = TBNUtil.pickRandom(team.getPlayers());
        team.setForeman(foreman);
        team.sendMessage(ChatColor.GREEN + foreman.getDisplayName() + " est le chef d'equipe.");

        for (Player player : team.getPlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(20);
            player.setFoodLevel(20);
            player.getInventory().clear();
        }

        foreman.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 1));
        foreman.teleport(pos.toPlayerLocation(world));
    }

    @Override
    public void initPosition(Team team, Vector pos) {
        team.setTotem(pos);
        team.getTotem().create(game.getWorld());
    }
}
