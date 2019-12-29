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
 *     lower period is assigned higher priority. Rate Monotonic (RM)
 *     scheduling is performed by {@link #schedule()} method.
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
                if (processInstance.getPeriod() < t1.getPeriod())
                    return -1;
                else if(processInstance.getPeriod() == t1.getPeriod())
                    return 0;
                else
                    return 1;
            }
        });
        runningInstance = null;
        processes = null;
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
     *     If scheduling point is reached, it invokes
     *     the {@link #schedule()} method to schedule
     *     another {@link ProcessInstance}.
     * </p>
     *
     * @param processes the system of processes.
     */
    public void run(Vector<Process> processes) {
        this.processes = processes;
        int end = 40;
        boolean schedRequired = false;
        for (int time = 0; time < end; ++time) {
            System.out.println("-------------------------------------------------------");
            System.out.println("Time = " + time);
            if (runningInstance != null) {
                if(runningInstance.isDeadlineMissed(time))
                    runningInstance.abort();
                else
                    runningInstance.execute();

                if (runningInstance.isFinished()) {
                    System.out.println("(Process : " + runningInstance.getProcessName() + " Instance No. : " + runningInstance.getInstanceNo() + ") Finished");
                    if(runningInstance.isDeadlineMissed(time))
                        System.out.println("DEADLINE MISSED");
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
     * Schedule a {@link ProcessInstance} using the Rate
     * Monotonic (RM) scheduling algorithm.
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
