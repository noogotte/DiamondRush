package fr.aumgn.diamondrush.stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.aumgn.bukkitutils.util.Vector;
import fr.aumgn.bukkitutils.util.Vector2D;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.Util;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.region.ChestPopulator;
import fr.aumgn.diamondrush.region.Totem;

public class TotemStage extends PositioningStage {

    public TotemStage(DiamondRush dr) {
        super(dr);
    }

    @Override
    public void start() {
        super.start();
        dr.getGame().sendMessage("Phase de placement du totem.");

        Game game = dr.getGame();
        List<Team> teams = game.getTeams();
        Vector spawnPos = game.getSpawn().getMiddle(); 
        Iterator<Vector> spawnPositions = game.getSpawn().
                getStartPositions(teams.size()).iterator();
        for (Team team : teams) {
            Vector pos = spawnPositions.next();
            Vector2D dir = pos.subtract(spawnPos).to2D();
            Vector2D blockPos2D = dir.normalize().multiply(2);
            blockPos2D = pos.to2D().add(blockPos2D);
            int y = game.getWorld().getHighestBlockYAt(blockPos2D.getBlockX(),
                    blockPos2D.getBlockZ());
            Vector blockPos = blockPos2D.to3D(y);
            playersHoldingBlock.put(team, Util.pickRandom(team.getPlayers()));
            positions.put(team, blockPos);
            blockPos.toBlock(game.getWorld()).setType(getMaterial());
        }

        SpawnStage nextStage = new SpawnStage(dr, playersHoldingBlock);
        scheduleNextStage(dr.getConfig().getTotemDuration(), nextStage);
    }

    @Override
    public void stop() {
        super.stop();
        List<Team> teams = dr.getGame().getTeams();
        List<Totem> totems = new ArrayList<Totem>(teams.size());
        for (Team team : teams) {
            totems.add(team.getTotem());
        }
        ChestPopulator chestPopulator = new ChestPopulator(dr.getGame().getSpawn(), totems, 50);
        int chestCount = 2 * dr.getGame().getTeams().size() - 1;
        ArrayList<ItemStack[]> chests = new ArrayList<ItemStack[]>(chestCount);
        for (int i = 0; i < chestCount; i++) {
            chests.add(dr.getConfig().getRandomBonus());
        }
        chestPopulator.populate(dr.getGame().getWorld(), chests);
    }

    @Override
    public Material getMaterial() {
        return Material.OBSIDIAN;
    }

    @Override
    public void initPosition(Team team, Vector pos) {
        Totem totem = new Totem(pos,
                dr.getGame().getWorld().getMaxHeight());
        dr.totemSet(team, totem);
    }
}
