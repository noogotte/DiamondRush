package fr.aumgn.diamondrush;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import fr.aumgn.bukkitutils.localization.PluginResourceBundles;
import fr.aumgn.bukkitutils.localization.bundle.PluginResourceBundle;
import fr.aumgn.bukkitutils.playerid.PlayerId;
import fr.aumgn.bukkitutils.playerid.map.PlayersIdHashMap;
import fr.aumgn.bukkitutils.playerid.map.PlayersIdMap;
import fr.aumgn.bukkitutils.util.Util;
import fr.aumgn.diamondrush.config.DRConfig;
import fr.aumgn.diamondrush.event.game.DRGameStartEvent;
import fr.aumgn.diamondrush.event.game.DRGameStopEvent;
import fr.aumgn.diamondrush.event.game.DRGameWinEvent;
import fr.aumgn.diamondrush.event.players.DRPlayerJoinEvent;
import fr.aumgn.diamondrush.event.players.DRPlayerQuitEvent;
import fr.aumgn.diamondrush.event.players.DRSpectatorJoinEvent;
import fr.aumgn.diamondrush.event.players.DRSpectatorQuitEvent;
import fr.aumgn.diamondrush.event.team.DRTeamLooseEvent;
import fr.aumgn.diamondrush.event.team.DRTeamSpawnSetEvent;
import fr.aumgn.diamondrush.event.team.DRTotemBreakEvent;
import fr.aumgn.diamondrush.event.team.DRTotemSetEvent;
import fr.aumgn.diamondrush.exception.DiamondRushException;
import fr.aumgn.diamondrush.exception.NoStatistics;
import fr.aumgn.diamondrush.game.Game;
import fr.aumgn.diamondrush.game.GameStatistics;
import fr.aumgn.diamondrush.game.Team;
import fr.aumgn.diamondrush.listeners.GameListener;
import fr.aumgn.diamondrush.listeners.RegionsListener;
import fr.aumgn.diamondrush.listeners.SpectatorsListener;
import fr.aumgn.diamondrush.listeners.SpectatorsVisibilityListener;
import fr.aumgn.diamondrush.listeners.StatisticsListener;
import fr.aumgn.diamondrush.region.TeamSpawn;
import fr.aumgn.diamondrush.region.Totem;
import fr.aumgn.diamondrush.stage.JoinStage;
import fr.aumgn.diamondrush.stage.PauseStage;
import fr.aumgn.diamondrush.stage.Stage;
import fr.aumgn.diamondrush.stage.StartStage;
import fr.aumgn.diamondrush.views.GameView;

public final class DiamondRush {

    private final DiamondRushPlugin plugin;

    private DRConfig config;
    private PluginResourceBundle cmdMessages;
    private PluginResourceBundle messages;

    private GameStatistics statistics;

    private Game game;
    private Listener[] listeners;
    private Stage stage;
    private PlayersIdMap<List<Runnable>> onReconnect;

    public DiamondRush(DiamondRushPlugin plugin) {
        this.plugin = plugin;
        reloadConfig();
        PluginResourceBundles bundles = new PluginResourceBundles(plugin,
                Locale.FRANCE, plugin.getDataFolder());
        this.cmdMessages = bundles.get("commands");
        this.messages = bundles.get("messages");
        this.statistics = null;
        this.game = null;
        this.listeners = new Listener[5];
        this.stage = null;
        this.onReconnect = null;
    }

    public DiamondRushPlugin getPlugin() {
        return plugin;
    }

    public DRConfig getConfig() {
        return config;
    }

    public PluginResourceBundle getCmdMessages() {
        return cmdMessages;
    }

    public PluginResourceBundle getMessages() {
        return messages;
    }

    public GameStatistics getStatistics() {
        if (statistics == null) {
            throw new NoStatistics();
        }
        return statistics;
    }

    public boolean isRunning() {
        return game != null;
    }

    public Game getGame() {
        return game;
    }

    public Stage getStage() {
        return stage;
    }

    public void initGame(Game game, JoinStage stage) {
        this.statistics = new GameStatistics();
        this.game = game;
        PluginManager pm = Bukkit.getPluginManager();
        listeners[0] = new GameListener(this);
        listeners[1] = new RegionsListener(this);
        listeners[2] = new SpectatorsListener(this);
        listeners[3] = new SpectatorsVisibilityListener(this);
        listeners[4] = new StatisticsListener(this);
        for (Listener listener : listeners) {
            pm.registerEvents(listener, plugin);
        }
        this.onReconnect = new PlayersIdHashMap<List<Runnable>>();
        nextStage(stage);
    }

    public void reloadConfig() {
        this.config = plugin.loadDRConfig();
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
        if (stage == null) {
            throw new UnsupportedOperationException();
        }
        unregisterStageListeners();
        stage.stop();
        stage = ((PauseStage) stage).getOldStage();
        registerStageListeners();
        stage.resume();
    }

