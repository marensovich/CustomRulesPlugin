package org.marensovich.customRulesPlugin;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.marensovich.customRulesPlugin.Commands.RulesCommand;
import org.marensovich.customRulesPlugin.Listeners.ServerJoinListener;

import java.io.File;
import java.io.IOException;

public final class CustomRulesPlugin extends JavaPlugin {

    private static CustomRulesPlugin instance;

    private FileConfiguration config;
    private File rulesFile;
    private FileConfiguration rulesConfig;

    public static CustomRulesPlugin getInstance() {
        return instance;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getRulesConfig() {
        return rulesConfig;
    }


    @Override
    public void reloadConfig() {
        if (rulesFile == null) {
            rulesFile = new File(getDataFolder(), "rules.yml");
        }
        if (!rulesFile.exists()) {
            saveResource("rules.yml", false);
        }
        rulesConfig = new YamlConfiguration();
        try {
            rulesConfig.load(rulesFile);
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().severe("Не удалось загрузить rules.yml!");
            e.printStackTrace();
        }

        super.reloadConfig();
        this.config = super.getConfig();
    }

    @Override
    public void onEnable() {
        instance = this;
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
        getServer().getCommandMap().register("rules", rulesCommand);

        getLogger().info("Команда /rules успешно зарегистрирована");
    }


    @Override
    public void onDisable() {
        getLogger().info("CustomRules plugin disabled");
        instance = null;
    }

}
