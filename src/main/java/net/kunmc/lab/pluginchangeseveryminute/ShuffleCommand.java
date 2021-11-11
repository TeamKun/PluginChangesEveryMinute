package net.kunmc.lab.pluginchangeseveryminute;

import dev.kotx.flylib.command.Command;
import dev.kotx.flylib.command.CommandContext;

public class ShuffleCommand extends Command {
    public ShuffleCommand() {
        super("shuffle");
    }

    @Override
    public void execute(CommandContext ctx) {
        if (PluginChangesEveryMinute.game.shuffle()) {
            ctx.success("プラグインの順番をシャッフルしました.");
        } else {
            ctx.fail("実行中はシャッフルできません.");
        }
    }
}
