import java.util.HashMap;
import java.util.function.BinaryOperator;

public class QuestionFactory {
    private static final HashMap<Integer, BinaryOperator<Integer>> commandNumberToResult =
            new HashMap<>();
    private static final HashMap<Integer, Character> commandNumberToLiteral =
            new HashMap<>();

    static {
        commandNumberToResult.put(0, (x, y) -> x - y);
        commandNumberToResult.put(1, (x, y) -> x + y);
        commandNumberToResult.put(2, (x, y) -> x * y);
        commandNumberToResult.put(3, (x, y) -> (int) Math.round((double) x / y));
        commandNumberToLiteral.put(0, '-');
        commandNumberToLiteral.put(1, '+');
        commandNumberToLiteral.put(2, '*');
        commandNumberToLiteral.put(3, '/');
    }

    public static Question getNextQuestion() {
        int numberOperation = (int) (Math.random() * 4);
        int firstNum = (int) (1 + Math.random() * 100);
        int secondNum = (int) (1 + Math.random() * 100);

        int answer = QuestionFactory.commandNumberToResult.get(numberOperation).apply(firstNum, secondNum);
        char operator = QuestionFactory.commandNumberToLiteral.get(numberOperation);
        String example = String.format("%d %c %d", firstNum, operator, secondNum);
        return new Question(example, Integer.toString(answer));
    }
}
