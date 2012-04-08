package fr.aumgn.tobenamed.stage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.stage.listeners.PauseListener;

public class PauseStage extends Stage {

    public static class PauseLocation {

        private Location location;
        private Material material;
        private byte data;

        public PauseLocation(Player player) {
            Location loc = player.getLocation();
            location = new Location(loc.getWorld(), loc.getBlockX() + 0.5, 
                    loc.getBlockY(), loc.getBlockZ() + 0.5,
                    loc.getYaw(), loc.getPitch());
            Block block = location.getBlock().getRelative(BlockFace.DOWN);
            material = block.getType();
            data = block.getData();
            block.setType(Material.GLASS);
            teleportTo(player);
        }

        public void teleportTo(Player player) {
            player.teleport(location);
        }

        public void restore() {
            Block block = location.getBlock().getRelative(BlockFace.DOWN);
            block.setTypeIdAndData(material.getId(), data, true);
        }
    }

    private Game game;
    private Stage oldStage;
    private PauseListener listener;
    private long time;
    private Map<Player, PauseLocation> locations;

    public PauseStage(Game game, Stage oldStage){
        this.game = game;
        this.oldStage = oldStage;
        this.listener = new PauseListener(this);
        this.locations = new HashMap<Player, PauseStage.PauseLocation>();
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.<Listener>singletonList(listener);
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void start() {
        this.time = game.getWorld().getTime();
        game.sendMessage(ChatColor.RED +
                "TBN est en pause, vous ne pouvez rien faire !");
        for (Team team : game.getTeams()) {
            for (Player player : team.getPlayers()) {
                locations.put(player, new PauseLocation(player));
            }
        }
    }

    @Override
    public void stop() {
        game.getWorld().setTime(time);
        game.sendMessage(ChatColor.GREEN + "La partie reprend !");
        for (PauseLocation location : locations.values()) {
            location.restore();
        }
    }

    public PauseLocation getPauseLocation(Player player) {
        return locations.get(player);
    }

    public Stage getOldStage() {
        return oldStage;
    }
}
