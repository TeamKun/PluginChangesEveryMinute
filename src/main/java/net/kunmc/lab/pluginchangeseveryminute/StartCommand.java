package net.kunmc.lab.pluginchangeseveryminute;

import dev.kotx.flylib.command.Command;
import dev.kotx.flylib.command.CommandContext;

public class StartCommand extends Command {
    public StartCommand() {
        super("start");
    }

    @Override
    public void execute(CommandContext ctx) {
        if (PluginChangesEveryMinute.game.start()) {
            ctx.success("PluginChangesをスタートしました.");
        } else {
            ctx.fail("すでにPluginChangesは実行中です.");
        }
    }
}
