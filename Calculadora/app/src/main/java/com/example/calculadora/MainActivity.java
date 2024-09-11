package com.example.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    // Declaração do TextView que exibirá o resultado e a string que armazenará o número atual
    private TextView resultTextView;
    private String currentNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa o TextView e o botão de igual, desabilitando o botão de igual inicialmente
        resultTextView = findViewById(R.id.result);
        Button equalButton = findViewById(R.id.equal);
        equalButton.setEnabled(false);
    }

    // Método chamado quando um número é clicado
    public void onNumberClick(View view) {
        Button button = (Button) view;
        String value = button.getText().toString();

        // Impede a adição de mais de um ponto decimal
        if (value.equals(".") && currentNumber.contains(".")) {
            return;
        }

        // Adiciona o valor ao número atual e atualiza o estado do botão de igual
        appendToCurrentNumber(value);
        updateEqualButtonState();
    }

    // Método chamado quando um operador é clicado
    public void onOperatorClick(View view) {
        // Impede a adição de um operador se o número atual estiver vazio
        if (currentNumber.isEmpty()) {
            return;
        }

        // Adiciona o operador ao número atual e atualiza o estado do botão de igual
        appendToCurrentNumber(((Button) view).getText().toString());
        updateEqualButtonState();
    }

    // Método chamado quando o botão de igual é clicado
    public void onEqualClick(View view) {
        // Verifica se o número atual está vazio ou termina com um operador
        if (currentNumber.isEmpty() || isOperator(currentNumber.substring(currentNumber.length() - 1))) {
            displayError();
            return;
        }

        // Tenta avaliar a expressão e exibir o resultado
        try {
            double result = evaluateExpression(currentNumber);
            displayResult(result);
        } catch (Exception e) {
            displayError();
        }
    }

    // Atualiza o estado do botão de igual (habilitado/desabilitado)
    private void updateEqualButtonState() {
        Button equalButton = findViewById(R.id.equal);
        equalButton.setEnabled(!currentNumber.isEmpty() && !isOperator(currentNumber.substring(currentNumber.length() - 1)));
    }

    // Método chamado quando o botão de limpar é clicado
    public void onClearClick(View view) {
        clearCurrentNumber();
    }

    // Método chamado quando o botão de porcentagem é clicado
    public void onPercentageClick(View view) {
        if (!currentNumber.isEmpty()) {
            double number = Double.parseDouble(currentNumber);
            double result = number / 100;
            displayResult(result);
        }
    }

    // Método chamado quando o botão de deletar é clicado
    public void onDeleteClick(View view) {
        if (!currentNumber.isEmpty()) {
            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
            resultTextView.setText(currentNumber);
        }
    }

    // Adiciona um valor ao número atual, impedindo operadores ou pontos duplicados
    private void appendToCurrentNumber(String value) {
        if ((isOperator(value) || value.equals(".")) && !currentNumber.isEmpty() && (isOperator(currentNumber.substring(currentNumber.length() - 1)) || currentNumber.endsWith("."))) {
            return;
        }
        currentNumber += value;
        resultTextView.setText(currentNumber);
    }

    // Verifica se um valor é um operador
    private boolean isOperator(String value) {
        return value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/");
    }

    // Exibe o resultado no TextView e atualiza o número atual
    private void displayResult(double result) {
        currentNumber = String.valueOf(result);
        resultTextView.setText(currentNumber);
    }

    // Exibe uma mensagem de erro no TextView e limpa o número atual
    private void displayError() {
        resultTextView.setText(R.string.error);
        clearCurrentNumber();
    }

    // Limpa o número atual e o TextView
    private void clearCurrentNumber() {
        currentNumber = "";
        resultTextView.setText("");
    }

    // Avalia a expressão matemática e retorna o resultado
    private double evaluateExpression(String expression) {
        if (!expression.matches("[0-9+\\-*/().]*")) {
            throw new IllegalArgumentException("Expressão contém caracteres inválidos.");
        }
        try {
            Expression exp = new ExpressionBuilder(expression).build();
            return exp.evaluate();
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Erro aritmético na expressão.", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro na avaliação da expressão.", e);
        }
    }
}