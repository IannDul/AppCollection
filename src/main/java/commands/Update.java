package commands;

import commands.dependencies.GetProperties;
import commands.dependencies.Instances;
import commands.dependencies.PropertiesDependant;
import dragon.Dragon;
import exceptions.InvalidValueException;

import java.util.List;

/**
 * Класс, предназначенный для обновления существующего элемента по его <b>ID</b> (<i>обязательный аргумент команды</i>)<br>
 * При вводе данных в консоль пользователю будет показываться приглашение к вводу<br>
 * При вводе данных в файл все характеристики элемента нужно вводить последовательно через пробел
 */
public final class Update extends Command implements PropertiesDependant {
    int statusCode = 200;
    @Override
    public String getName() {
        return "update";
    }

    public Update(List<String> args) {
        super(args, 1, 10);
        indexShift = 1;
    }

    @Override
    public int execute(Instances instances) {
        int id;
        try{
            id = Integer.parseInt(args.get(0));
        }
        catch (RuntimeException e){
            instances.outPutter.output("Нецелочисленный тип данных id");
            statusCode = 400;
            return -1;
        }
        boolean found = false;
//        for(Dragon d: instances.dao.getAll()) {
//            if(d.getId() == id) {
//                found = true;
//                break;
//            }
//        }

        found = instances.dao.getAll().stream().anyMatch(dragon -> dragon.getId() == id);

        if (!found){
            instances.outPutter.output("Элемент с id %d не существует".formatted(id));
            statusCode = 403;
            return -1;
        }

        int exitCode = instances.dao.update(id, properties);

        if (exitCode == 0)
            instances.outPutter.output("Элемент успешно обновлён");
        return exitCode;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }
}
