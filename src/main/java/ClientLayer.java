import com.fasterxml.jackson.core.JsonProcessingException;
import commands.Command;
import commands.CommandCreator;
import commands.ExecuteScript;
import commands.dependencies.CommandProperties;
import commands.dependencies.Instances;
import exceptions.InvalidArgsSizeException;
import exceptions.InvalidValueException;
import exceptions.ProgramExitException;
import io.ConsoleOutput;
import json.Json;
import net.Client;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.util.*;

public final class ClientLayer {

    private final Client client;
    private final Instances instances = new Instances();
    ArrayList<String> logins;

    public ClientLayer() throws IOException {
        client = new Client("localhost", 80);
        instances.outPutter = new ConsoleOutput();
        logins = new ArrayList<>();
    }

    public void run() {

        instances.outPutter.output("Введите команду. Для полного списка команд введите help");
        for(;;) {
            try {
                loopBody();
            } catch (ProgramExitException e) {
                instances.outPutter.output(e.getMessage());
                break;
            } catch (PortUnreachableException e) {
                instances.outPutter.output("Сервер не работает в данный момент. Повторите попытку позже");
            }
            catch (InvalidValueException | IOException e) {
                instances.outPutter.output(e.getMessage());
            }
        }
    }

    private void loopBody() throws InvalidValueException, IOException {
        List<Command> commands;
        try {
            commands = CommandCreator.getCommands(instances.consoleReader);
        } catch (InvalidArgsSizeException e) {
            instances.outPutter.output(e.getMessage());
            return;
        } catch (NoSuchElementException e) {
            throw new ProgramExitException("Завершение программы...");
        } catch (NullPointerException e) {
            instances.outPutter.output("Такой команды не существует. Введите help для подробной информации");
            return;
        }

        List<CommandProperties> commandProperties = new ArrayList<>();

        for(Command c: commands) {
            if (c instanceof ExecuteScript)
                Instances.filePathChain.clear();
            try {
                if (c instanceof ExecuteScript)
                    commandProperties.addAll(handleExecuteScript((ExecuteScript) c));
                else
                    commandProperties.add(c.getProperties(instances));
            } catch (NullPointerException e) {
                instances.outPutter.output("Одна или несколько команд не были распознаны");
                return;
            } catch (RuntimeException e) {
                instances.outPutter.output(e.getMessage());
                return;
            }
        }

        for (CommandProperties p: commandProperties) {
            String request;

            // Закончить работу клиента, не отправлять команду на сервер
            if (p.args.get(0).equals("exit"))
                throw new ProgramExitException("Завершение программы...");

            try {
                request = serialize(p);
            } catch (JsonProcessingException e) {
                instances.outPutter.output("Ошибка сериализации json");
                return;
            }

            String response = client.sendAndReceiveResponse(request, 20);

            instances.outPutter.output(response);
        }

    }

    private List<CommandProperties> handleExecuteScript(ExecuteScript script) throws InvalidValueException {
        List<CommandProperties> properties = new ArrayList<>();

        for(Command c: script.getNestedCommands(instances))
            properties.add(c.getProperties(instances));

        return properties;
    }

    private String serialize(CommandProperties p) throws JsonProcessingException {
        return Json.stringRepresentation(Json.toJson(p), false);
    }



    public static void main(String[] args) {
        try {
            ClientLayer layer = new ClientLayer();
            layer.run();
        } catch (IOException e) {
            System.out.println("не удалось создать клиент");
        }

    }

}

