package net.kunmc.lab.paperplugintemplate;

import net.kunmc.lab.config.BaseConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class PaperPluginTemplate extends JavaPlugin {
    final List<PluginProperty> pluginPropertyList = new ArrayList<>();

    @Override
    public void onEnable() {
        for (File file : getDataFolder().listFiles()) {
            pluginPropertyList.add(BaseConfig.newInstanceFrom(file, PluginProperty.class, this));
        }
    }

    @Override
    public void onDisable() {
    }
}
