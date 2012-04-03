package fr.aumgn.tobenamed.stage;

import org.bukkit.Material;

import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.util.TBNUtil;
import fr.aumgn.tobenamed.util.Vector;

public class SpawnStage extends PositioningStage {

    private static final int MinDistance = 20 * 20;

    public class NextStage implements Runnable {

        @Override
        public void run() {
            for (Team team : game.getTeams()) {
                if (validatePosition(team)) {
                    continue;
                }
                game.sendMessage(team.getName() + 
                        " a placé son spawn trop pres du totem.");
                removeBlocksFromWorld();
                removeBlocksFromInventories();
                giveBlocks();
                // Hidden delayed recursive call FTW !
                scheduleNextStage();
                return;
            }
            TBN.nextStage(null);
        }
    }

    public SpawnStage(Game game) {
        super(game);
    }

    @Override
    public void start() {
        super.start();
        game.sendMessage("Phase de placement du spawn.");
        scheduleNextStage();
    }

    private void scheduleNextStage() {
        TBNUtil.scheduleDelayed(600, new NextStage());
    }

    public boolean validatePosition(Team team) {
        Vector pos = getPosition(team);
        Vector totemPos = team.getTotem().getMiddle();
        int distance = totemPos.subtract(pos).lengthSq();
        System.out.println(team.getName() + " - " + distance);
        return distance > MinDistance;
    }

    @Override
    public void initPosition(Team team, Vector pos) {
        team.setSpawn(pos);
        team.getSpawn().create(game.getWorld());
    }

    @Override
    public Material getMaterial() {
        return Material.SMOOTH_BRICK;
    }
}
