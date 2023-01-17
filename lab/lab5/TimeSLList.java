import javax.swing.plaf.SliderUI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that collects timing information about SLList getLast method.
 */
public class TimeSLList {
    private static void printTimingTable(List<Integer> Ns, List<Double> times, List<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        List<Integer> Ns = new LinkedList<>();
        List<Double> times = new LinkedList<>();
        List<Integer> opCounts = new LinkedList<>();

        for (int i = 1000; i <= 128000; i *= 2) {
            SLList L1 = new SLList();
            for (int j = 0; j < i; j += 1) {
                L1.addFirst(j);
            }
            Stopwatch sw = new Stopwatch();
            for (int m = 0; m < 10000; m += 1) {
                L1.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            Ns.add(i);
            times.add(timeInSeconds);
            opCounts.add(10000);
        }
        printTimingTable(Ns, times, opCounts);


    }

}
