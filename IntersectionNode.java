import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class IntersectionNode {
    private final StackPane visual = new StackPane();
    private final Circle circle = new Circle(30);
    private final Text idText = new Text();
    private final Text countText = new Text();

    public IntersectionNode(String id, double x, double y) {
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.LIGHTGRAY);

        idText.setText(id);
        idText.setFill(Color.BLACK);

        countText.setFill(Color.WHITE);
        countText.setTranslateY(20);

        visual.getChildren().addAll(circle, idText, countText);
        visual.setLayoutX(x);
        visual.setLayoutY(y);
    }

    public void update(int vehicleCount, boolean isGreen, int emergencyCount) {
        circle.setFill(isGreen ? Color.GREEN : Color.RED);
        countText.setText("N:" + vehicleCount + " E:" + emergencyCount);
    }

    public StackPane getVisual() {
        return visual;
    }
}