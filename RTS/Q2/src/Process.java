/**
 * Class representing a process.
 *
 * <p>
 *     It contains the different properties of a process, such as :
 *     <ul>
 *         <li>Name</li>
 *         <li>Phase</li>
 *         <li>Period</li>
 *         <li>Execution time</li>
 *         <li>Deadline</li>
 *     </ul>
 *     It also provides method to create a new {@link ProcessInstance}.
 * </p>
 */
public class Process {
    /**
     * Name of the process.
     */
    private String name;
    /**
     * Phase of the process.
     */
    private int phase;
    /**
     * Period of the process.
     */
    private int period;
    /**
     * Execution time of the process.
     */
    private int executionTime;
    /**
     * Relative deadline of the process.
     */
    private int deadline;
    /**
     * The instance number of the next {@link ProcessInstance}
     * to be created.
     */
    private int nextInstanceNo;

    /**
     * Constructor.
     *
     * @param name name of the process.
     * @param phase phase of the process.
     * @param period period of the process.
     * @param eTime execution time of the process.
     * @param deadline relative deadline of the process.
     */
    public Process(String name, int phase, int period, int eTime, int deadline) {
        this.name = name;
        this.phase = phase;
        this.period = period;
        this.executionTime = eTime;
        this.deadline = deadline;
        nextInstanceNo = 1;
    }

    /**
     * Returns the name of the process.
     *
     * @return name of the process.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the phase of the process.
     *
     * @return phase of the process.
     */
    public int getPhase() {
        return phase;
    }

    /**
     * Returns the period of the process.
     *
     * @return period of the process.
     */
    public int getPeriod() {
        return period;
    }

    /**
     * Returns the execution time of the process.
     *
     * @return execution time of the process.
     */
    public int getExecutionTime() {
        return executionTime;
    }

    /**
     * Returns the relative deadline of the process.
     *
     * @return relative deadline of the process.
     */
    public int getDeadline() {
        return deadline;
    }

    /**
     * Creates a new {@link ProcessInstance} of the process.
     *
     * <p>
     *     The instance number of the created <code>ProcessInstance</code>
     *     is {@link #nextInstanceNo}.
     * </p>
     *
     * @return the newly created <code>ProcessInstance</code>.
     */
    public ProcessInstance createInstance() {
        int startTime = nextInstanceStartsAt();
        int absDeadline = startTime + deadline;
        ProcessInstance instance = new ProcessInstance(name, nextInstanceNo, startTime, executionTime, absDeadline, period);
        System.out.println("(Process : " + name + " Instance No. : " + nextInstanceNo + ") Created;");
        nextInstanceNo++;
        return instance;
    }

    /**
     * The time at which an instance of the process starts.
     *
     * @return the time instant.
     */
    public int nextInstanceStartsAt() {
        return phase + (nextInstanceNo - 1) * period;
    }
}
