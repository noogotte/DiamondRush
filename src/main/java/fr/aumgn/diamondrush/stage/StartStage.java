package fr.aumgn.diamondrush.stage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public StartStage(DiamondRush dr) {
        super(dr, new TotemStage(dr), dr.getConfig().getStartDuration());
    }

    @Override
    public void start() {
        Game game = dr.getGame();
        List<Team> teams = game.getTeams();
        Vector spawnPos = game.getSpawn().getMiddle(); 
        Iterator<Vector> positions = game.getSpawn().
                getStartPositions(teams.size()).iterator();
        Map<Team, Player> foremen = new HashMap<Team, Player>();
        for (Team team : teams) {
            Player foreman = Util.pickRandom(team.getPlayers());
            foremen.put(team, foreman);
            team.sendMessage(ChatColor.GREEN + foreman.getDisplayName() +
                    " est le chef d'équipe.");
            Vector pos = positions.next();
            Vector2D dir = pos.subtract(spawnPos).to2D();
            initTeam(team, game.getWorld(), pos, dir);
        }
        ((TotemStage) nextStage).setForemen(foremen);
        for (Monster monster : game.getWorld().getEntitiesByClass(Monster.class)) {
            monster.remove();
        }

        game.getSpawn().create(game.getWorld());
        game.getWorld().setTime(0);
        game.sendMessage(ChatColor.GREEN + "La partie débute !");
        super.start();
    }

    private void initTeam(Team team, World world, Vector pos, Vector2D dir) {
        Vector offset = dir.rotate90().normalize().to3D();
        int i;
        boolean left;
        if ((team.size() & 1) == 0) {
            i = 1;
            left = true;
        } else {
            i = 0;
            left = false;
        }

        float yaw = dir.toYaw();
        for (Player player : team.getPlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(20);
            player.setFoodLevel(20);
            PlayerInventory inventory = player.getInventory();
            for (int j = 0; j <= 39; j++) {
                inventory.setItem(j, null);
            }
            player.setTotalExperience(0);

            Vector playerPos;
            if (left) {
                playerPos = pos.subtract(offset.multiply(i));
                left = false;
            } else {
                playerPos = pos.add(offset.multiply(i));
                left = true;
                i++;
            }
            player.teleport(playerPos.toLocation(world, yaw));
        }
    }
}
