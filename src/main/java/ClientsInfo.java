import java.util.concurrent.ConcurrentHashMap;

public class ClientsInfo {
    private ConcurrentHashMap<String, String> clientIdToExpectedAnswer;

    public ClientsInfo() {
        clientIdToExpectedAnswer = new ConcurrentHashMap<>();
    }

    public Question generateExample(String chatId) {
        Question nextQuestion = QuestionFactory.getNextQuestion();
        clientIdToExpectedAnswer.put(chatId, nextQuestion.getAnswer());
        return nextQuestion;
    }

    public boolean isCorrectAnswer(String answer, String chatId) {
        return clientIdToExpectedAnswer.get(chatId).equals(answer);
    }
}
