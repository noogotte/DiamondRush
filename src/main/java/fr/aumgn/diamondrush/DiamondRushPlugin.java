package fr.aumgn.diamondrush;

import org.bukkit.plugin.java.JavaPlugin;

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

    public void onEnable() {
        DiamondRush.init(this);

        CommandsRegistration commandsRegistration = new CommandsRegistration(
                this, new FrenchMessages());
        commandsRegistration.register(new AdminCommands());
        commandsRegistration.register(new GameCommands());
        commandsRegistration.register(new SpectatorsCommands());
        commandsRegistration.register(new PlayerCommands());
        commandsRegistration.register(new InfoCommands());

        getLogger().info("Enabled.");
    }

    public void onDisable() {
        getLogger().info("Disabled.");
    }

    public DRConfig loadTBNConfig() {
        try {
            GConfLoader loader = new GConfLoader(DiamondRush.getGson(), this);
            return loader.loadOrCreate("config.json", DRConfig.class);
        } catch (GConfLoadException exc) {
            getLogger().warning(
                    "Impossible de charger le fichier de configuration.");
            getLogger().warning("Utilisation des valeurs par défaut.");
            return new DRConfig();
        }
    }
}
