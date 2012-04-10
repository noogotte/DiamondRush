package fr.aumgn.tobenamed.stage;

import java.util.Collection;
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
import org.bukkit.potion.PotionEffect;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.stage.listeners.PauseListener;

public class PauseStage extends Stage {

    public static class PlayerStatus {

        private Location location;
        private Material material;
        private byte data;
        private Collection<PotionEffect> potionEffects;

        public PlayerStatus(Player player) {
            Location loc = player.getLocation();
            location = new Location(loc.getWorld(), loc.getBlockX() + 0.5, 
                    loc.getBlockY(), loc.getBlockZ() + 0.5,
                    loc.getYaw(), loc.getPitch());
            Block block = location.getBlock().getRelative(BlockFace.DOWN);
            material = block.getType();
            data = block.getData();
            block.setType(Material.GLASS);
            restorePosition(player);

            this.potionEffects = player.getActivePotionEffects();
            for (PotionEffect activeEffect : potionEffects) {
                player.removePotionEffect(activeEffect.getType());
            }
        }

        public void restorePosition(Player player) {
            player.teleport(location);
        }

        public void restore(Player player) {
            Block block = location.getBlock().getRelative(BlockFace.DOWN);
            block.setTypeIdAndData(material.getId(), data, true);

            for (PotionEffect potionEffect : potionEffects) {
                player.addPotionEffect(potionEffect, true);
            }
        }
    }

    private Game game;
    private Stage oldStage;
    private PauseListener listener;
    private long time;
    private Map<Player, PlayerStatus> status;

    public PauseStage(Game game, Stage oldStage){
        this.game = game;
        this.oldStage = oldStage;
        this.listener = new PauseListener(this);
        this.status = new HashMap<Player, PlayerStatus>();
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
                status.put(player, new PlayerStatus(player));
            }
        }
    }

    @Override
    public void stop() {
        game.getWorld().setTime(time);
        game.sendMessage(ChatColor.GREEN + "La partie reprend !");
        for (Map.Entry<Player, PlayerStatus> playerStatus : status.entrySet()) {
            Player player = playerStatus.getKey();
            if (player.isOnline()) {
                playerStatus.getValue().restore(player);
            }
        }
    }

    public PlayerStatus getPlayerStatus(Player player) {
        return status.get(player);
    }

    public Stage getOldStage() {
        return oldStage;
    }
}
