package org.marensovich.customRulesPlugin.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.marensovich.customRulesPlugin.CustomRulesPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RulesCommand extends BukkitCommand {

    public RulesCommand() {
        super("rules");
        this.setDescription("Управление правилами плагина.");
        this.setUsage("/rules <reload>");
        this.setPermission("customrules.use");
        this.setPermissionMessage(ChatColor.translateAlternateColorCodes('&',
                Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("plugin-prefix"))) +
                ChatColor.translateAlternateColorCodes('&',
                        Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("no-permission"))));
        this.setAliases(List.of("rules"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!testPermission(sender)) {
            return true;
        }
        if (args.length == 0) {
            if (!sender.hasPermission("customrules.use")){
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("plugin-prefix"))) +
                                ChatColor.translateAlternateColorCodes('&',
                                        Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("no-permission"))));
                return false;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("plugin-prefix"))) +
                    ChatColor.translateAlternateColorCodes('&',
                    Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("info"))));
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("customrules.reload")) {
                try {
                    CustomRulesPlugin.getInstance().reloadConfig();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("plugin-prefix"))) +
                            ChatColor.translateAlternateColorCodes('&',
                            Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("reload-success"))));
                    return true;
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("plugin-prefix"))) +
                            ChatColor.translateAlternateColorCodes('&',
                            Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("reload-error"))));
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("plugin-prefix"))) +
                        ChatColor.translateAlternateColorCodes('&',
                        Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("no-permission"))));
                return false;
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("plugin-prefix"))) +
                    ChatColor.translateAlternateColorCodes('&',
                            Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("wrong-command"))));
            return false;
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("customrules.reload")) {
                String input = args[0].toLowerCase();
                if ("reload".startsWith(input)) {
                    completions.add("reload");
                }
            }
        }
        return completions;
    }
}