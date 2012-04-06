package fr.aumgn.tobenamed;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.aumgn.tobenamed.exception.NoGameRunning;
import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.stage.JoinStage;

public final class TBN {

    private static final double GSON_VERSION = 0.0;

    private static TBNPlugin plugin;
    private static Random random;
    private static Game game;
    private static TBNConfig config;

    private TBN() {
    }

    public static void init(TBNPlugin plugin) {
        if (TBN.plugin != null) {
            throw new UnsupportedOperationException();
        }
        TBN.plugin = plugin;
        TBN.random = new Random();
        TBN.game = null;
        reloadConfig();
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static Random getRandom() {
        return random;
    }

    public static Gson getGson() {
        return new GsonBuilder()
            .setVersion(GSON_VERSION)
            .setPrettyPrinting()
            .create();
    }

    public static void reloadConfig() {
        try {
            TBN.config = plugin.loadTBNConfig();
        } catch (IOException exc) {
            plugin.getLogger().warning("Impossible de charger le fichier de configuration.");
            plugin.getLogger().warning("Utilisation des valeurs par défaut.");
            TBN.config = new TBNConfig();
        }
    }

    public TBNConfig getConfig() {
        return config;
    }

    public static boolean isRunning() {
        return game != null;
    }

    public static Game getGame() {
        if (!isRunning()) {
            throw new NoGameRunning();
        }
        return game;
    }

    public static void initGame(Game game, JoinStage stage) {
        TBN.game = game;
        game.nextStage(stage);
    }

    public static void forceStop() {
        for (Listener listener : game.getStage().getListeners()) {
            HandlerList.unregisterAll(listener);
        }
        Bukkit.getScheduler().cancelTasks(TBN.getPlugin());
        TBN.game = null;
    }
}
