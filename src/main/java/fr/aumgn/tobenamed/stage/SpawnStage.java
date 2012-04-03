package fr.aumgn.tobenamed.stage;

import org.bukkit.Material;

import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.util.Vector;

public class SpawnStage extends PositioningStage {

    public SpawnStage(Game game) {
        super(game);
    }

    @Override
    public void start() {
        super.start();
        game.sendMessage("Phase de placement du spawn.");
        TBN.scheduleDelayed(600, new Runnable() {
            @Override
            public void run() {
                TBN.nextStage(null);
            }
        });
    }

    @Override
    public void initPosition(Team team, Vector pos) {
        team.setSpawn(pos);
        team.getSpawn().create(game.getWorld());
    }

    @Override
    public Material getMaterial() {
        return Material.OBSIDIAN;
    }
}
