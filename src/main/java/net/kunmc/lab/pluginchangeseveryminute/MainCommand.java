package net.kunmc.lab.pluginchangeseveryminute;

import dev.kotx.flylib.command.Command;
import net.kunmc.lab.command.ConfigCommand;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends Command {
    public MainCommand(@NotNull String name, ConfigCommand configCommand) {
        super(name);
        children(new StartCommand(), new StopCommand(), new ShuffleCommand(), configCommand);
    }
}
