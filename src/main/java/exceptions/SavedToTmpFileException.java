package exceptions;

/**
 * Исключение при сохранении коллекции во временный файл
 */
public class SavedToTmpFileException extends RuntimeException{
    public SavedToTmpFileException(String msg) {
        super(msg);
    }
}
