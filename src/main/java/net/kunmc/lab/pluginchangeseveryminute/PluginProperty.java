package net.kunmc.lab.pluginchangeseveryminute;

import com.rylinaux.plugman.util.PluginUtil;
import net.kunmc.lab.configlib.config.BaseConfig;
import net.kunmc.lab.configlib.value.StringListValue;
import net.kunmc.lab.configlib.value.StringValue;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PluginProperty extends BaseConfig {
    public final StringValue pluginName;
    public final StringValue projectName;
    public final StringValue explanation;
    private final StringListValue startUpCommands;

    public PluginProperty(Plugin plugin, String pluginName, String projectName, String explanation, List<String> startUpCommands) {
        super(plugin, pluginName);
        this.pluginName = new StringValue(pluginName)
                .writableByCommand(false)
                .listable(false);
        this.projectName = new StringValue(projectName)
                .writableByCommand(false);
        this.explanation = new StringValue(explanation)
                .writableByCommand(false);
        this.startUpCommands = new StringListValue(startUpCommands)
                .addableByCommand(false)
                .removableByCommand(false)
                .clearableByCommand(false);
    }

    public boolean pluginExists() {
        return getPlugin() != null;
    }

    @Nullable
    public Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin(pluginName.value());
    }

    public boolean pluginEnabled() {
        if (pluginExists()) {
            return getPlugin().isEnabled();
        }

        return false;
    }

    public void disablePlugin() {
        if (pluginExists()) {
            PluginUtil.unload(getPlugin());
        }
    }

    public void enablePlugin() {
        if (pluginExists()) {
            PluginUtil.load(pluginName.value());
        }
    }

    public void dispatchCommands() {
        for (String command : startUpCommands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
}
