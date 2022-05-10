package commands;

import collection.Orderable;
import commands.dependencies.Instances;

import java.util.List;

/**
 * Класс, предназначенный для сортировки коллекции. Сортировка производится по <i>возрастанию</i> поля <b>"возраст"</b>
 */
public final class Sort extends Command {
    int statusCode = 200;
    @Override
    public String getName() {
        return "sort";
    }

    public Sort(List<String> args) {
        super(args, 0);
    }

    @Override
    public int execute(Instances instances) {
        int exitCode = ((Orderable)instances.dao).sort();
        instances.outPutter.output("Коллекция успешно отсортирована");
        return exitCode;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }
}