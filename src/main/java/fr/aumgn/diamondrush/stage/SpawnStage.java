package fr.aumgn.diamondrush.stage;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.aumgn.bukkitutils.util.Vector;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.region.TeamSpawn;

public class SpawnStage extends PositioningStage {

    private int duration; 

    public SpawnStage(DiamondRush dr, Map<Team, Player> players) {
        super(dr, players);
        this.duration = dr.getConfig().getSpawnDuration();
    }

    @Override
    public void start() {
        super.start();
        Game game = dr.getGame();
        game.sendMessage("Phase de placement du spawn.");
        scheduleNextStage(duration, new DevelopmentStage(dr));

        giveBlocksAtStart();
    }

    @Override
    public void scheduleNextStage(int seconds, Stage nextStage) {
        schedule(seconds, new Runnable() {
            public void run() {
                nextStage();
            }
        });
    }

    public void nextStage() {
        for (Team team : dr.getGame().getTeams()) {
            if (validatePosition(team)) {
                continue;
            }
            dr.getGame().sendMessage(team.getDisplayName() + ChatColor.YELLOW +
                    " a placé son spawn trop près du totem.");
            removeBlocksFromWorld();
            removeBlocksFromInventories();
            clearPositions();
            giveBlocksAtStart();
            scheduleNextStage(duration, new DevelopmentStage(dr));
            return;
        }
        dr.nextStage(new DevelopmentStage(dr));
    }

    private void giveBlocksAtStart() {
        for (Team team : dr.getGame().getTeams()) {
            giveBlock(playersHoldingBlock.get(team));
        }
    }

    private boolean validatePosition(Team team) {
        Vector pos = getPosition(team);
        Vector totemPos = team.getTotem().getMiddle();
        double distance = totemPos.distanceSq(pos);
        return distance > dr.getConfig().getTotemSpawnMinDistance();
    }

    @Override
    public Material getMaterial() {
        return Material.SMOOTH_BRICK;
    }

    @Override
    public void initPosition(Team team, Vector pos) {
        TeamSpawn spawn = new TeamSpawn(pos);
        dr.teamSpawnSet(team, spawn);
    }
}
