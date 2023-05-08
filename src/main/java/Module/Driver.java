package Module;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Driver extends Application {



    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Map.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("World Map");
        stage.setScene(scene);
        stage.show();

    }

}
