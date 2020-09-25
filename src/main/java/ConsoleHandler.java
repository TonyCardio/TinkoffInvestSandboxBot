import java.util.HashMap;

public class ConsoleHandler {
    private final HashMap<String, String> commands = new HashMap<>();

    public ConsoleHandler() {
        String commandHelpText = "Добрый день, утро, вечер, ночь\n" +
                "Я, временно исполняющий обязанности математического бота,\n" +
                "который научит тебя пользоваться калькулятором\n" +
                "Для начала тренироваки напиши - /start\n" +
                "В примерах с делением округление как в математике\n\n" +
                "Желаю успехов!!!\n\n" +
                "P.S. Скоро тут будет Invest Bot";

        String commandStartText = "Удачи!";
        commands.put("/help", commandHelpText);
        commands.put("/start", commandStartText);
    }

    public String getCommandText(String command) {
        return commands.get(command);
    }
}
