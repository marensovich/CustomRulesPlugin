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
import java.util.Objects;

public class JoinDialog {

    public static Dialog getDialog() {
        FileConfiguration rules = CustomRulesPlugin.getInstance().getRulesConfig();

        String titleText = rules.getString("title.text");
        String titleColor = rules.getString("title.color");
        Component title = Component.text(Objects.requireNonNull(titleText), TextColor.fromHexString(Objects.requireNonNull(titleColor)));

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

        String acceptText = rules.getString("buttons.accept.text");
        String acceptColor = rules.getString("buttons.accept.color");
        String denyText = rules.getString("buttons.deny.text");
        String denyColor = rules.getString("buttons.deny.color");

        ActionButton acceptButton = ActionButton.builder(
                        Component.text(Objects.requireNonNull(acceptText), TextColor.fromHexString(Objects.requireNonNull(acceptColor)))
                )
                .action(DialogAction.customClick(Key.key("customrules:accept"), null))
                .build();

        ActionButton denyButton = ActionButton.builder(
                        Component.text(Objects.requireNonNull(denyText), TextColor.fromHexString(Objects.requireNonNull(denyColor)))
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
