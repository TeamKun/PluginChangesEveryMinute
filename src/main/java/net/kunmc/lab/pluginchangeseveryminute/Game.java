package net.kunmc.lab.pluginchangeseveryminute;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private final List<PluginProperty> pluginProperties;
    private final Config config;
    private final Plugin plugin;
    private PluginProperty currentPlugin = null;
    private BukkitTask mainTask;
    private boolean isRunning = false;
    private int pluginIndex = -1;

    public Game(List<PluginProperty> pluginPropertyList, Config config, Plugin plugin) {
        this.pluginProperties = pluginPropertyList.stream()
                .filter(PluginProperty::pluginExists)
                .collect(Collectors.toList());
        config.writeSequenceOfPlugins(pluginProperties);
        this.config = config;
        this.plugin = plugin;

        new BukkitRunnable() {
            @Override
            public void run() {
                pluginProperties.forEach(PluginProperty::disablePlugin);
            }
        }.runTaskLater(plugin, 1);
    }

    public boolean start() {
        if (isRunning) {
            return false;
        }

        isRunning = true;
        config.remainingSecsToNext.value(config.intervalSecs.value());
        mainTask = new ProceedTask().runTaskTimer(plugin, 20, 20);

        return true;
    }

    public boolean stop() {
        if (!isRunning) {
            return false;
        }

        isRunning = false;
        currentPlugin.disablePlugin();
        currentPlugin = null;
        config.currentPlugin.value("");
        config.remainingSecsToNext.value(config.intervalSecs.value());
        pluginIndex = -1;
        mainTask.cancel();

        return true;
    }

    public void next() {
        pluginIndex++;
        if (pluginIndex >= pluginProperties.size()) {
            pluginIndex = 0;
        }

        if (currentPlugin != null) {
            currentPlugin.disablePlugin();
        }

        currentPlugin = pluginProperties.get(pluginIndex);
        currentPlugin.enablePlugin();
        currentPlugin.dispatchCommands();

        config.currentPlugin.value(currentPlugin.pluginName.value());
        config.remainingSecsToNext.value(config.intervalSecs.value());
    }

    public boolean shuffle() {
        if (isRunning) {
            return false;
        }

        Collections.shuffle(pluginProperties);
        config.writeSequenceOfPlugins(pluginProperties);
        return true;
    }

    private class ProceedTask extends BukkitRunnable {
        public ProceedTask() {
            next();
        }

        @Override
        public void run() {
            int remain = config.remainingSecsToNext.value() - 1;
            config.remainingSecsToNext.value(remain);
            if (remain <= 0) {
                next();
            }
        }
    }
}
