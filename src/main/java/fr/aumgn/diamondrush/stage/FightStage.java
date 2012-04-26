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
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.stage.listeners.FightListener;

public class FightStage extends Stage {

    private FightListener listener;
    private Map<Team, Integer> deathsByTeam;
    private Map<Player, Integer> deathsByPlayer;
    private boolean surrender;

    public FightStage(Game game) {
        super(game);
        this.listener = new FightListener(this);
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
        game.sendMessage(ChatColor.GREEN + "La phase de combat commence.");
        for (Team team : game.getTeams()) {
            deathsByTeam.put(team, 0);
            for (Player player : team.getPlayers()) {
                deathsByPlayer.put(player, 0);
            }
        }
        int duration = DiamondRush.getConfig().getFightDuration(game.getTurnCount());
        scheduleNextStageWithTransition(duration, new DevelopmentStage(game));
    }

    @Override
    public void stop() {
        super.stop();
        game.sendMessage(ChatColor.GREEN + "Fin de la phase de combat.");
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
        game.sendMessage("L'équipe " + team.getDisplayName() + " s'est rendue.");
        scheduleNextStageWithTransition(
                DiamondRush.getConfig().getTimeLeftAfterSurrender(),
                new DevelopmentStage(game));
    }

    private void affect(Team team) {
        PotionEffect effect = new PotionEffect(malusTypeFor(team), 
                DiamondRush.getConfig().getSurrenderMalusDuration(), 10);
        for (Player player : team.getPlayers()) {
            player.addPotionEffect(effect);
        }
    }

    private PotionEffectType malusTypeFor(Team team) {
        int step = team.getSurrenders() / DiamondRush.getConfig().getSurrenderMalusStep();
        if (step > 2) {
            return PotionEffectType.SLOW;
        } else if (step == 2) {
            return PotionEffectType.BLINDNESS;
        } else {
            return PotionEffectType.CONFUSION;
        }
    }
}
