package commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import commands.dependencies.CommandProperties;
import commands.dependencies.Instances;
import exceptions.InfiniteLoopException;
import exceptions.InvalidArgsSizeException;
import exceptions.InvalidValueException;
import io.InputReader;
import json.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, предназначенный для выполнения скрипта (<i>последовательности команд</i>) из файла <br>
 * Путь до искомого файла является обязательным аргументом команды
 */
public final class ExecuteScript extends Command {
    int statusCode = 200;
    @Override
    public String getName() {
        return "execute_script";
    }

    public ExecuteScript(List<String> args) {
        super(args, 1);
    }

    private List<Command> readCommands(Instances instances) {
        String filePath = args.get(0);
        Instances.filePathChain.add(filePath);
        InputReader reader = instances.fileReader;
        reader.addProperties(filePath);
        return CommandCreator.getCommands(reader);
    }

    @Override
    public int execute(Instances instances) {
        if(Instances.filePathChain.contains(args.get(0)))
            throw new InfiniteLoopException();
        List<Command> commands;
        try{
            commands = readCommands(instances);
        }
        catch(InvalidArgsSizeException e) {
            statusCode = 400;
            instances.outPutter.output(e.getMessage());
            return -1;
        }
        catch (RuntimeException e) {
            instances.outPutter.output("Одна или несколько команд не были распознаны");
            statusCode = 400;
            return -1;
        }
        instances.outPutter.output("Все команды были распознаны и поданы на выполнение");

        int exitCode = 0;
        for (Command c : commands) {
            exitCode += c.execute(instances);
        }
        return exitCode;
    }

    public List<Command> getNestedCommands(Instances instances) {
        List<Command> output = new ArrayList<>();
        for (Command c : readCommands(instances)) {

            if (c instanceof ExecuteScript script) {
                if (Instances.filePathChain.contains(script.args.get(0)))
                    throw new InfiniteLoopException();
                output.addAll(script.getNestedCommands(instances));
            } else
                output.add(c);

        }

        return output;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }
}