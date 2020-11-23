package models.keyboards;

import java.util.List;

public class InlineKeyboard {
    private final List<List<InlineButtonInfo>> keyboardRows;

    public InlineKeyboard(List<List<InlineButtonInfo>> keyboardRows) {
        this.keyboardRows = keyboardRows;
    }

    public List<List<InlineButtonInfo>> getKeyboardRows() {
        return keyboardRows;
    }
}
