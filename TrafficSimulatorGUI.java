
// TrafficSimulatorGUI.java
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.animation.*;
import javafx.util.Duration;
import java.util.*;

public class TrafficSimulatorGUI {
    private final BorderPane root = new BorderPane();
    private final RoadNetwork roadNetwork = new RoadNetwork();
    private final Map<String, IntersectionNode> intersectionNodes = new HashMap<>();
    private final Pane simulationPane = new Pane();
    private final TextArea logArea = new TextArea();
    private final Timeline simulationTimeline;
    private int simulationTime = 0;

    private final List<Vehicle> vehicles = new ArrayList<>();
    private final Map<String, Point2D> positions = Map.of(
            "A", new Point2D(200, 200),
            "B", new Point2D(600, 200),
            "C", new Point2D(600, 600),
            "D", new Point2D(200, 600));

    public TrafficSimulatorGUI() {
        initializeRoadNetwork();
        setupUI();

        simulationTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    simulationTime++;
                    updateSimulation();
                }));

        simulationTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void initializeRoadNetwork() {
        String[] intersections = { "A", "B", "C", "D" };
        for (String id : intersections) {
            roadNetwork.addIntersection(id);
        }

        roadNetwork.addRoad("A", "B", 2);
        roadNetwork.addRoad("B", "C", 3);
        roadNetwork.addRoad("A", "D", 5);
        roadNetwork.addRoad("D", "C", 4);

        addVehicle(new Vehicle("V1", false, "A", "C"));
        addVehicle(new Vehicle("V2", true, "A", "B"));
        addVehicle(new Vehicle("V3", false, "A", "D"));
        addVehicle(new Vehicle("V4", false, "A", "C"));
    }

    private void setupUI() {
        simulationPane.setPrefSize(900, 700);
        drawRoadNetwork();

        VBox controlPanel = new VBox(10);
        Button startButton = new Button("Start Simulation");
        Button pauseButton = new Button("Pause");
        Button resetButton = new Button("Reset");

        startButton.setOnAction(e -> startSimulation());
        pauseButton.setOnAction(e -> simulationTimeline.pause());
        resetButton.setOnAction(e -> resetSimulation());

        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPrefHeight(150);

        controlPanel.getChildren().addAll(startButton, pauseButton, resetButton, new Label("Simulation Log:"), logArea);
        controlPanel.setPadding(new javafx.geometry.Insets(10));

        root.setCenter(simulationPane);
        root.setRight(controlPanel);
    }

    private void drawRoadNetwork() {
        simulationPane.getChildren().clear();
        intersectionNodes.clear();

        for (String from : roadNetwork.getAdjacentIntersections().keySet()) {
            for (String to : roadNetwork.getAdjacentIntersections().get(from).keySet()) {
                Point2D start = positions.get(from);
                Point2D end = positions.get(to);

                Line road = new Line(start.getX(), start.getY(), end.getX(), end.getY());
                road.setStroke(Color.GRAY);
                road.setStrokeWidth(3);

                Text label = new Text(
                        (start.getX() + end.getX()) / 2,
                        (start.getY() + end.getY()) / 2,
                        roadNetwork.getAdjacentIntersections().get(from).get(to) + " min");

                simulationPane.getChildren().addAll(road, label);
            }
        }

        for (String id : positions.keySet()) {
            Point2D pos = positions.get(id);
            IntersectionNode node = new IntersectionNode(id, pos.getX(), pos.getY());
            intersectionNodes.put(id, node);
            simulationPane.getChildren().add(node.getVisual());
        }

        for (Vehicle v : vehicles) {
            simulationPane.getChildren().add(v.getVisual());
        }
    }

    private void updateSimulation() {
        for (String id : intersectionNodes.keySet()) {
            Intersection intersection = roadNetwork.getIntersection(id);
            int queueSize = intersection.getQueueSize();
            int greenDuration = Math.min(60, 30 + queueSize * 3);
            intersection.updateLightTiming(greenDuration);

            intersectionNodes.get(id).update(
                    intersection.getQueueSize(),
                    intersection.isGreenLight(),
                    intersection.getEmergencyQueueSize());
        }

        moveVehicles();

        logArea.appendText("Time: " + simulationTime + "s - ");
        logArea.appendText("Intersection A: " +
                roadNetwork.getIntersection("A").getQueueStatus() + "\n");
    }

    private void moveVehicles() {
        for (Vehicle v : vehicles) {
            Rectangle rect = v.getVisual();
            double x = rect.getLayoutX();
            double y = rect.getLayoutY();
            Point2D source = positions.get(v.getSource());
            Point2D dest = positions.get(v.getDestination());

            double dx = dest.getX() - x;
            double dy = dest.getY() - y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            Intersection sourceIntersection = roadNetwork.getIntersection(v.getSource());
            boolean canMove = v.isEmergency() || sourceIntersection.isGreenLight();

            if (distance > 5 && canMove) {
                double step = 5;
                double nx = x + (dx / distance) * step;
                double ny = y + (dy / distance) * step;
                rect.setLayoutX(nx);
                rect.setLayoutY(ny);
            } else if (distance <= 5 && v.isEmergency()) {
                Intersection destIntersection = roadNetwork.getIntersection(v.getDestination());
                destIntersection.setForceGreenForEmergency(true); // 💡 Force green
            }
        }
    }

    private void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        roadNetwork.getIntersection(vehicle.getSource()).addVehicle(vehicle);

        Point2D pos = positions.get(vehicle.getSource());
        Rectangle visual = new Rectangle(12, 12, vehicle.isEmergency() ? Color.BLUE : Color.BLACK);
        visual.setStroke(Color.YELLOW);
        Tooltip.install(visual, new Tooltip(vehicle.getId()));

        visual.setLayoutX(pos.getX() + Math.random() * 30);
        visual.setLayoutY(pos.getY() + Math.random() * 30);

        vehicle.setVisual(visual);
        simulationPane.getChildren().add(visual);
    }

    private void resetSimulation() {
        simulationTimeline.stop();
        simulationTime = 0;
        vehicles.clear();
        simulationPane.getChildren().clear();
        initializeRoadNetwork();
        drawRoadNetwork();
        logArea.clear();
    }

    public void startSimulation() {
        logArea.clear();
        logArea.appendText("Simulation started\n");
        simulationTimeline.play();
    }

    public BorderPane getRoot() {
        return root;
    }
}
