public class Question {
    private String example;
    private String answer;

    public Question(String example, String answer) {
        this.answer = answer;
        this.example = example;
    }

    public String getAnswer() {
        return answer;
    }

    public String getExample() {
        return example;
    }
}
