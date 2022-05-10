package collection;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import dragon.Dragon;
import io.Properties;

import json.Json;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс, который имплементируется от collection.DAO. В нём мы реализуем методы для работы с коллекцией и инициализируем саму коллекцию
 */
public final class DragonDAO implements DAO, Describable, Orderable {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime initDateTime;

    private int availableId = 1;

    private List<Dragon> collection = new LinkedList<>();

    public DragonDAO() {
        initDateTime = LocalDateTime.now();
    }

    /**
     * Метод добавления элемента в коллекцию
     * @param properties - свойства элемента
     * */
    @Override
    public int create(Properties properties) {
        collection.add(new Dragon(availableId++, properties));
        return 0;
    }
    /**
     * Метод обновления элемента в коллекции по его id
     * @param properties - свойства элемента
     * @param id - id элемента, который пользователь хочет обновить
     * */
    @Override
    public int update(int id, Properties properties) {
        for(Dragon dragon1 : collection){
            if (id == dragon1.getId()) {
                dragon1.update(properties);
                return 0;
            }
        }
        return -1;
    }
    /**
     * Метод удаления элемента из коллекции по его id
     * @param id - id элемента, который пользователь хочет удалить
     * */
    @Override
    public int delete(int id) {
        if (collection.removeIf(dragon -> dragon.getId() == id))
            return 0;
        return -1;
    }
    /**
     * Метод получения элемента из коллекции по его id
     * @param id - id элемента, который пользователь хочет получить
     * @return dragon - элемент коллекции
     * */
    @Override
    public Dragon get(int id) {
        for(Dragon dragon : collection){
            if (dragon.getId() == id) {
                return dragon;
            }
        }
        return null;
    }
    /**
     * Метод получения всей коллекции
     * @return outputCollection - копия коллекции
     * */
    @JsonProperty("collection")
    @Override
    public List<Dragon> getAll(){
        List<Dragon> outputCollection = new LinkedList<>();
        outputCollection.addAll(collection);
        return outputCollection;
    }
    /**
     * Метод очистки всей коллекции
     * */
    @Override
    public int clear() {
        collection.clear();
        return 0;
    }
    /**
     * Метод возвращения информации о коллекции
     * @return output - информация о коллекции
     * */
    @Override
    public String description() throws JsonProcessingException {
        JsonNode node = Json.toJson(this);
        return Json.stringRepresentation(node, true);
    }
    /**
     * Метод сортировки коллекции
     * */
    @Override
    public int sort() {
        Collections.sort(collection);
        return 0;
    }

    @Override
    public String info() {
        return "Collection {" + System.lineSeparator() +
                "\tinit date: " + initDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss")) + "," + System.lineSeparator() +
                "\ttype: " + "LinkedList" + "," + System.lineSeparator() +
                "\tsize: " + collection.size() + "," + System.lineSeparator() +
                "}";
    }

    public LocalDateTime getInitDateTime() {
        return initDateTime;
    }

    public void setInitDateTime(LocalDateTime initDateTime) {
        this.initDateTime = initDateTime;
    }

    public int getAvailableId() {
        return availableId;
    }

    public void setAvailableId(int availableId) {
        this.availableId = availableId;
    }

    public void setCollection(List<Dragon> collection) {
        this.collection = collection;
    }
}
