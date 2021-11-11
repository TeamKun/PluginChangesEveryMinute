package net.kunmc.lab.pluginchangeseveryminute;

import dev.kotx.flylib.FlyLib;
import net.kunmc.lab.command.ConfigCommand;
import net.kunmc.lab.command.ConfigCommandBuilder;
import net.kunmc.lab.config.BaseConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class PluginChangesEveryMinute extends JavaPlugin {
    public static Game game;

    @Override
    public void onEnable() {
        List<PluginProperty> pluginPropertyList = new ArrayList<>();
        for (File file : getDataFolder().listFiles()) {
            pluginPropertyList.add(BaseConfig.newInstanceFrom(file, PluginProperty.class, this));
        }

        Config config = new Config(this, "gameConfig");
        game = new Game(pluginPropertyList, config, this);

        ConfigCommand configCommand = new ConfigCommandBuilder(config)
                .disableReloadCommand()
                .build();

        FlyLib.create(this, builder -> {
            builder.command(new MainCommand("pluginchanges", configCommand));
        });
    }

    @Override
    public void onDisable() {
    }
}
