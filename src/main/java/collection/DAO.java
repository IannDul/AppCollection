package collection;

import dragon.Dragon;
import io.Properties;

import java.util.List;
/**
* Интерфейс для работы с коллекцией
*/

public interface DAO {
    int create(Properties properties);
    int update(int id, Properties properties);
    int delete(int id);
    Dragon get(int id);
    List<Dragon> getAll();
    int clear();
}
