package fr.aumgn.diamondrush;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import fr.aumgn.bukkitutils.command.exception.CommandError;
import fr.aumgn.diamondrush.config.DRConfig;
import fr.aumgn.diamondrush.event.game.DRGameStartEvent;
import fr.aumgn.diamondrush.event.game.DRGameStopEvent;
import fr.aumgn.diamondrush.event.game.DRGameWinEvent;
import fr.aumgn.diamondrush.event.players.DRPlayerJoinEvent;
import fr.aumgn.diamondrush.event.players.DRPlayerQuitEvent;
import fr.aumgn.diamondrush.event.spectators.DRSpectatorJoinEvent;
import fr.aumgn.diamondrush.event.spectators.DRSpectatorQuitEvent;
import fr.aumgn.diamondrush.event.team.DRTeamLooseEvent;
import fr.aumgn.diamondrush.event.team.DRTeamSpawnSetEvent;
import fr.aumgn.diamondrush.event.team.DRTotemMinedEvent;
import fr.aumgn.diamondrush.event.team.DRTotemSetEvent;
import fr.aumgn.diamondrush.exception.NoGameRunning;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.listeners.GameListener;
import fr.aumgn.diamondrush.listeners.RegionsListener;
import fr.aumgn.diamondrush.listeners.SpectatorsListener;
import fr.aumgn.diamondrush.region.TeamSpawn;
import fr.aumgn.diamondrush.region.Totem;
import fr.aumgn.diamondrush.stage.JoinStage;
import fr.aumgn.diamondrush.stage.PauseStage;
import fr.aumgn.diamondrush.stage.Stage;
import fr.aumgn.diamondrush.stage.StartStage;
import fr.aumgn.diamondrush.stage.TotemStage;

public final class DiamondRush {

    private DiamondRushPlugin plugin;
    private DRConfig config;
    private Game game;
    private Stage stage;
    private Listener[] listeners;

    public DiamondRush(DiamondRushPlugin plugin) {
        this.plugin = plugin;
        this.game = null;
        this.stage = null;
        this.listeners = new Listener[3];
    }

    public DiamondRushPlugin getPlugin() {
        return plugin;
    }

    public DRConfig getConfig() {
        return config;
    }

    public boolean isRunning() {
        return game != null;
    }

    public Game getGame() {
        if (!isRunning()) {
            throw new NoGameRunning();
        }
        return game;
    }

    public Stage getStage() {
        return stage;
    }

    public void initGame(Game game, JoinStage stage) {
        this.game = game;
        PluginManager pm = Bukkit.getPluginManager();
        listeners[0] = new GameListener(this);
        listeners[1] = new RegionsListener(this);
        listeners[2] = new SpectatorsListener(this);
        for (Listener listener : listeners) {
            pm.registerEvents(listener, plugin);
        }
        nextStage(stage);
    }

    private void unregisterStageListeners() {
        for (Listener listener : stage.getListeners()) {
            HandlerList.unregisterAll(listener);
        }
    }

    private void registerStageListeners() {
        for (Listener listener : stage.getListeners()) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

    public void nextStage(Stage newStage) {
        if (newStage == null) {
            throw new IllegalArgumentException("New stage cannot be null");
        }
        if (stage != null) {
            unregisterStageListeners();
            Bukkit.getScheduler().cancelTasks(plugin);
            stage.stop();
        }
        stage = newStage;
        registerStageListeners();
        stage.start();
    }

    public boolean isPaused() {
        return (stage instanceof PauseStage);
    }

    public void pause() {
        if (stage == null) {
            throw new UnsupportedOperationException();
        }
        unregisterStageListeners();
        stage.pause();
        stage = new PauseStage(this, stage);
        stage.start();
        registerStageListeners();
    }

    public void resume() {
        unregisterStageListeners();
        stage.stop();
        stage = ((PauseStage) stage).getOldStage();
        registerStageListeners();
        stage.resume();
    }

    public void forceStop() {
        for (Listener listener : stage.getListeners()) {
            HandlerList.unregisterAll(listener);
        }
        Bukkit.getScheduler().cancelTasks(plugin);

        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }
        game = null;
    }

