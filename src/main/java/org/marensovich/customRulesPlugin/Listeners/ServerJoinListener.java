package org.marensovich.customRulesPlugin.Listeners;

import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.event.connection.configuration.AsyncPlayerConnectionConfigureEvent;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.marensovich.customRulesPlugin.CustomRulesPlugin;
import org.marensovich.customRulesPlugin.Dialogs.JoinDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
public class ServerJoinListener implements Listener {

    private final Map<PlayerCommonConnection, CompletableFuture<Boolean>> awaitingResponse = new HashMap<>();

    @EventHandler
    void onPlayerConfigure(AsyncPlayerConnectionConfigureEvent event) {

        if (!CustomRulesPlugin.getInstance().getConfig().getBoolean("display-every-join")){
            if (Bukkit.getOfflinePlayer(event.getConnection().getProfile().getId()).hasPlayedBefore()){
                return;
            }
        }

        CompletableFuture<Boolean> response = new CompletableFuture<>();
        awaitingResponse.put(event.getConnection(), response);
        event.getConnection().getAudience().showDialog(JoinDialog.getDialog());
        if (!response.join()) {
            event.getConnection().disconnect(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(
                            Objects.requireNonNull(CustomRulesPlugin.getInstance().getConfig().getString("no-rules"))
                    )
            );
        }
        awaitingResponse.remove(event.getConnection());
    }

    @EventHandler
    void onHandleDialog(PlayerCustomClickEvent event) {
        Key key = event.getIdentifier();

        if (key.equals(Key.key("customrules:deny"))) {
            setConnectionJoinResult(event.getCommonConnection(), false);
        } else if (key.equals(Key.key("customrules:accept"))) {
            setConnectionJoinResult(event.getCommonConnection(), true);
        }
    }

    private void setConnectionJoinResult(PlayerCommonConnection connection, boolean value) {
        CompletableFuture<Boolean> future = awaitingResponse.get(connection);
        if (future != null) {
            future.complete(value);
        }
    }
}