package fr.aumgn.tobenamed;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.aumgn.tobenamed.game.Game;
import fr.aumgn.tobenamed.stage.Stage;

public final class TBN {

    private static final double GSON_VERSION = 0.0;

    private static TBNPlugin plugin;
    private static Random random;
    private static Game game;
    private static Stage stage;
    private static TBNConfig config;

    private TBN() {
    }

    public static void init(TBNPlugin plugin) {
        if (TBN.plugin != null) {
            throw new UnsupportedOperationException();
        }
        TBN.plugin = plugin;
        TBN.random = new Random();
        TBN.stage = null;
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
        return game;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void nextStage(Stage newStage) {
        if (stage != null) {
            for (Listener listener : stage.getListeners()) {
                HandlerList.unregisterAll(listener);
            }
            stage.stop();
        }
        stage = newStage;
        if (newStage != null) {
            for (Listener listener : stage.getListeners()) {
                Bukkit.getPluginManager().registerEvents(listener, plugin);
            }
            stage.start();
        }
    }

}
