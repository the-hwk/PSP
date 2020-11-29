import com.google.gson.Gson;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();
        Keks keks = gson.fromJson("{\"command\":\"LOL\",\"commands\":{\"Second\":\"21312\",\"First\":\"Value\"}}", Keks.class);
        for (Map.Entry<String, String> entry : keks.commands.entrySet()) {
            System.out.println(entry.getKey() + "->" + entry.getValue());
        }
    }
}

enum Command {
    LOL
}

class Keks {
    private Command command;
    Map<String, String> commands;

    public Keks(Command command, Map<String, String> commands) {
        this.command = command;
        this.commands = commands;
    }
}
