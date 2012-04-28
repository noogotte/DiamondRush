package fr.aumgn.diamondrush.stage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aumgn.diamondrush.DiamondRush;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.stage.listeners.FightListener;

public class FightStage extends Stage {

    private FightListener listener;
    private Map<Team, Integer> deathsByTeam;
    private Map<Player, Integer> deathsByPlayer;
    private boolean surrender;

    public FightStage(DiamondRush dr) {
        super(dr);
        this.listener = new FightListener(dr, this);
        this.deathsByTeam = new HashMap<Team, Integer>();
        this.deathsByPlayer = new HashMap<Player, Integer>();
        this.surrender = false;
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.<Listener>singletonList(listener);
    }

    @Override
    public void start() {
        dr.getGame().sendMessage(ChatColor.GREEN + "La phase de combat commence.");
        for (Team team : dr.getGame().getTeams()) {
            deathsByTeam.put(team, 0);
            for (Player player : team.getPlayers()) {
                deathsByPlayer.put(player, 0);
            }
        }
        int duration = dr.getConfig().getFightDuration(dr.getGame().getTurnCount());
        scheduleNextStageWithTransition(duration, new DevelopmentStage(dr));
    }

    @Override
    public void stop() {
        super.stop();
        dr.getGame().sendMessage(ChatColor.GREEN + "Fin de la phase de combat.");
    }

    public boolean canSurrender() {
        return !surrender;
    }

    public int getDeathCount(Team team) {
        return deathsByTeam.get(team);
    }

    public int getDeathCount(Player player) {
        return deathsByPlayer.get(player);
    }

    public void incrementDeathCount(Team team, Player player) {
        int count = getDeathCount(team) + 1;
        deathsByTeam.put(team, count);
        count = getDeathCount(player) + 1;
        deathsByPlayer.put(player, count);
    }

    public void surrender(Team team) {
        surrender = true;
        team.incrementSurrenders();
        affect(team);
        cancelGameTimer();
        dr.getGame().sendMessage("L'équipe " + team.getDisplayName() + " s'est rendue.");
        scheduleNextStageWithTransition(
                dr.getConfig().getTimeLeftAfterSurrender(),
                new DevelopmentStage(dr));
    }

    private void affect(Team team) {
        PotionEffect effect = new PotionEffect(malusTypeFor(team), 
                dr.getConfig().getSurrenderMalusDuration(), 10);
        for (Player player : team.getPlayers()) {
            player.addPotionEffect(effect);
        }
    }

    private PotionEffectType malusTypeFor(Team team) {
        int step = team.getSurrenders() / dr.getConfig().getSurrenderMalusStep();
        if (step > 2) {
            return PotionEffectType.SLOW;
        } else if (step == 2) {
            return PotionEffectType.BLINDNESS;
        } else {
            return PotionEffectType.CONFUSION;
        }
    }
}
