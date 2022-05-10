package commands;


import commands.dependencies.Instances;

import java.util.List;

/**
 * Класс, предназначенный для удаления элемента коллекции по его <b>ID</b> (<i>обязательный аргумент команды</i>)
 */
public final class RemoveById extends Command {
    int statusCode = 200;
    @Override
    public String getName() {
        return "remove_by_id";
    }

    public RemoveById(List<String> args) {
        super(args, 1);
    }

    @Override
    public int execute(Instances instances) {
        int exitCode;
        try{
            if ((exitCode = instances.dao.delete(Integer.parseInt(args.get(0)))) == 0)
                instances.outPutter.output("Элемент успешно удален");
            else{
                instances.outPutter.output("Элемент не найден.");
                statusCode = 403;
            }
            return exitCode;
        }
        catch (RuntimeException e){
            instances.outPutter.output("Нецелочисленный тип данных id");
            statusCode = 400;
            return -1;
        }
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }
}
