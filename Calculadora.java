import java.util.*;

public class Calculadora {
    public static void main(String[] args) {

        String inputString;
        Scanner keyb = new Scanner(System.in);

        while (true) {
            System.out.println("Introduce una expresión en notación infija (o 'quit' para salir):");
            System.out.print("> ");
            inputString = keyb.nextLine();

            if (inputString.equalsIgnoreCase("quit")) {
                break;
            }

            List<String> tokens = getTokens(inputString);
            System.out.println("Notación infija: " + Calculadora.toString(tokens));

            List<String> postfix = Calculadora.toPostfix(tokens);
            System.out.println("Notación postfija: " + Calculadora.toString(postfix));

            try {
                double result = evaluatePostfix(postfix);
                System.out.println("Resultado: " + result);
            } catch (Exception e) {
                System.out.println("Error en la evaluación: " + e.getMessage());
            }
        }
        keyb.close();
    }

    // Función para evaluar una expresión en notación postfija
    public static double evaluatePostfix(List<String> postfix) throws Exception {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix) {
            if (isOperand(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new Exception("Expresión mal formada.");
                }
                double b = stack.pop();
                double a = stack.pop();
                double result;

                switch (token) {
                    case "+":
                        result = a + b;
                        break;
                    case "-":
                        result = a - b;
                        break;
                    case "*":
                        result = a * b;
                        break;
                    case "/":
                        if (b == 0) {
                            throw new ArithmeticException("División por cero.");
                        }
                        result = a / b;
                        break;
                    case "^":
                        result = Math.pow(a, b);
                        break;
                    default:
                        throw new Exception("Operador desconocido: " + token);
                }
                stack.push(result);
            }
        }

        if (stack.size() != 1) {
            throw new Exception("Expresión mal formada.");
        }

        return stack.pop();
    }

    public static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") ||
                token.equals("*") || token.equals("/") || token.equals("^");
    }

    public static String toString(List<String> list) {
        StringBuilder output = new StringBuilder();
        for (String token: list) {
            output.append(token).append(" ");
        }
        return output.toString();
    }

    public static ArrayList<String> toPostfix(List<String> input) {
        Stack<String> stack = new Stack<>();
        ArrayList<String> output = new ArrayList<>();
        String t;

        for (String token : input) {
            if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!(t = stack.pop()).equals("(")) {
                    output.add(t);
                }
            } else if (isOperand(token)) {
                output.add(token);
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && getPrec(token) <= getPrec(stack.peek())) {
                    output.add(stack.pop());
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }
        return output;
    }

    public static boolean isOperand(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static List<String> getTokens(String input) {
        StringTokenizer st = new StringTokenizer(input, " ()+-*/^", true);
        ArrayList<String> tokenList = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if (!token.isEmpty()) {
                tokenList.add(token);
            }
        }
        return tokenList;
    }

    public static int getPrec(String token) {
        switch (token) {
            case "^":
                return 3;
            case "*":
            case "/":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                return 0;
        }
    }
}
