package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Класс для создания списка записей
 */
public class RecordsList {
    private ObservableList<Record> recordsList = FXCollections.observableArrayList();

    /**
     * Получить все записи списка
     * @return все записи списка
     */
    public ObservableList<Record> getRecordsList() {
        return recordsList;
    }

    /**
     * Добавление новой записи
     * @param record запись для добавления
     */
    public void add(Record record) {
        recordsList.add(record);
    }

    /**
     * Очистка списка
     */
    public void clear() {
        recordsList.clear();
    }
}
