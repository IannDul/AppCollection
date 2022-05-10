package commands;


import commands.dependencies.Instances;
import dragon.Dragon;

import java.util.List;

/**
 * Класс, предназначенный для вывода количества элементов с заданным <b>возрастом</b> (<i>обязательный аргумент команды</i>)
 */
public final class CountByAge extends Command {
    int statusCode = 200;
    @Override
    public String getName() {
        return "count_by_age";
    }

    public CountByAge(List<String> args) {
        super(args, 1);
    }

    @Override
    public int execute(Instances instances) {
        Long age;
        try{
            age = Long.parseLong(args.get(0));
        }
        catch(RuntimeException e){
            instances.outPutter.output("Типы данных не совпали");
            statusCode = 400;
            return -1;
        }
        Long ageCount = instances.dao.getAll().stream().filter(dragon -> dragon.getAge().equals(age)).count();

        instances.outPutter.output(ageCount);
        return 0;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }
}
