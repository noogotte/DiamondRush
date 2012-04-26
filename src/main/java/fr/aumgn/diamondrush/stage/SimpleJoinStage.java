package fr.aumgn.diamondrush.stage;

import org.bukkit.event.EventHandler;

import fr.aumgn.diamondrush.event.game.DRGameStartEvent;
import fr.aumgn.diamondrush.exception.NotEnoughPlayers;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;

public class SimpleJoinStage extends JoinStage {

    public SimpleJoinStage(Game game) {
        super(game);
    }

    @EventHandler()
    public void onGameStart(DRGameStartEvent event) {
        for (Team team : game.getTeams()) {
            if (team.size() < 1) {
                event.setCancelled(true);
                throw new NotEnoughPlayers("L'équipe " + 
                        team.getDisplayName() + " n'a aucun joueur.");
            }
        }
    }
}
