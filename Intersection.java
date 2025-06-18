import java.util.*;

public class Intersection {
    private final String id;
    private final Queue<Vehicle> normalLane = new LinkedList<>();
    private final PriorityQueue<Vehicle> emergencyLane = new PriorityQueue<>();
    private int greenDuration = 30;
    private boolean isGreenLight = false;
    private boolean forceGreen = false; // ✅ New flag

    public Intersection(String id) {
        this.id = id;
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicle.isEmergency()) {
            emergencyLane.add(vehicle);
        } else {
            normalLane.add(vehicle);
        }
    }

    public Queue<Vehicle> getNormalLane() {
        return normalLane;
    }

    public PriorityQueue<Vehicle> getEmergencyLane() {
        return emergencyLane;
    }

    public String getQueueStatus() {
        return "Normal: " + normalLane.size() + ", Emergency: " + emergencyLane.size();
    }

    public int getQueueSize() {
        return normalLane.size() + emergencyLane.size();
    }

    public int getEmergencyQueueSize() {
        return emergencyLane.size();
    }

    public boolean isGreenLight() {
        return isGreenLight;
    }

    // ✅ Add this method to allow external classes to force green light
    public void setForceGreenForEmergency(boolean forceGreen) {
        this.forceGreen = forceGreen;
    }

    // ✅ Modify logic to prioritize emergency override
    public void updateLightTiming(int duration) {
        this.greenDuration = duration;

        if (forceGreen) {
            isGreenLight = true;
            forceGreen = false; // ✅ Reset after forcing once
        } else {
            isGreenLight = (System.currentTimeMillis() / 1000) % (duration * 2) < duration;
        }
    }

    public String getClearanceOrder() {
        List<String> order = new ArrayList<>();
        for (Vehicle v : emergencyLane)
            order.add(v.getId());
        for (Vehicle v : normalLane)
            order.add(v.getId());
        return String.join(" -> ", order);
    }
}
