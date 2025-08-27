package org.marensovich.customRulesPlugin.Dialogs;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.marensovich.customRulesPlugin.CustomRulesPlugin;

import java.util.ArrayList;
import java.util.List;

public class JoinDialog {

    public static Dialog getDialog() {
        FileConfiguration rules = CustomRulesPlugin.getInstance().getRulesConfig();

        String titleText = rules.getString("title.text", "Правила сервера");
        String titleColor = rules.getString("title.color", "#FFFFFF");
        Component title = Component.text(titleText, TextColor.fromHexString(titleColor));

        List<DialogBody> bodyLines = new ArrayList<>();
        rules.getConfigurationSection("body").getKeys(false).forEach(blockKey -> {
            Object textObj = rules.get("body." + blockKey + ".text");
            String color = rules.getString("body." + blockKey + ".color", "#FFFFFF");

            if (textObj instanceof String) {
                bodyLines.add(DialogBody.plainMessage(
                        Component.text((String) textObj, TextColor.fromHexString(color))
                ));
            } else if (textObj instanceof List<?>) {
                for (Object line : (List<?>) textObj) {
                    if (line instanceof String) {
                        bodyLines.add(DialogBody.plainMessage(
                                Component.text((String) line, TextColor.fromHexString(color))
                        ));
                    }
                }
            }
        });

        String acceptText = rules.getString("buttons.accept.text", "✓ Принять");
        String acceptColor = rules.getString("buttons.accept.color", "#00FF00");
        String denyText = rules.getString("buttons.deny.text", "✗ Отклонить");
        String denyColor = rules.getString("buttons.deny.color", "#FF0000");

        ActionButton acceptButton = ActionButton.builder(
                        Component.text(acceptText, TextColor.fromHexString(acceptColor))
                )
                .action(DialogAction.customClick(Key.key("customrules:accept"), null))
                .build();

        ActionButton denyButton = ActionButton.builder(
                        Component.text(denyText, TextColor.fromHexString(denyColor))
                )
                .action(DialogAction.customClick(Key.key("customrules:deny"), null))
                .build();

        DialogBase base = DialogBase.builder(title)
                .canCloseWithEscape(false)
                .body(bodyLines)
                .build();

        return Dialog.create(builder -> builder.empty()
                .base(base)
                .type(DialogType.confirmation(acceptButton, denyButton))
        );
    }
}
