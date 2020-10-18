package models.keyboards;

public class InlineButtonInfo {
    private final String buttonText;
    private final String callBackData;

    public InlineButtonInfo(String buttonText, String callBackData) {
        this.buttonText = buttonText;
        this.callBackData = callBackData;
    }

    public String getButtonText() {
        return this.buttonText;
    }

    public String getCallBackData(){
        return this.callBackData;
    }
}
