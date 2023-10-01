package hotelbooking.View;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HotelApp extends Application{
    


    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Hotel Booking App");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hotelbooking/View/HotelApp.fxml"));

        try {
            primaryStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        primaryStage.show();

    }

}