    public void handlePlayerJoinEvent(DRPlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!game.contains(player)) {
            Util.callEvent(event);
            if (!event.isCancelled()) {
                game.addPlayer(player, event.getTeam());
                Util.broadcast(player.getDisplayName() + ChatColor.YELLOW +
                        " a rejoint l'équipe " + event.getTeam().getDisplayName());
            }
        } else {
            player.sendMessage(ChatColor.RED + 
                    "Vous êtes déjà dans la partie.");
        }
    }

    public void handlePlayerQuitEvent(DRPlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (game.contains(player)) {
            Util.callEvent(event);
            if (!event.isCancelled()) {
                game.removePlayer(player);
                Util.broadcast(player.getDisplayName() + ChatColor.YELLOW +
                        " a quitté la partie.");
            }
        } else {
            player.sendMessage(ChatColor.RED + 
                    "Vous n'êtes pas dans la partie.");
        }
    }

    public void handleSpectatorJoinEvent(DRSpectatorJoinEvent event) {
        Player spectator = event.getSpectator();
        if (!game.getSpectators().contains(spectator)) {
            Util.callEvent(event);
            if (!event.isCancelled()) {
                Player player = event.getSpectator();
                game.sendMessage(player.getDisplayName() + ChatColor.YELLOW +
                        " est maintenant spectateur.");
                game.getSpectators().add(player);
                player.sendMessage(ChatColor.GREEN + "Vous êtes maintenant spectateur.");
            }
        } else {
            spectator.sendMessage(ChatColor.RED + "Vous êtes déjà spectateur.");
        }
    }

    public void handleSpectatorQuitEvent(DRSpectatorQuitEvent event) {
        Player spectator = event.getSpectator();
        if (game.getSpectators().contains(spectator)) {
            Util.callEvent(event);
            game.getSpectators().remove(spectator);
            spectator.sendMessage(ChatColor.GREEN + "Vous n'êtes plus spectateur.");
            game.sendMessage(spectator.getDisplayName() + ChatColor.YELLOW +
                    " n'est plus spectateur.");
        } else {
            spectator.sendMessage(spectator.getDisplayName() + "n'est pas spectateur.");
        }
    }

    public void handleGameStartEvent(DRGameStartEvent event) {
        if (!(stage instanceof JoinStage)) {
            throw new CommandError("Cette commande ne peut être utilisée que durant la phase de join.");
        }

        if (stage.hasNextStageScheduled()) {
            throw new CommandError(
                    "La partie est déjà sur le point de démarrer.");
        }

        Util.callEvent(event);
        if (!event.isCancelled()) {
            game.sendMessage(ChatColor.GREEN + "La partie va commencer.");
            nextStage(new StartStage(this, new TotemStage(this)));
        }
    }

    public void handleTotemMinedEvent(DRTotemMinedEvent event) {
        Util.callEvent(event);
        if (!event.isCancelled()) {
            Team team = event.getTeam();
            team.decreaseLives();
            if (team.getLives() == 0) {
                Team responsible = game.getTeam(event.getPlayer());
                Totem totem = event.getRegion();
                DRTeamLooseEvent teamLooseEvent = 
                        new DRTeamLooseEvent(game, team, responsible, totem);
                handleTeamLooseEvent(teamLooseEvent);
            } else {
                game.sendMessage(ChatColor.YELLOW + "L'équipe " + team.getDisplayName() + 
                        ChatColor.YELLOW + " a perdu une vie. " + team.getLives() + " restantes.");
            }
        }
    }

    public void handleTeamLooseEvent(DRTeamLooseEvent event) {
        Util.callEvent(event);
        Team team = event.getTeam();
        game.removeTeam(team);
        if (game.getTeams().size() == 1) {
            DRGameWinEvent winEvent = new DRGameWinEvent(game, team);
            handleGameWinEvent(winEvent);
        } else {
            String msg = ChatColor.RED +"L'équipe " + team.getDisplayName() 
                    + ChatColor.RED + " a perdu la partie.";
            game.sendMessage(msg);
            team.sendMessage(msg);
        }
    }

    public void handleGameWinEvent(DRGameWinEvent event) {
        Util.callEvent(event);
        Team winningTeam = game.getTeams().get(0);
        String msg = ChatColor.GREEN +
                "L'équipe " + winningTeam.getDisplayName() +
                ChatColor.GREEN + " a gagné la partie.";
        game.sendMessage(msg);
        forceStop();
    }

    public void handleTotemSetEvent(DRTotemSetEvent event) {
        Util.callEvent(event);
        Team team = event.getTeam();
        Totem totem = event.getRegion();
        team.setTotem(totem);
        totem.create(game.getWorld(), team.getColor(), team.getLives());
    }

    public void handleTeamSpawnSetEvent(DRTeamSpawnSetEvent event) {
        Util.callEvent(event);
        Team team = event.getTeam();
        TeamSpawn spawn = event.getRegion();
        team.setSpawn(spawn);
        team.getSpawn().create(game.getWorld(), team.getColor());
    }

    public void handleGameStopEvent(DRGameStopEvent event) {
        Util.callEvent(event);
        forceStop();
        game.sendMessage(ChatColor.RED + "La partie a été arretée.");
    }
}
