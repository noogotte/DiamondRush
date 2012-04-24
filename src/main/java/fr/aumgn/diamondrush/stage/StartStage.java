package fr.aumgn.diamondrush.stage;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import fr.aumgn.bukkitutils.util.Vector;
import fr.aumgn.bukkitutils.util.Vector2D;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.Util;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;

public class StartStage extends TransitionStage {

    public StartStage(Game game, Stage nextStage) {
        super(game, nextStage, DiamondRush.getConfig().getStartDuration());
    }

    @Override
    public void start() {
        List<Team> teams = game.getTeams();
        Vector spawnPos = game.getSpawn().getMiddle(); 
        Iterator<Vector> positions = game.getSpawn().
                getStartPositions(teams.size()).iterator();
        for (Team team : teams) {
            Vector pos = positions.next();
            Vector2D dir = pos.subtract(spawnPos).to2D();
            initTeam(team, game.getWorld(), pos, dir);
        }
        for (Monster monster : game.getWorld().getEntitiesByClass(Monster.class)) {
            monster.remove();
        }

        game.getSpawn().create(game.getWorld());
        game.getWorld().setTime(0);
        game.sendMessage(ChatColor.GREEN + "La partie débute !");
        super.start();
    }

    private void initTeam(Team team, World world, Vector pos, Vector2D dir) {
        Player foreman = Util.pickRandom(team.getPlayers());
        team.setForeman(foreman);
        team.sendMessage(ChatColor.GREEN + foreman.getDisplayName() + " est le chef d'équipe.");

        for (Player player : team.getPlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(20);
            player.setFoodLevel(20);
            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i <= 39; i++) {
                inventory.setItem(i, null);
            }
            player.setExp(0);
        }

        foreman.teleport(pos.toLocation(world, dir));
    }
}
