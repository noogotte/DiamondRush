package fr.aumgn.tobenamed.stage;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;
import fr.aumgn.tobenamed.stage.listeners.StaticListener;

public class StaticStage extends Stage {

    public static class PlayerStatus {

        private Location location;
        private Material material;
        private byte data;
        private Collection<PotionEffect> potionEffects;
        private float fallDistance;
        private int remainingAir;
        private int fireTicks;

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

            this.fallDistance = player.getFallDistance();
            this.remainingAir = player.getRemainingAir();
            this.fireTicks = player.getFireTicks();
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

            player.setFallDistance(fallDistance);
            player.setRemainingAir(remainingAir);
            player.setFireTicks(fireTicks);
        }
    }

    protected Game game;
    private StaticListener listener;
    private long time;
    private Map<Player, PlayerStatus> status;

    public StaticStage(Game game) {
        this.game = game;
        this.listener = new StaticListener(this);
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
        for (Team team : game.getTeams()) {
            for (Player player : team.getPlayers()) {
                status.put(player, new PlayerStatus(player));
            }
        }
    }

    @Override
    public void stop() {
        game.getWorld().setTime(time);
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
}
