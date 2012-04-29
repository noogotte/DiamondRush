package fr.aumgn.diamondrush.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;

import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Statistics;

public class PlayerView implements Iterable<String> {

    private List<String> messages;

    public PlayerView(Game game, Player player) {
        this.messages = new ArrayList<String>();
        this.messages.add(player.getDisplayName() + 
                " (" + game.getTeam(player).getDisplayName() + ")");
        this.messages.add("Vies : " + player.getHealth() + " / 20");
        this.messages.add("Faim : " + player.getFoodLevel() + " / 20");
        Statistics statistics = game.getStatistics();
        this.messages.add("Kills : " + statistics.getKills(player));
        this.messages.add("Morts : " + statistics.getDeaths(player));
        this.messages.add("Blocs minés : " + statistics.getTotemBlockBroken(player));
    }

    @Override
    public Iterator<String> iterator() {
        return messages.iterator();
    }
}
