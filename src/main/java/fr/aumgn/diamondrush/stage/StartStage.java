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
import fr.aumgn.diamondrush.game.Team;

public class StartStage extends TransitionStage {

    public StartStage(DiamondRush dr, Stage nextStage) {
        super(dr, nextStage, dr.getConfig().getStartDuration());
    }

    @Override
    public void start() {
        List<Team> teams = dr.getGame().getTeams();
        Vector spawnPos = dr.getGame().getSpawn().getMiddle(); 
        Iterator<Vector> positions = dr.getGame().getSpawn().
                getStartPositions(teams.size()).iterator();
        for (Team team : teams) {
            Vector pos = positions.next();
            Vector2D dir = pos.subtract(spawnPos).to2D();
            initTeam(team, dr.getGame().getWorld(), pos, dir);
        }
        for (Monster monster : dr.getGame().getWorld().getEntitiesByClass(Monster.class)) {
            monster.remove();
        }

        dr.getGame().getSpawn().create(dr.getGame().getWorld());
        dr.getGame().getWorld().setTime(0);
        dr.getGame().sendMessage(ChatColor.GREEN + "La partie débute !");
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
