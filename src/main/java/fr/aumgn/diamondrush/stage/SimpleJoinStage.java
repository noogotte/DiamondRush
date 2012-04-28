package fr.aumgn.diamondrush.stage;

import org.bukkit.event.EventHandler;

import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.event.game.DRGameStartEvent;
import fr.aumgn.diamondrush.exception.NotEnoughPlayers;
import fr.aumgn.diamondrush.game.Team;

public class SimpleJoinStage extends JoinStage {

    public SimpleJoinStage(DiamondRush dr) {
        super(dr);
    }

    @EventHandler()
    public void onGameStart(DRGameStartEvent event) {
        for (Team team : dr.getGame().getTeams()) {
            if (team.size() < 1) {
                event.setCancelled(true);
                throw new NotEnoughPlayers("L'équipe " + 
                        team.getDisplayName() + " n'a aucun joueur.");
            }
        }
    }
}
