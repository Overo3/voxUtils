package io.github.overo3.voxUtils;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

@SuppressWarnings("UnstableApiUsage")
public class VoxUtilsBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            UnbreakableCommand unbreakableCommand = new UnbreakableCommand();
            commands.registrar().register(unbreakableCommand.commandStack.build());
        } );
    }
}
