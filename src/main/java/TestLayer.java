import collection.DragonDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import commands.Command;
import commands.CommandCreator;
import commands.ExecuteScript;
import commands.dependencies.Instances;
import exceptions.InvalidArgsSizeException;
import exceptions.InvalidValueException;
import exceptions.ProgramExitException;
import io.ConsoleOutput;
import io.ConsoleReader;
import io.FileManipulator;
import io.FileReader;
import io.request.ConsoleRequester;
import json.Json;
import commands.dependencies.CommandProperties;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class TestLayer {
    private final Instances instances = new Instances();

    public TestLayer() {
        instances.outPutter = new ConsoleOutput();

        try {
            instances.dao = FileManipulator.get();
        } catch (RuntimeException e) {
            instances.outPutter.output(e.getMessage());
            instances.dao = new DragonDAO();
        }

        instances.consoleReader = new ConsoleReader();
        instances.consoleRequester = new ConsoleRequester();
        instances.fileReader = new FileReader();

    }
    public void run() {
        instances.outPutter.output("Начало");
        while (true) {
            try {
                loopBody();
            } catch (ProgramExitException e) {
                instances.outPutter.output(e.getMessage());
                break;
            } catch (InvalidValueException e) {
                instances.outPutter.output("Не удалось получить properties");
            }
            catch (IOException e) {
                instances.outPutter.output(e.getMessage());
            }
        }
    }

    private void loopBody() throws IOException, InvalidValueException {
        List<Command> commands;
        try {
            commands = CommandCreator.getCommands(instances.consoleReader);
        } catch (InvalidArgsSizeException e) {
            instances.outPutter.output(e.getMessage());
            return;
        } catch (NoSuchElementException e) {
            throw new ProgramExitException("Завершение программы...");
        }
        catch (NullPointerException e) {
            instances.outPutter.output("Такой команды не существует. Введите help для подробной информации");
            return;
        }

        int exit;
        for (Command c: commands) {
            if (c.getName().equals("execute_script"))
                Instances.filePathChain.clear();

            try {
                if (c instanceof ExecuteScript) {
                    List<Command> commands1 = ((ExecuteScript) c).getNestedCommands(instances);
                    for (Command command : commands1) {
                        CommandProperties p = command.getProperties(instances);
                        instances.outPutter.output(Json.stringRepresentation(Json.toJson(p), true));
                    }
                } else
                    instances.outPutter.output(Json.stringRepresentation(Json.toJson(c.getProperties(instances)), true));
            } catch (NullPointerException e) {
                instances.outPutter.output("Одна или несколько команд не были распознаны");
            }
        }
    }
}
