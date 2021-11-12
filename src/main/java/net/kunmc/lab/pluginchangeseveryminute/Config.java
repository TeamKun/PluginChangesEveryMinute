package net.kunmc.lab.pluginchangeseveryminute;

import net.kunmc.lab.configlib.config.BaseConfig;
import net.kunmc.lab.configlib.value.IntegerValue;
import net.kunmc.lab.configlib.value.StringListValue;
import net.kunmc.lab.configlib.value.StringValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Config extends BaseConfig {
    public IntegerValue intervalSecs = new IntegerValue(180);
    public IntegerValue remainingSecsToNext = new IntegerValue(intervalSecs.value());
    public StringValue currentPlugin = new StringValue("").writableByCommand(false);
    public StringListValue sequenceOfPlugins = new StringListValue(new ArrayList<>())
            .addableByCommand(false)
            .removableByCommand(false)
            .clearableByCommand(false);

    public Config(@NotNull Plugin plugin, @NotNull String entryName) {
        super(plugin, entryName);
    }

    public void writeSequenceOfPlugins(List<PluginProperty> pluginPropertyList) {
        sequenceOfPlugins.clear();
        sequenceOfPlugins.addAll(pluginPropertyList.stream()
                .map(x -> x.pluginName.value())
                .collect(Collectors.toList()));
    }
}
