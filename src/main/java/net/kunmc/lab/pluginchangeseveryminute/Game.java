package net.kunmc.lab.pluginchangeseveryminute;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    public final List<PluginProperty> pluginProperties;
    private final Config config;
    private final Plugin plugin;
    private PluginProperty currentPlugin = null;
    private BukkitTask mainTask;
    private BossBar bossBar;
    private boolean isRunning = false;
    private int pluginIndex = -1;

    public Game(List<PluginProperty> pluginPropertyList, Config config, Plugin plugin) {
        this.pluginProperties = pluginPropertyList.stream()
                .filter(PluginProperty::pluginExists)
                .collect(Collectors.toList());
        config.writeSequenceOfPlugins(pluginProperties);
        this.config = config;
        this.plugin = plugin;
    }

    public boolean start() {
        if (isRunning) {
            return false;
        }

        isRunning = true;
        config.remainingSecsToNext.value(config.intervalSecs.value());
        bossBar = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID);
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
        bossBar.removeAll();

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

        bossBar.setTitle(currentPlugin.projectName.value());

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendTitle(currentPlugin.projectName.value(), "", 20, 100, 20);
            Bukkit.broadcast(Component.text(ChatColor.YELLOW + "プラグインの説明: " + currentPlugin.explanation.value()));
        });

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

            bossBar.setProgress(Math.min((double) remain / config.intervalSecs.value(), 1.0));
            Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);

            if (remain <= 0) {
                next();
            }
        }
    }
}
