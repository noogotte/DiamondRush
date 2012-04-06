package fr.aumgn.tobenamed.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.util.Vector;
import fr.aumgn.tobenamed.util.Vector2D;

public class GameListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (!TBN.isRunning()) {
            return;
        }

        Player player = event.getPlayer();
        Game game = TBN.getStage().getGame();
        if (!game.contains(player)) {
            return;
        }

        Team team = game.getTeam(player);
        if (team.getSpawn() == null) {
            Vector2D currentPos = new Vector(player.getLocation()).to2D();
            Vector pos = game.getSpawn().getMiddle();
            Vector2D dir = currentPos.subtract(pos.to2D());
            event.setRespawnLocation(pos.toLocation(game.getWorld(), dir));
        } else {
            Vector pos = team.getSpawn().getMiddle();
            Vector2D dir = game.getSpawn().getMiddle().to2D().subtract(pos.to2D());
            event.setRespawnLocation(pos.toLocation(game.getWorld(), dir));
        }
    }
}
