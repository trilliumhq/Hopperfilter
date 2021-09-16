package me.trilliumhq.filter.hopperitemframefilter;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class HopperItemFrameFilter extends JavaPlugin {
    private static HopperItemFrameFilter instance;

    public HopperItemFrameFilter() {
    }

    public void onEnable() {
        instance = this;
        this.getLogger().info("HopperItemFrameFilter enabled");
        this.registerListeners();
    }

    public void onDisable() {
        instance = null;
    }

    private void registerListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new EventListener(), this);
    }

    public static HopperItemFrameFilter getInstance() {
        return instance;
    }
}

