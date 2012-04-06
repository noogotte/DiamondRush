package fr.aumgn.tobenamed.stage;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.util.TBNUtil;
import fr.aumgn.tobenamed.util.Vector;

public class SpawnStage extends PositioningStage {

    private static final int MIN_DISTANCE = 20 * 20;
    private static final int DELAY = 30 * 20;

    public class SpawnNextStageTask extends NextStageTask {
        @Override
        public void run() {
            for (Team team : game.getTeams()) {
                if (validatePosition(team)) {
                    continue;
                }
                game.sendMessage(team.getDisplayName() + ChatColor.YELLOW +
                        " a placé son spawn trop pres du totem.");
                removeBlocksFromWorld();
                removeBlocksFromInventories();
                clearPositions();
                giveBlocks();
                // Hidden delayed recursive call FTW !
                scheduleNextStage(DELAY);
                return;
            }
            super.run();
        }
    }

    public SpawnStage(Game game) {
        super(game);
    }

    @Override
    public void start() {
        super.start();
        game.sendMessage("Phase de placement du spawn.");
        scheduleNextStage(DELAY);
    }

    @Override
    protected void scheduleNextStage(int ticks) {
        TBNUtil.scheduleDelayed(ticks, new SpawnNextStageTask());
    }

    @Override
    public Stage nextStage() {
        return new DevelopmentStage(game);
    }

    public boolean validatePosition(Team team) {
        Vector pos = getPosition(team);
        Vector totemPos = team.getTotem().getMiddle();
        int distance = totemPos.distanceSq(pos);
        return distance > MIN_DISTANCE;
    }

    @Override
    public void initPosition(Team team, Vector pos) {
        team.setSpawn(pos, game.getWorld().getMaxHeight());
        team.getSpawn().create(game.getWorld(), team.getColor());
    }

    @Override
    public Material getMaterial() {
        return Material.SMOOTH_BRICK;
    }
}
