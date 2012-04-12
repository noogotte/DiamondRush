package fr.aumgn.tobenamed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import fr.aumgn.bukkit.command.CommandsManager;
import fr.aumgn.tobenamed.command.GeneralCommands;
import fr.aumgn.tobenamed.command.InfoCommands;
import fr.aumgn.tobenamed.command.JoinStageCommands;
import fr.aumgn.tobenamed.command.SpectatorsCommands;
import fr.aumgn.tobenamed.config.TBNConfig;

public class TBNPlugin extends JavaPlugin {

    public void onEnable() {
        TBN.init(this);

        CommandsManager commandsManager = new CommandsManager(this); 
        commandsManager.register(new GeneralCommands());
        commandsManager.register(new InfoCommands());
        commandsManager.register(new JoinStageCommands());
        commandsManager.register(new SpectatorsCommands());

        getLogger().info("Enabled.");
    }

    public void onDisable() {
        getLogger().info("Disabled.");
    }

    public TBNConfig loadTBNConfig() throws IOException {
        File folder = getDataFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(getDataFolder(), "config.json");
        Gson gson = TBN.getGson();
        TBNConfig config;
        if (file.exists()) {
            JsonReader reader = new JsonReader(new FileReader(file));
            try {
                config = gson.fromJson(reader, TBNConfig.class);
            } finally {
                reader.close();
            }
        } else {
            config = new TBNConfig();
            file.createNewFile();
        }
        // This ensures user file is updated with newer fields. 
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        try {
            writer.write(gson.toJson(config, TBNConfig.class));
        } finally {
            writer.close();
        }

        return config;
    }
}
