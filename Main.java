import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        TrafficSimulatorGUI simulator = new TrafficSimulatorGUI();
        Scene scene = new Scene(simulator.getRoot(), 1200, 800);

        primaryStage.setTitle("Traffic Management System Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();

        simulator.startSimulation();
    }

    public static void main(String[] args) {
        launch(args);
    }
}