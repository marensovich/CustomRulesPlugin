package org.marensovich.customRulesPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.marensovich.customRulesPlugin.Commands.RulesCommand;
import org.marensovich.customRulesPlugin.Listeners.ServerJoinListener;


public final class CustomRulesPlugin extends JavaPlugin {

    private static CustomRulesPlugin instance;

    private FileConfiguration config;

    CustomRulesPlugin(){
        instance = this;
    }

    public static CustomRulesPlugin getInstance() {
        return instance;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.config = super.getConfig();
    }


    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        getLogger().info("CustomRules plugin enabled");

        registerCommands();
        registerListeners();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ServerJoinListener(), this);
    }

    private void registerCommands() {
        org.bukkit.command.Command existingCommand = getServer().getCommandMap().getCommand("rules");
        if (existingCommand != null) {
            existingCommand.unregister(getServer().getCommandMap());
        }

        RulesCommand rulesCommand = new RulesCommand();
        getServer().getCommandMap().register("customrules", rulesCommand);

        getLogger().info("Команда /rules успешно зарегистрирована");
    }


    @Override
    public void onDisable() {
        getLogger().info("CustomRules plugin disabled");
    }
}
