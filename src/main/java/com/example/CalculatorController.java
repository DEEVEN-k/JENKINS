// CalculatorController.java
package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

public class CalculatorController {

    @FXML
    private TextArea display;

    private final StringBuilder input = new StringBuilder();

    public void appendText(String value) {
        input.append(value);
        display.setText(input.toString());
    }

    public void clear() {
        input.setLength(0);
        display.clear();
    }

    public void deleteLast() {
        if (input.length() > 0) {
            input.deleteCharAt(input.length() - 1);
            display.setText(input.toString());
        }
    }

    public void calculate() {
        try {
            // Ajout de la fonction sqrt
            Function sqrt = new Function("sqrt", 1) {
                @Override
                public double apply(double... args) {
                    return Math.sqrt(args[0]);
                }
            };

            Expression expression = new ExpressionBuilder(input.toString())
                    .functions(sqrt)
                    .build();
            double result = expression.evaluate();
            display.setText(String.valueOf(result));
            input.setLength(0);
            input.append(result);
        } catch (Exception e) {
            display.setText("Erreur");
            input.setLength(0);
        }
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER -> calculate();
            case BACK_SPACE -> deleteLast();
            case ESCAPE -> clear();
            default -> {
                String text = event.getText();
                if (text.matches("[0-9+\\-*/().]") || text.equals(".")) {
                    appendText(text);
                }
            }
        }
    }
    public void handleButton(javafx.event.ActionEvent event) {
        Button btn = (Button) event.getSource();
        String value = btn.getText();
        appendText(value);
    }
}
