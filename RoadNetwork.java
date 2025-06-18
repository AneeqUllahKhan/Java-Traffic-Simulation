import java.util.*;

public class RoadNetwork {
    private final Map<String, Intersection> intersections = new HashMap<>();
    private final Map<String, Map<String, Integer>> adjacentIntersections = new HashMap<>();

    public void addIntersection(String id) {
        intersections.put(id, new Intersection(id));
        adjacentIntersections.put(id, new HashMap<>());
    }

    public void addRoad(String from, String to, int travelTime) {
        adjacentIntersections.get(from).put(to, travelTime);
        adjacentIntersections.get(to).put(from, travelTime);
    }

    public Intersection getIntersection(String id) {
        return intersections.get(id);
    }

    public Map<String, Map<String, Integer>> getAdjacentIntersections() {
        return adjacentIntersections;
    }
}