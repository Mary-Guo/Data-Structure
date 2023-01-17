package bearmaps.proj2c;

import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import bearmaps.proj2ab.DoubleMapPQ;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private SolverOutcome outcome;
    private double solutionWeight = 0;
    private LinkedList<Vertex> solution;
    private double timeSpent;
    private int numStatesExplored = 0;
    private double distToP = 0;



    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();

        Map<Vertex, Vertex> edgeTo = new HashMap<>();
        Map<Vertex, Double> distTo = new HashMap<>();
        ExtrinsicMinPQ<Vertex> pq = new DoubleMapPQ<>();

        pq.add(start, input.estimatedDistanceToGoal(start, end));
        distTo.put(start, 0.00);

        solution = new LinkedList<>();

        while (pq.size() > 0) {
            if (sw.elapsedTime() > timeout) {
                outcome = SolverOutcome.TIMEOUT;
                timeSpent = sw.elapsedTime();
                return;
            }

            if (pq.getSmallest().equals(end)) {
                outcome = SolverOutcome.SOLVED;
                Vertex temp = end;

                while (!temp.equals(start)) {
                    solution.addFirst(temp);
                    temp = edgeTo.get(temp);
                }
                solution.addFirst(start);
                solutionWeight = distTo.get(end);
                timeSpent = sw.elapsedTime();
                return;
            }


            Vertex p = pq.removeSmallest();
            numStatesExplored += 1;

            distToP = distTo.get(p);
            for (WeightedEdge<Vertex> edge: input.neighbors(p)) {
                Vertex q = edge.to();
                double w = edge.weight();
                if (!distTo.containsKey(q) || (distToP + w < distTo.get(q))) {
                    distTo.put(q, distToP + w);
                    edgeTo.put(q, p);

                    double shortValue = distTo.get(q) + input.estimatedDistanceToGoal(q, end);
                    if (pq.contains(q)) {
                        pq.changePriority(q, shortValue);
                    } else {
                        pq.add(q, shortValue);
                    }
                }
            }
        }
        outcome = SolverOutcome.UNSOLVABLE;
        timeSpent = sw.elapsedTime();
    }



    @Override
    public SolverOutcome outcome() {
        return outcome;
    }
    @Override
    public List<Vertex> solution() {
        return solution;
    }
    @Override
    public double solutionWeight() {
        return solutionWeight;
    }
    @Override
    public int numStatesExplored() {
        return numStatesExplored;
    }
    @Override
    public double explorationTime() {
        return timeSpent;
    }
}

