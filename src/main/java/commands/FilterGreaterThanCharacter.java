package commands;


import commands.dependencies.Instances;
import dragon.Dragon;
import dragon.DragonCharacter;

import java.util.List;

/**
 * Класс, предназначенный для вывода элементов, значение поля <b>характер</b> которых больше заданного (<i>обязательный агрумент команды</i>)<br>
 * Сравнение характеров происходит по <b>длине названия</b> характера
 *
 */
public final class FilterGreaterThanCharacter extends Command {
    int statusCode = 200;
    @Override
    public String getName() {
        return "FGTC";
    }

    public FilterGreaterThanCharacter(List<String> args) {
        super(args, 1);
    }

    @Override
    public int execute(Instances instances) {
        DragonCharacter character;
        try {
            character = args.get(0).equalsIgnoreCase("null")? null : DragonCharacter.valueOf(args.get(0).toUpperCase());
        }
        catch (RuntimeException e){
            instances.outPutter.output("Характер не определён");
            statusCode = 400;
            return -1;
        }
        long count = 0;
        count = instances.dao.getAll().stream()
                .filter(dragon -> DragonCharacter.compareBoolean(dragon.getCharacter(), character)).count();
        if (count == 0) {
            instances.outPutter.output("Нет подходящих элементов");
        } else {
            instances.dao.getAll().stream()
                    .filter(dragon -> DragonCharacter.compareBoolean(dragon.getCharacter(), character))
                    .forEach(instances.outPutter::output);
        }

        return 0;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }
}
