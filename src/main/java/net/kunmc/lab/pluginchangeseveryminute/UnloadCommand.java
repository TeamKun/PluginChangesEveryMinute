package net.kunmc.lab.pluginchangeseveryminute;

import dev.kotx.flylib.command.Command;
import dev.kotx.flylib.command.CommandContext;

public class UnloadCommand extends Command {
    public UnloadCommand() {
        super("unload");
    }

    @Override
    public void execute(CommandContext ctx) {
        PluginChangesEveryMinute.game.pluginProperties.forEach(p -> {
            try {
                p.disablePlugin();
            } catch (Exception ignored) {
            }
        });
        ctx.success("PluginChangesで使用されるプラグインをアンロードしました.");
    }
}
