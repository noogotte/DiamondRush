package fr.aumgn.diamondrush;

import org.bukkit.plugin.java.JavaPlugin;

import fr.aumgn.bukkitutils.command.CommandsRegistration;
import fr.aumgn.bukkitutils.command.messages.FrenchMessages;
import fr.aumgn.bukkitutils.gconf.GConfLoadException;
import fr.aumgn.bukkitutils.gconf.GConfLoader;
import fr.aumgn.diamondrush.command.GeneralCommands;
import fr.aumgn.diamondrush.command.InfoCommands;
import fr.aumgn.diamondrush.command.JoinStageCommands;
import fr.aumgn.diamondrush.command.SpectatorsCommands;
import fr.aumgn.diamondrush.config.DRConfig;

public class DiamondRushPlugin extends JavaPlugin {

    public void onEnable() {
        DiamondRush.init(this);

        CommandsRegistration commandsManager = new CommandsRegistration(this, new FrenchMessages()); 
        commandsManager.register(new GeneralCommands());
        commandsManager.register(new InfoCommands());
        commandsManager.register(new JoinStageCommands());
        commandsManager.register(new SpectatorsCommands());

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
            getLogger().warning("Impossible de charger le fichier de configuration.");
            getLogger().warning("Utilisation des valeurs par défaut.");
            return new DRConfig();
        }
    }
}
