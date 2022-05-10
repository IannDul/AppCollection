package commands;

import commands.dependencies.Instances;
import dragon.Dragon;

import java.util.List;
import java.util.Optional;

/**
 * Класс, предназначенный для вывода элемента с наименьшим <b>ID</b> в заранее заданный поток вывода
 * <table align="left" border="1">
 *     <thead>Формат вывода элемента:</thead>
 *     <tr><i>ID</i></tr>
 *     <tr><i>Имя</i></tr>
 *     <tr><i>Координаты</i></tr>
 *     <tr><i>Дата инициализации(с точностью до дней)</i></tr>
 *     <tr><i>Возраст</i></tr>
 *     <tr><i>Цвет</i></tr>
 *     <tr><i>Тип</i></tr>
 *     <tr><i>Характер</i></tr>
 *     <tr><i>Информация о пещере</i></tr>
 * </table>
 *
 */
public final class MinById extends Command {
    int statusCode = 200;
    @Override
    public String getName() {
        return "min_by_id";
    }

    public MinById(List<String> args) {
        super(args, 0);
    }

    @Override
    public int execute(Instances instances) {
        Optional<Dragon> minDragon = instances.dao.getAll()
                .stream()
                .min((dragon1, dragon2) -> (int) dragon1.getId() - dragon2.getId());

        if (minDragon.isPresent()) {
            instances.outPutter.output(minDragon.get().getId());
        }
        else
            instances.outPutter.output("Коллекция пуста");
        return 0;

    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }
}