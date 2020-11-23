package models.keyboards;

import java.util.List;

public class ResponseKeyboard {
    private final List<List<String>> keyboardRows;

    public ResponseKeyboard(List<List<String>> keyboardRows) {
        this.keyboardRows = keyboardRows;
    }

    public List<List<String>> getKeyboardRows() {
        return keyboardRows;
    }
}
