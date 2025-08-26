package org.marensovich.customRulesPlugin;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.marensovich.customRulesPlugin.Commands.RulesCommand;
import org.marensovich.customRulesPlugin.Listeners.ServerJoinListener;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class CustomRulesPlugin extends JavaPlugin {

    private static CustomRulesPlugin instance;
    private FileConfiguration rulesConfiguration;
    private File rulesConfigFile;

    public static CustomRulesPlugin getInstance() {
        return instance;
    }

    public FileConfiguration getRulesConfiguration() {
        return rulesConfiguration;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        reloadConfig();

        createRulesConfiguration();

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

    }

    private void createRulesConfiguration() {
        rulesConfigFile = new File(getDataFolder(), "rules.yml");

        if (!rulesConfigFile.exists()) {
            saveResource("rules.yml", false);
        }

        rulesConfiguration = new YamlConfiguration();
        try {
            rulesConfiguration.load(rulesConfigFile);
            getLogger().info("rules.yml успешно загружен");
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().severe("Не удалось загрузить rules.yml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void reloadRulesConfig() {
        if (rulesConfigFile != null && rulesConfigFile.exists()) {
            try {
                rulesConfiguration.load(rulesConfigFile);
                getLogger().info("rules.yml перезагружен");
            } catch (IOException | InvalidConfigurationException e) {
                getLogger().severe("Не удалось перезагрузить rules.yml: " + e.getMessage());
            }
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomRules plugin disabled");
        instance = null;
    }
}