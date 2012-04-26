package fr.aumgn.diamondrush.stage;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import fr.aumgn.bukkitutils.util.Vector;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.event.team.DRTotemSetEvent;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.region.ChestPopulator;
import fr.aumgn.diamondrush.region.Totem;

public class TotemStage extends PositioningStage {

    public TotemStage(Game game) {
        super(game);
    }

    @Override
    public void start() {
        super.start();
        game.sendMessage("Phase de placement du totem.");
        scheduleNextStage(DiamondRush.getConfig().getTotemDuration(), new SpawnStage(game));
    }

    @Override
    public void stop() {
        super.stop();
        List<Team> teams = game.getTeams();
        List<Totem> totems = new ArrayList<Totem>(teams.size());
        for (Team team : teams) {
            totems.add(team.getTotem());
        }
        ChestPopulator chestPopulator = new ChestPopulator(game.getSpawn(), totems, 50);
        chestPopulator.populate(game.getWorld(), 2 * teams.size() - 1);
    }

    @Override
    public Material getMaterial() {
        return Material.OBSIDIAN;
    }

    @Override
    public void initPosition(Team team, Vector pos) {
        Totem totem = new Totem(pos, game.getWorld().getMaxHeight());
        DRTotemSetEvent event = new DRTotemSetEvent(game, team, totem);
        DiamondRush.getController().handleTotemSetEvent(event);
    }
}
