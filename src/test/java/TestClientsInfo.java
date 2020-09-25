import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class TestClientsInfo {
    private ClientsInfo clientsInfo;

    @BeforeEach
    public void setUp() {
        clientsInfo = new ClientsInfo();
    }

    @Test
    public void testisCorrectAnswer_whenAnswerIsCorrect() {
        Question ex = clientsInfo.generateExample("id");
        Assertions.assertTrue(
                clientsInfo.isCorrectAnswer(ex.getAnswer(), "id"));
    }

    @Test
    public void testisCorrectAnswer_whenAnswerIsNotCorrect() {
        Question ex = clientsInfo.generateExample("id");
        Assertions.assertFalse(
                clientsInfo.isCorrectAnswer(ex.getAnswer() + "1", "id"));
    }
}
