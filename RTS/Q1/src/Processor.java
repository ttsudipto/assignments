import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Vector;

/**
 * Class representing a processor.
 *
 * <p>
 *     The processor starts running by invoking the {@link #run(Vector)}
 *     method. It executes the system of processes represented by
 *     {@link #processes}.
 * </p>
 * <p>
 *     It maintains a ready queue which is a priority queue that facilitates
 *     the scheduling of the processes. The {@link ProcessInstance} with
 *     lower absolute deadline is assigned higher priority. Earliest Deadline
 *     First (EDF) scheduling is performed by {@link #schedule()} method.
 * </p>
 */
public class Processor {

    /**
     * Ready queue.
     */
    private PriorityQueue<ProcessInstance> readyQueue;
    /**
     * {@link ProcessInstance} that is currently executing.
     */
    private ProcessInstance runningInstance;
    /**
     * System of {@link Process}s.
     */
    private Vector<Process> processes;

    /**
     * Constructor.
     */
    public Processor() {
        readyQueue = new PriorityQueue<>(new Comparator<ProcessInstance>() {
            @Override
            public int compare(ProcessInstance processInstance, ProcessInstance t1) {
                if (processInstance.getAbsoluteDeadline() < t1.getAbsoluteDeadline())
                    return -1;
                else if(processInstance.getAbsoluteDeadline() == t1.getAbsoluteDeadline())
                    return 0;
                else
                    return 1;
            }
        });
        runningInstance = null;
        processes = null;
    }

    /**
     * Computes the greatest common divisor of two integers.
     * @param a first number.
     * @param b second number.
     * @return gcd(a, b)
     */
    private int gcd(int a, int b) {
        if (a == 0)
            return b;
        if (b == 0)
            return a;
        return gcd(b, a%b);
    }

    /**
     * Computes the least common multiple of two integers.
     * @param a first number.
     * @param b second number.
     * @return lcm(a, b)
     */
    private int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    /**
     * Computes the size of the major clock.
     *
     * <p>
     *     Major clock is the least common multiple of the
     *     periods of the system of processes. The schedule
     *     repeats after every major clock.
     * </p>
     *
     * @return the major clock.
     */
    private int getMajorClock() {
        if (processes.size() == 0)
            return 0;
        if (processes.size() == 1)
            return processes.firstElement().getPeriod();

        int clock = lcm(processes.get(0).getPeriod(), processes.get(1).getPeriod());
        for (int i=2; i<processes.size(); ++i)
            clock = lcm(clock, processes.get(i).getPeriod());

        return clock;
    }

    /**
     * Adds new {@link ProcessInstance}s to the ready queue.
     *
     * <p>
     *     Creates new {@link ProcessInstance}s of every {@link Process}
     *     in the system that are meant to be created at the given time
     *     instant. It, then, adds them to the ready queue.
     * </p>
     * @param time the current time instant.
     * @return <code>true</code>, if any {@link ProcessInstance} is added
     *          to the ready queue, <br/>
     *          <code>false</code>, otherwise.
     */
    private boolean addNewInstances(int time) {
        boolean added = false;
        for (int i=0; i<processes.size(); ++i) {
            Process p = processes.get(i);
            if (p.nextInstanceStartsAt() == time) {
                readyQueue.add(p.createInstance());
                added = true;
            }
        }
        return added;
    }

    /**
     * Starts running the processor.
     *
     * <p>
     *     The processor runs for one major cycle.
     *     If scheduling point is reached, it invokes
     *     the {@link #schedule()} method to schedule
     *     another {@link ProcessInstance}.
     * </p>
     *
     * @param processes the system of processes.
     */
    public void run(Vector<Process> processes) {
        this.processes = processes;
        int end = getMajorClock();
        boolean schedRequired = false;
        for (int time = 0; time < end; ++time) {
            System.out.println("-------------------------------------------------------");
            System.out.println("Time = " + time);
            if (runningInstance != null) {
                runningInstance.execute();
                if (runningInstance.isFinished()) {
                    System.out.println("(Process : " + runningInstance.getProcessName() + " Instance No. : " + runningInstance.getInstanceNo() + ") Finished");
                    runningInstance = null;
                    schedRequired = true;
                }
            }
            if (addNewInstances(time)) {
                schedRequired = true;
            }
            if (schedRequired) {
                if (runningInstance != null)
                    readyQueue.add(runningInstance);
                runningInstance = schedule();
            }
            schedRequired = false;
        }
        System.out.println("-------------------------------------------------------");
    }

    /**
     * Schedule a {@link ProcessInstance} using the Earliest
     * Deadline First (EDF) algorithm.
     *
     * @return the scheduled {@link ProcessInstance}, or
     *          <code>null</code>, if the ready queue is empty.
     */
    private ProcessInstance schedule() {
        System.out.println("Scheduling point reached");
        if (readyQueue.isEmpty()) {
            System.out.println("Ready queue empty");
            return null;
        }
        ProcessInstance pi = readyQueue.poll();
        System.out.println("(Process : " + pi.getProcessName() + " Instance No. : " + pi.getInstanceNo() + ") Scheduled");
        return pi;
    }
}
