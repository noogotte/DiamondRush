package fr.aumgn.tobenamed;

import org.bukkit.plugin.java.JavaPlugin;

public class TBNPlugin extends JavaPlugin {

    public void onEnable() {
        TBN.init(this);
        getLogger().info("Enabled.");
    }

    public void onDisable() {
        getLogger().info("Disabled.");
    }
}
