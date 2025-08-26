package org.marensovich.customRulesPlugin.Dialogs;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.DialogKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;

public class JoinDialog {

    public static void registerDialog(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(RegistryEvents.DIALOG.compose(),
                event -> event.registry().register(
                        DialogKeys.create(Key.key("customrules:accept_rules")),
                        builder -> builder
                                .base(DialogBase.builder(Component.text("Принятие правил сервера", NamedTextColor.LIGHT_PURPLE))
                                        .canCloseWithEscape(false)
                                        .body(List.of(
                                                DialogBody.plainMessage(Component.text("Добро пожаловать на сервер!")),
                                                DialogBody.plainMessage(Component.text("Для игры на нашем сервере вы должны принять правила.")),
                                                DialogBody.plainMessage(Component.text("Нажмите 'Принять', чтобы согласиться с правилами."))
                                        ))
                                        .build()
                                )
                                .type(DialogType.confirmation(
                                        ActionButton.builder(Component.text("✓ Принять правила", TextColor.color(0x00FF00)))
                                                .tooltip(Component.text("Нажмите чтобы принять правила"))
                                                .action(DialogAction.customClick(Key.key("customrules:accept"), null))
                                                .build(),
                                        ActionButton.builder(Component.text("✗ Отказаться", TextColor.color(0xFF0000)))
                                                .tooltip(Component.text("Нажмите если не согласны с правилами"))
                                                .action(DialogAction.customClick(Key.key("customrules:deny"), null))
                                                .build()
                                ))
                )
        );
    }
}