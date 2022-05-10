package commands;


import java.util.List;

import commands.dependencies.Instances;
import exceptions.ProgramExitException;
/**
 * Класс, предназначенный для завершения работы программы в штатном режиме (<i>без сохранения изменений в коллекции</i>)
 *
 */
public final class Exit extends Command {
    @Override
    public String getName() {
        return "exit";
    }

    public Exit(List<String> args) {
        super(args, 0);
    }

    @Override
    public int execute(Instances instances) {
        throw new ProgramExitException("Завершение программы...");
    }
}
