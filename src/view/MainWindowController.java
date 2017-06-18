package view;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import model.RecordsList;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.Record;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Контроллер для визуальной формы MainWindow
 */
public class MainWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Record> recordsTable;

    @FXML
    private TableColumn<Record, String> colName;

    @FXML
    private TableColumn<Record, Integer> colPlan;

    @FXML
    private TableColumn<Record, Integer> colFact;

    @FXML
    private TableColumn<Record, Integer> colW;

    @FXML
    private TableColumn<Record, Double> colPercent;

    @FXML
    private Button editButton;

    @FXML
    private Button delButton;

    @FXML
    private Button addButton;

    @FXML
    private TextField planSum;

    @FXML
    private TextField factSum;

    private RecordsList list = new RecordsList();

    /**
     * Инициализация формы
     */
    @FXML
    void initialize() {
        recordsTable.setPlaceholder(new Label("Нет данных"));

        colName.setCellValueFactory(cellData -> cellData.getValue().getPlantName());
        colPlan.setCellValueFactory(cellData -> cellData.getValue().getByPlan().asObject());
        colFact.setCellValueFactory(cellData -> cellData.getValue().getInFact().asObject());
        colW.setCellValueFactory(cellData -> cellData.getValue().getDeviationW().asObject());
        colPercent.setCellValueFactory(cellData -> cellData.getValue().getDeviationP().asObject());

        delButton.disableProperty().bind(Bindings.isEmpty(recordsTable.getSelectionModel().getSelectedItems()));
        editButton.disableProperty().bind(Bindings.isEmpty(recordsTable.getSelectionModel().getSelectedItems()));

        recordsTable.setItems(list.getRecordsList());
    }

    /**
     * Открытие окна для добавления записи
     * @param event событие нажатия на кнопку "Добавить"
     */
    @FXML
    void handleAdd(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RecordForm.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Создаём диалоговое окно Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Добавление записи");
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner((Stage) editButton.getScene().getWindow());
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.getIcons().add(new Image("/resources/icon.png", 3000, 3000, false, true));

        RecordFormController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.showAndWait();

        if(controller.isOkClicked()) {
            list.add(controller.getRecord());
            recordsTable.refresh();
            calcSum();
        }
    }

    /**
     * Открытие окна для редактирования записи
     * @param event событие нажатия на кнопку "Изменить"
     */
    @FXML
    void handleEdit(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RecordForm.fxml"));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Изменение записи");
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner((Stage) editButton.getScene().getWindow());
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        RecordFormController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        int selectedIndex = recordsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            controller.setRecord(recordsTable.getItems().get(selectedIndex));
        }

        dialogStage.showAndWait();

        if(controller.isOkClicked()) {
            recordsTable.refresh();
            calcSum();
        }
    }

    /**
     * Удаление выбранной записи из ведомости
     * @param event событие нажатия на кнопку "Удалить"
     */
    @FXML
    void handleDelete(ActionEvent event) {
        int selectedIndex = recordsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            recordsTable.getItems().remove(selectedIndex);
        }
        calcSum();
    }

    /**
     * Открытие диалогового окна с информацией об авторах программы
     * @param event событие нажатия на подпункт меню "Выход"
     */
    @FXML
    void handleAuthors(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Авторы программы");
        alert.setHeaderText(null);
        alert.setContentText("Полупан Алена и Пирожкова Екатерина\nСтуденты группы 6.04.51.15.02");

        alert.showAndWait();
    }

    /**
     * Закрытие программы
     * @param event событие нажатия на подпункт меню "Выход"
     */
    @FXML
    void handleClose(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Открытие ведомости из текстового файла
     * @param event событие нажатия на подпункт меню "Открыть"
     */
    @FXML
    void handleOpenFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите текстовый файл");
        fileChooser.getExtensionFilters().
                addAll(new FileChooser.ExtensionFilter("Текстовый файл (.txt)",
                        "*.txt"));

        File file = fileChooser.showOpenDialog(addButton.getScene().getWindow());
        if (file != null) {
            list.clear();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] str = line.split(";");

                    Record record = new Record(str[0], Integer.parseInt(str[1]), Integer.parseInt(str[2]));

                    list.add(record);
                    calcSum();
                }

            } catch (IOException e) {
                showError("При открытии файла произошла ошибка");
            } catch (NumberFormatException e) {
                showError("Файл содержит неверные данные");
            } catch (Exception e) {
                showError("Файл содержит неверные данные");
            }
        }
    }

    /**
     * Сохранение ведомости в текстовый файл
     * @param event событие нажатия на подпункт меню "Сохранить"
     */
    @FXML
    void handleSaveFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Текстовый файл (.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(addButton.getScene().getWindow());

        if(file != null){
            try {
                Writer writer = new FileWriter(file);
                ObservableList<Record> records = list.getRecordsList();
                for (Record record : records) {
                    writer.write(record.getPlantName().getValue());
                    writer.write(";");
                    writer.write(String.valueOf(record.getByPlan().get()));
                    writer.write(";");
                    writer.write(String.valueOf(record.getInFact().get()));
                    writer.write(System.getProperty("line.separator"));
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();  // вывести окошко
            }
        }
    }

    /**
     * Подсчет суммарных данных
     */
    private void calcSum() {
        int totalPlan = 0;
        int totalFact = 0;
        for (Record record : recordsTable.getItems()) {
            totalPlan = totalPlan + record.getByPlan().get();
            totalFact = totalFact + record.getInFact().get();
        }

        planSum.setText(String.valueOf(totalPlan));
        factSum.setText(String.valueOf(totalFact));
    }

    /**
     * Показ сообщения об ошибке
     * @param error сообщение об ошибке
     */
    private void showError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Произошла ошибка");
        alert.setContentText(error);
        alert.initOwner(addButton.getScene().getWindow());
        alert.showAndWait();
    }
}