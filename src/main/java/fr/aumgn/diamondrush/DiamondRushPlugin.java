package fr.aumgn.diamondrush;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.aumgn.bukkitutils.command.CommandsRegistration;
import fr.aumgn.bukkitutils.command.messages.FrenchMessages;
import fr.aumgn.bukkitutils.gconf.GConfLoadException;
import fr.aumgn.bukkitutils.gconf.GConfLoader;
import fr.aumgn.diamondrush.command.AdminCommands;
import fr.aumgn.diamondrush.command.GameCommands;
import fr.aumgn.diamondrush.command.InfoCommands;
import fr.aumgn.diamondrush.command.PlayerCommands;
import fr.aumgn.diamondrush.command.SpectatorsCommands;
import fr.aumgn.diamondrush.config.DRConfig;

public class DiamondRushPlugin extends JavaPlugin {

    private static final double GSON_VERSION = 0.0;

    public void onEnable() {
        DiamondRush diamondRush = new DiamondRush(this);
        
        CommandsRegistration commandsRegistration = new CommandsRegistration(
                this, new FrenchMessages());
        commandsRegistration.register(new AdminCommands(diamondRush));
        commandsRegistration.register(new GameCommands(diamondRush));
        commandsRegistration.register(new SpectatorsCommands(diamondRush));
        commandsRegistration.register(new PlayerCommands(diamondRush));
        commandsRegistration.register(new InfoCommands(diamondRush));

        getLogger().info("Enabled.");
    }

    public Gson gson() {
        return new GsonBuilder()
            .setVersion(GSON_VERSION)
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
            .setPrettyPrinting()
            .create();
    }

    public void onDisable() {
        getLogger().info("Disabled.");
    }

    public DRConfig loadDRConfig() {
        try {
            GConfLoader loader = new GConfLoader(gson(), this);
            return loader.loadOrCreate("config.json", DRConfig.class);
        } catch (GConfLoadException exc) {
            getLogger().warning(
                    "Impossible de charger le fichier de configuration.");
            getLogger().warning("Utilisation des valeurs par défaut.");
            return new DRConfig();
        }
    }
}
