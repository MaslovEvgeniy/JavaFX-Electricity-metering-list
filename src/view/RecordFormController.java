package view;

import model.Record;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Контроллер для визуальной формы RecordForm
 */
public class RecordFormController {

    @FXML
    private Button saveButton;

    @FXML
    private TextField nameField = new TextField();

    @FXML
    private TextField planField = new TextField();

    @FXML
    private TextField factField = new TextField();

    private Record record;

    private Stage dialogStage;

    private boolean okClicked = false;


    /**
     * Установка сцены для окна
     * @param dialogStage сцена для окна
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Добавление записи для редактирования
     * @param record запись для редактирования
     */
    public void setRecord(Record record) {
        this.record = record;

        nameField.setText(record.getPlantName().getValue());
        planField.setText(Integer.toString(record.getByPlan().get()));
        factField.setText(Integer.toString(record.getInFact().get()));
    }

    /**
     * Сохранение внесенных изменений
     * @param event событие нажатия на кнопку "Сохранить"
     */
    @FXML
    void handleSave(ActionEvent event) {
        if (checkInput()) {
            if(record != null) {
                record.setPlantName(nameField.getText());
                record.setByPlan(Integer.parseInt(planField.getText()));
                record.setInFact(Integer.parseInt(factField.getText()));
                record.recalc();
            } else {
                record = new Record(nameField.getText(), Integer.parseInt(planField.getText()), Integer.parseInt(factField.getText()));
            }
            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Проверка правильности введенных данных
     * @return результат проверки
     */
    private boolean checkInput() {
        String errorMessage = "";
        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "Название завода не может быть пустым\n";
        }
        try {
            int plan = Integer.parseInt(planField.getText());
            if(plan < 0)
                errorMessage += "Потребление по плану не может быть отрицательным\n";
        } catch (NumberFormatException e) {
            errorMessage += "Потребление по плану введено неверно\n";
        }
        try {
            int fact = Integer.parseInt(factField.getText());
            if(fact < 0)
                errorMessage += "Потребление по факту не может быть отрицательным\n";
        } catch (NumberFormatException e) {
            errorMessage += "Потребление по факту введено неверно\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Показываем сообщение об ошибке.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Неверный ввод");
            alert.setHeaderText("Поля для ввода заполнены неверно");
            alert.setContentText(errorMessage);
            alert.initOwner(saveButton.getScene().getWindow());

            alert.showAndWait();

            return false;
        }

    }

    /**
     * Проверка на то, была ли нажата кнопка "Сохранить"
     * @return результат проверки
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Возвращение созданную запись
     * @return созданная запись
     */
    public Record getRecord() {
        return record;
    }

}