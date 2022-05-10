package commands;


import commands.dependencies.Instances;

import java.util.List;

/**
 * Класс, предназначенный для очищения коллекции (<i>безвозвратного удаления всех элементов</i>)
 */
public final class Clear extends Command {
    int statusCode = 200;
    @Override
    public String getName() {
        return "clear";
    }

    public Clear(List<String> args) {
        super(args, 0);
    }

    @Override
    public int execute(Instances instances) {
        instances.dao.clear();
        instances.outPutter.output("Коллекция успешно очищена");
        return 0;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }
}