package fr.aumgn.tobenamed.stage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        for (Team team : game.getTeams()) {
            for (Player player : team.getPlayers()) {
                player.getInventory().clear();
            }
            team.getForeman().getInventory().addItem(new ItemStack(Material.OBSIDIAN, 1));
        }
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
}
