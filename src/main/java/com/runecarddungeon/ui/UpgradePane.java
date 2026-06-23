
package com.runecarddungeon.ui;

import com.runecarddungeon.data.UpgradeManager;
import com.runecarddungeon.model.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class UpgradePane extends VBox {

    public UpgradePane(Player player, int level, Runnable onUpgradeComplete) {
        setAlignment(Pos.CENTER);
        setSpacing(20);
        setStyle("-fx-padding: 40; -fx-background-color: #1a1a2e;");

        UpgradeManager um = UpgradeManager.getInstance();

        Label titleLabel = new Label("🌟 强化选择");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #ffd700; -fx-font-weight: bold;");

        Label descLabel = new Label("选择一项强化，继续冒险！");
        descLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #cccccc;");

        VBox optionsBox = new VBox(15);
        optionsBox.setAlignment(Pos.CENTER);

        if (level == 2) {
            // 第2关强化选项
            Button optionA = new Button("⚔️ 攻击卡强化：伤害 8 → 12");
            optionA.setStyle("-fx-font-size: 16px; -fx-padding: 15 30; -fx-background-color: #ff6b6b; -fx-text-fill: white;");
            optionA.setOnAction(e -> {
                um.applyUpgradeLevel2(player, true);
                onUpgradeComplete.run();
            });

            Button optionB = new Button("❤️ 最大生命值 +15");
            optionB.setStyle("-fx-font-size: 16px; -fx-padding: 15 30; -fx-background-color: #51cf66; -fx-text-fill: white;");
            optionB.setOnAction(e -> {
                um.applyUpgradeLevel2(player, false);
                onUpgradeComplete.run();
            });

            optionsBox.getChildren().addAll(optionA, optionB);

        } else if (level == 4) {
            // 第4关强化选项
            Button optionA = new Button("💀 削弱卡强化：效果 6 → 10");
            optionA.setStyle("-fx-font-size: 16px; -fx-padding: 15 30; -fx-background-color: #cc5de8; -fx-text-fill: white;");
            optionA.setOnAction(e -> {
                um.applyUpgradeLevel4(player, true);
                onUpgradeComplete.run();
            });

            Button optionB = new Button("🔮 每回合蓝量 +1");
            optionB.setStyle("-fx-font-size: 16px; -fx-padding: 15 30; -fx-background-color: #4dabf7; -fx-text-fill: white;");
            optionB.setOnAction(e -> {
                um.applyUpgradeLevel4(player, false);
                onUpgradeComplete.run();
            });

            optionsBox.getChildren().addAll(optionA, optionB);
        }

        getChildren().addAll(titleLabel, descLabel, optionsBox);
    }
}
