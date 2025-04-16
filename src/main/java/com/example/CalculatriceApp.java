package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Stack;

public class CalculatriceApp extends Application {

    private TextField display = new TextField();
    private ListView<String> historyView = new ListView<>();
    private StringBuilder currentInput = new StringBuilder();
    private boolean darkMode = true;

    @Override
    public void start(Stage primaryStage) {
        display.setFont(Font.font(24));
        display.setEditable(false);
        display.setStyle("-fx-control-inner-background: #1e1e1e; -fx-text-fill: white;");

        GridPane buttons = createButtonGrid();
        VBox historyBox = new VBox(new Label("Historique"), historyView);
        historyBox.setPadding(new Insets(10));

        ToggleButton themeToggle = new ToggleButton("🌙 Thème sombre");
        themeToggle.setSelected(true);
        themeToggle.setOnAction(e -> toggleTheme());

        VBox root = new VBox(10, display, buttons, themeToggle, historyBox);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2e2e2e;");

        Scene scene = new Scene(root, 400, 600);
        scene.setOnKeyPressed(this::handleKeyPress);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Calculatrice JavaFX");
        primaryStage.show();
    }

    private GridPane createButtonGrid() {
        String[][] labels = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "*"},
                {"1", "2", "3", "-"},
                {"0", ".", "=", "+"},
                {"sin", "cos", "√", "C"},
        };

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        for (int row = 0; row < labels.length; row++) {
            for (int col = 0; col < labels[row].length; col++) {
                String label = labels[row][col];
                Button btn = new Button(label);
                btn.setPrefSize(60, 60);
                btn.setFont(Font.font(18));
                btn.setOnAction(e -> handleInput(label));
                grid.add(btn, col, row);
            }
        }
        return grid;
    }

    private void handleInput(String input) {
        switch (input) {
            case "=" -> evaluate();
            case "C" -> {
                currentInput.setLength(0);
                display.clear();
            }
            case "sin" -> currentInput.append("Math.sin(");
            case "cos" -> currentInput.append("Math.cos(");
            case "√" -> currentInput.append("Math.sqrt(");
            default -> currentInput.append(input);
        }
        display.setText(currentInput.toString());
    }

    private void evaluate() {
        try {
            String expr = currentInput.toString()
                    .replace("Math.sin", "Math.sin")
                    .replace("Math.cos", "Math.cos")
                    .replace("Math.sqrt", "Math.sqrt");
            Object result = new javax.script.ScriptEngineManager()
                    .getEngineByName("JavaScript")
                    .eval(expr);
            display.setText(String.valueOf(result));
            historyView.getItems().add(expr + " = " + result);
            currentInput.setLength(0);
        } catch (Exception e) {
            display.setText("Erreur");
            currentInput.setLength(0);
        }
    }

    private void handleKeyPress(KeyEvent event) {
        KeyCode code = event.getCode();
        if (code.isDigitKey() || code.isKeypadKey()) {
            currentInput.append(event.getText());
        } else if (code == KeyCode.ENTER) {
            evaluate();
        } else if (code == KeyCode.BACK_SPACE && currentInput.length() > 0) {
            currentInput.deleteCharAt(currentInput.length() - 1);
        } else if (code == KeyCode.ADD) {
            currentInput.append("+");
        } else if (code == KeyCode.SUBTRACT) {
            currentInput.append("-");
        } else if (code == KeyCode.MULTIPLY) {
            currentInput.append("*");
        } else if (code == KeyCode.DIVIDE) {
            currentInput.append("/");
        }
        display.setText(currentInput.toString());
    }

    private void toggleTheme() {
        darkMode = !darkMode;
        if (darkMode) {
            display.setStyle("-fx-control-inner-background: #1e1e1e; -fx-text-fill: white;");
        } else {
            display.setStyle("-fx-control-inner-background: white; -fx-text-fill: black;");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
