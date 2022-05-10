package commands;

import commands.dependencies.Instances;
import log.Logger;

import java.util.List;

public final class History extends Command {
    int statusCode = 200;
    @Override
    public String getName() {
        return "history";
    }

    public History(List<String> args) {
        super(args, 0);
    }

    @Override
    public int execute(Instances instances) {
        for (String msg : Logger.getAll()) {
            instances.outPutter.output(msg);
        }
        return 0;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }
}
