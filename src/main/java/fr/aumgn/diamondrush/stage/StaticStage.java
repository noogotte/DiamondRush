package fr.aumgn.diamondrush.stage;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.aumgn.bukkitutils.playerref.PlayerRef;
import fr.aumgn.bukkitutils.playerref.map.PlayersRefHashMap;
import fr.aumgn.bukkitutils.playerref.map.PlayersRefMap;
import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.stage.listeners.StaticListener;

public class StaticStage extends Stage {

    private StaticListener listener;
    private long time;
    private PlayersRefMap<StaticPlayer> status;

    public StaticStage(DiamondRush dr) {
        super(dr);
        this.listener = new StaticListener(this, dr.getGame());
        this.status = new PlayersRefHashMap<StaticPlayer>();
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.<Listener>singletonList(listener);
    }

    @Override
    public void start() {
        this.time = dr.getGame().getWorld().getTime();
        for (Team team : dr.getGame().getTeams()) {
            for (Player player : team.getPlayers()) {
                status.put(player, new StaticPlayer(player));
            }
        }
        for (Team team : dr.getGame().getTeams()) {
            for (Player player : team.getPlayers()) {
                status.get(player).init(player);
            }
        }
    }

    @Override
    public void stop() {
        dr.getGame().getWorld().setTime(time);
        for (final Map.Entry<PlayerRef, StaticPlayer> entry :
                status.entrySet()) {
            entry.getValue().restoreEnvironment();

            final PlayerRef playerId = entry.getKey();
            if (playerId.isOffline()) {
                final StaticPlayer playerStatus = entry.getValue();
                dr.onReconnect(playerId, new Runnable() {
                    public void run() {
                        playerStatus.restore(playerId.getPlayer());
                    }
                });
            } else {
                entry.getValue().restore(playerId.getPlayer());
            }
        }
    }

    public void initPlayer(Player player) {
        StaticPlayer playerStatus = new StaticPlayer(player);
        status.put(player, playerStatus);
        playerStatus.init(player);
    }

    public StaticPlayer getPlayerStatus(Player player) {
        return status.get(player);
    }
}
