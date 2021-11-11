package net.kunmc.lab.pluginchangeseveryminute;

import dev.kotx.flylib.command.Command;
import dev.kotx.flylib.command.CommandContext;

public class StopCommand extends Command {
    public StopCommand() {
        super("stop");
    }

    @Override
    public void execute(CommandContext ctx) {
        if (PluginChangesEveryMinute.game.stop()) {
            ctx.success("PluginChangesを停止しました.");
        } else {
            ctx.fail("PluginChangesは実行されていません.");
        }
    }
}