    private void forceStop() {
        for (Listener listener : stage.getListeners()) {
            HandlerList.unregisterAll(listener);
        }
        Bukkit.getScheduler().cancelTasks(plugin);

        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }
        game = null;
    }

    public void playerJoin(Team team, Player player) {
        if (!game.contains(player)) {
            DRPlayerJoinEvent event = new DRPlayerJoinEvent(game, team, player);
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

    public void playerQuit(Player player) {
        if (game.contains(player)) {
            DRPlayerQuitEvent event = new DRPlayerQuitEvent(game, player); 
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

    public void spectatorJoin(Player player) {
        DRSpectatorJoinEvent event = new DRSpectatorJoinEvent(game, player);
        if (!game.getSpectators().contains(player)) {
            Util.callEvent(event);
            if (!event.isCancelled()) {
                game.sendMessage(player.getDisplayName() + ChatColor.YELLOW +
                        " est maintenant spectateur.");
                game.getSpectators().add(player);
                player.sendMessage(ChatColor.GREEN + "Vous êtes maintenant spectateur.");
            }
        } else {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Vous êtes déjà spectateur.");
        }
    }

    public void spectatorQuit(Player spectator) {
        DRSpectatorQuitEvent event = new DRSpectatorQuitEvent(game, spectator);
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

    public void startGame() {
        if (!(stage instanceof JoinStage)) {
            throw new DiamondRushException("Impossible de démarrer " +
                    "une partie en dehors d'une phase de join.");
        }

        if (stage.hasNextStageScheduled()) {
            throw new DiamondRushException(
                    "La partie est déjà sur le point de démarrer.");
        }

        DRGameStartEvent event = new DRGameStartEvent(game);
        Util.callEvent(event);
        if (!event.isCancelled()) {
            game.sendMessage(ChatColor.GREEN + "La partie va commencer.");
            nextStage(new StartStage(this));
        }
    }

    public void gameWin(Team team) {
        DRGameWinEvent event = new DRGameWinEvent(game, team);
        Util.callEvent(event);
        Team winningTeam = game.getTeams().get(0);
        String msg = ChatColor.GREEN +
                "L'équipe " + winningTeam.getDisplayName() +
                ChatColor.GREEN + " a gagné la partie.";
        Util.broadcast(msg);
        GameView view = new GameView(this, false, false);
        for (String message : view) {
            Util.broadcast(message);
        }
        forceStop();
    }

    public void gameStop() {
        DRGameStopEvent event = new DRGameStopEvent(game);
        Util.callEvent(event);
        game.sendMessage(ChatColor.RED + "La partie a été arretée.");
        forceStop();
    }

    public boolean totemBreak(Team team, Totem totem, Player player) {
        DRTotemBreakEvent event = new DRTotemBreakEvent(game, team, totem, player);
        Util.callEvent(event);
        if (!event.isCancelled()) {
            team.decreaseLives();
            if (team.getLives() == 0) {
                Team responsible = game.getTeam(player);
                teamLoose(team, responsible);
            } else {
                game.sendMessage(ChatColor.YELLOW + "L'équipe " + team.getDisplayName() + 
                        ChatColor.YELLOW + " a perdu une vie. " + team.getLives() + " restantes.");
            }
        }
        return event.isCancelled();
    }

    public void teamLoose(Team team, Team responsible) {
        DRTeamLooseEvent event = new DRTeamLooseEvent(game, team, responsible);
        Util.callEvent(event);
        game.removeTeam(team);
        if (game.getTeams().size() == 1) {
            gameWin(team);
        } else {
            String msg = ChatColor.RED +"L'équipe " + team.getDisplayName() 
                    + ChatColor.RED + " a perdu la partie.";
            game.sendMessage(msg);
            team.sendMessage(msg);
            for (Player player : team.getPlayers()) {
                spectatorJoin(player);
            }
        }
    }

    public void totemSet(Team team, Totem totem) {
        DRTotemSetEvent event = new DRTotemSetEvent(game, team, totem);
        Util.callEvent(event);
        team.setTotem(totem);
        totem.create(game.getWorld(), team.getColor(), team.getLives());
    }

    public void teamSpawnSet(Team team, TeamSpawn spawn) {
        DRTeamSpawnSetEvent event = new DRTeamSpawnSetEvent(game, team, spawn); 
        Util.callEvent(event);
        team.setSpawn(spawn);
        team.getSpawn().create(game.getWorld(), team.getColor());
    }

    public void onReconnect(PlayerId player, Runnable runnable) {
        List<Runnable> list;
        if (onReconnect.containsKey(player)) {
            list = onReconnect.get(player);
        } else {
            list = new ArrayList<Runnable>();
            onReconnect.put(player, list);
        }

        list.add(runnable);
    }

    public void handleReconnect(Player player) {
        if (!onReconnect.containsKey(player)) {
            return;
        }

        for (Runnable runnable : onReconnect.get(player)) {
            runnable.run();
        }
        onReconnect.remove(player);
    }
}
