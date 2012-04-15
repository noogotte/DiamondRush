package fr.aumgn.diamondrush.stage;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import fr.aumgn.bukkit.util.Vector;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;

public class SpawnStage extends PositioningStage {

    private int duration; 

    public SpawnStage(Game game) {
        super(game);
        this.duration = DiamondRush.getConfig().getSpawnDuration();
    }

    @Override
    public void start() {
        super.start();
        game.sendMessage("Phase de placement du spawn.");
        scheduleNextStage(duration, new DevelopmentStage(game));
    }

    @Override
    public void scheduleNextStage(int seconds, Stage nextStage) {
        schedule(seconds, new Runnable() {
            public void run() {
                nextStage();
            }
        });
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

    public void nextStage() {
        for (Team team : game.getTeams()) {
            if (validatePosition(team)) {
                continue;
            }
            game.sendMessage(team.getDisplayName() + ChatColor.YELLOW +
                    " a placé son spawn trop près du totem.");
            removeBlocksFromWorld();
            removeBlocksFromInventories();
            clearPositions();
            giveBlocks();
            scheduleNextStage(duration, new DevelopmentStage(game));
            return;
        }
        game.nextStage(new DevelopmentStage(game));
    }

    private boolean validatePosition(Team team) {
        Vector pos = getPosition(team);
        Vector totemPos = team.getTotem().getMiddle();
        int distance = totemPos.distanceSq(pos);
        return distance > DiamondRush.getConfig().getTotemSpawnMinDistance();
    }
}
