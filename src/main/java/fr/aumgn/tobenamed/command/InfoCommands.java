package fr.aumgn.tobenamed.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.aumgn.bukkit.command.Command;
import fr.aumgn.bukkit.command.CommandArgs;
import fr.aumgn.bukkit.command.Commands;
import fr.aumgn.tobenamed.TBN;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.game.Team;

public class InfoCommands extends Commands {

    @Command(name = "show-teams", max = 0)
    public void showTeams(Player player, CommandArgs args) {
        Game game = TBN.getStage().getGame();
        player.sendMessage(ChatColor.GREEN + "Equipes :");
        for (Team team : game.getTeams()) {
            StringBuffer teamMessage = new StringBuffer();
            teamMessage.append(" - ");
            teamMessage.append(ChatColor.GOLD);
            teamMessage.append(team.getName());
            teamMessage.append(ChatColor.RESET);
            if (team.size() > 0) {
                teamMessage.append(" : ");
                Player foreman = team.getForeman();
                if (foreman != null) {
                    teamMessage.append(ChatColor.AQUA + foreman.getDisplayName());
                    teamMessage.append(" ");
                }
                teamMessage.append(ChatColor.BLUE);
                for (Player teamPlayer : team.getPlayers()) {
                    if (foreman.equals(teamPlayer)) {
                        continue;
                    }
                    teamMessage.append(teamPlayer.getDisplayName());
                    teamMessage.append(" ");
                }
            }
            player.sendMessage(teamMessage.toString());
        }
    }
}
