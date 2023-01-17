package bearmaps.proj2d;

import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.PointSet;
import bearmaps.proj2ab.WeirdPointSet;
import bearmaps.proj2c.streetmap.StreetMapGraph;
import bearmaps.proj2c.streetmap.Node;
import edu.princeton.cs.algs4.TrieSET;


import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, Mary Guo
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    private TrieSET trie;
    private Map<Point, Node> pointToNode;
    private List<Point> points;
    private Map<String, List<Node>> nameToNodes;
    private List<String> cleanNames = new LinkedList<>();
    private List<Node> nodes = this.getNodes();
  

    private void addCleanNames(List<Node> nodeList) {
        for (Node n : nodeList) {
            if (name(n.id()) != null) {
                String name = cleanString(name(n.id()));
                trie.add(name);
                cleanNames.add(name);
                if (!nameToNodes.containsKey(name)) {
                    nameToNodes.put(name, new LinkedList<>());
                }
                nameToNodes.get(name).add(n);
            }

            if (neighbors(n.id()).size() > 0) {
                Point p = new Point(n.lon(), n.lat());
                points.add(p);
                pointToNode.put(p, n);
            }
        }
    }


    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        points = new ArrayList<>();
        trie = new TrieSET();
        pointToNode = new HashMap<>();
        nameToNodes = new HashMap<>();
        addCleanNames(nodes);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        PointSet set = new WeirdPointSet(points);
        return pointToNode.get(set.nearest(lon, lat)).id();

    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> names = new LinkedList<>();
        for (String s : cleanNames) {
            if (s.indexOf(prefix) == 0 && nameToNodes.containsKey(s)) {
                for (Node n :nameToNodes.get(s)) {
                    if (!names.contains(n.name())) {
                        names.add(n.name());
                    }
                }
            }
        }
        return names;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> locations = new LinkedList<>();
        String cleanName = cleanString(locationName);
        if (nameToNodes.containsKey(cleanName)) {
            for (Node n : nameToNodes.get(cleanName)) {
                putInfo(n, locations);
            }
        }
        return locations;
    }

    private void putInfo(Node node, List<Map<String, Object>> list) {
        Map<String, Object> info = new HashMap<>();
        info.put("lat", node.lat());
        info.put("lon", node.lon());
        info.put("name", node.name());
        info.put("id", node.id());
        list.add(info);
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
