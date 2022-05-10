package commands;

import commands.dependencies.GetProperties;
import commands.dependencies.Instances;
import commands.dependencies.PropertiesDependant;
import exceptions.InvalidValueException;
import io.Properties;

import java.util.List;
/**
 * Класс, предназначенный для добавления элемента в коллекцию<br>
 * При вводе данных в консоль пользователю будет показываться приглашение к вводу<br>
 * При вводе данных в файл все характеристики элемента нужно вводить последовательно через пробел
 */
public final class Add extends Command implements PropertiesDependant {
    int statusCode = 200;
    @Override
    public String getName() {
        return "add";
    }

    public Add(List<String> args) {
        super(args, 0, 9);
    }

    @Override
    public int execute(Instances instances) {
        int exitCode;
        if ((exitCode = instances.dao.create(properties)) == 0)
            instances.outPutter.output("Элемент успешно добавлен");
            statusCode = 201;

        return exitCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
