package fr.aumgn.tobenamed;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class TBNPlugin extends JavaPlugin {

    public void onEnable() {
        TBN.init(this);
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
            config = gson.fromJson(reader, TBNConfig.class);
            reader.close();
        } else {
            config = new TBNConfig();
            file.createNewFile();
        }
        // This ensures user file is updated with newer fields. 
        JsonWriter writer = new JsonWriter(new FileWriter(file));
        gson.toJson(config, TBNConfig.class, writer);
        writer.close();

        return config;
    }
}
