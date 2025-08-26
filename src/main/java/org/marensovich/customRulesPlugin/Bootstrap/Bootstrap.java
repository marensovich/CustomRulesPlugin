package org.marensovich.customRulesPlugin.Bootstrap;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import org.marensovich.customRulesPlugin.Dialogs.JoinDialog;

public class Bootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
        JoinDialog.registerDialog(context);
    }
}