package fr.aumgn.diamondrush.stage;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.aumgn.bukkitutils.util.Vector;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.event.team.DRTotemSetEvent;
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
        scheduleNextStage(dr.getConfig().getTotemDuration(), new SpawnStage(dr));
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
        Totem totem = new Totem(pos, dr.getGame().getWorld().getMaxHeight());
        DRTotemSetEvent event = new DRTotemSetEvent(dr.getGame(), team, totem);
        dr.handleTotemSetEvent(event);
    }
}
