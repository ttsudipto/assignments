import java.util.Vector;

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
 *         <li>Priority</li>
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
     * Priority of the process.
     */
    private int priority;
    /**
     * The instance number of the next {@link ProcessInstance}
     * to be created.
     */
    private int nextInstanceNo;
    /**
     * <code>Vector</code> of {@link ResourceRequirement}s representing
     * the requirements of resources by the process.
     */
    private Vector<ResourceRequirement> resourceRequirements;

    /**
     * Constructor.
     *
     * @param name name of the process.
     * @param phase phase of the process.
     * @param period period of the process.
     * @param eTime execution time of the process.
     * @param deadline relative deadline of the process.
     * @param priority priority of the process.
     */
    public Process(String name, int phase, int period, int eTime, int deadline, int priority) {
        this.name = name;
        this.phase = phase;
        this.period = period;
        this.executionTime = eTime;
        this.deadline = deadline;
        this.priority = priority;
        nextInstanceNo = 1;
        resourceRequirements = new Vector<>();
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
     * Returns the priority of the process.
     *
     * @return priority of the process.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Returns the {@link ResourceRequirement}s of the process.
     *
     * @return <code>Vector</code> of {@link ResourceRequirement}.
     */
    public Vector<ResourceRequirement> getResourceRequirements() {
        return resourceRequirements;
    }

    /**
     * Adds a {@link ResourceRequirement} to this process.
     * @param resource the required {@link Resource}.
     * @param offset offset of the time of requirement from the start
     *               of execution of the process relative to each
     *               {@link ProcessInstance}.
     * @param duration the time for which the resource is required.
     */
    public void addResourceRequirement(Resource resource, int offset, int duration) {
        resourceRequirements.addElement(new ResourceRequirement(resource, offset, duration));
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
        ProcessInstance instance = new ProcessInstance(nextInstanceNo, startTime,  this);
        System.out.println("(Process : " + name + " Instance No. : " + nextInstanceNo + ") Created;");
        System.out.println("Current priority : " + instance.getCurrentPriority());
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
