import javafx.scene.shape.Rectangle;

public class Vehicle implements Comparable<Vehicle> {
    private final String id;
    private final boolean emergency;
    private final long arrivalTime;
    private final String source;
    private final String destination;
    private Rectangle visual; // 🔹 this is for GUI representation

    public Vehicle(String id, boolean emergency, String source, String destination) {
        this.id = id;
        this.emergency = emergency;
        this.arrivalTime = System.currentTimeMillis();
        this.source = source;
        this.destination = destination;
    }

    private boolean inTransit = false;

    public boolean isInTransit() {
        return inTransit;
    }

    public void setInTransit(boolean inTransit) {
        this.inTransit = inTransit;
    }

    public String getId() {
        return id;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public Rectangle getVisual() {
        return visual;
    }

    public void setVisual(Rectangle visual) {
        this.visual = visual;
    }

    @Override
    public int compareTo(Vehicle other) {
        if (this.emergency && !other.emergency)
            return -1;
        if (!this.emergency && other.emergency)
            return 1;
        return Long.compare(this.arrivalTime, other.arrivalTime);
    }
}
