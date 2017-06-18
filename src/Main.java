import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Главный класс программы
 */
public class Main extends Application {

    /**
     * Точка входа в программу
     * @param primaryStage окно программы
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/MainWindow.fxml"));
        primaryStage.setTitle("Ведомость потребления электроэнергии");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("/resources/icon.png", 3000, 3000, false, true));
        primaryStage.show();
    }

    /**
     * Запуск программы
     * @param args параметры командной строки
     */
    public static void main(String[] args) {
        launch(args);
    }
}
