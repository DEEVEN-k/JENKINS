// CalculatorController.java
package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculatorController {

    @FXML
    private TextArea display;

    private StringBuilder input = new StringBuilder();

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
            Expression expression = new ExpressionBuilder(input.toString()).build();
            double result = expression.evaluate();
            display.setText(String.valueOf(result));
            input.setLength(0);
            input.append(result);
        } catch (Exception e) {
            display.setText("Erreur");
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
                if (text.matches("[0-9+\-*/().]") || text.equals(".")) {
                    appendText(text);
                }
            }
        }
    }
}
