package io;

import collection.DAO;
import collection.Describable;
import collection.DragonDAO;
import commands.dependencies.Instances;
import exceptions.SavedToTmpFileException;

public class Autosaver {
    public static void autosave(Instances instances) {
        try {
            FileManipulator.save(((Describable) instances.dao));

        } catch (SavedToTmpFileException e) {
            instances.outPutter.output(e.getMessage());
        }
        catch (RuntimeException e) {
            instances.outPutter.output("Автоматическое сохранение коллекции завершилось ошибкой (" + e.getMessage() + ")");
        }
    }

}
