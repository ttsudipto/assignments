/**
 * Class representing an instance of a {@link Process}.
 *
 * <p>
 *     It contains the following properties :
 *     <ul>
 *         <li>Name of the process</li>
 *         <li>Instance number</li>
 *         <li>Starting time</li>
 *         <li>Absolute deadline</li>
 *         <li>Remaining execution time</li>
 *     </ul>
 *     It is executed for one time instant by calling the
 *     {@link #execute()} method. Execution is finished
 *     if the {@link #remExecutionTime} is equal to
 *     <code>0</code>.
 * </p>
 */
public class ProcessInstance {

    /**
     * Name of the process.
     */
    private String  processName;
    /**
     * Instance number.
     */
    private int instanceNo;
    /**
     * Absolute deadline of the instance.
     */
    private int absoluteDeadline;
    /**
     * Period of the instance.
     */
    private int period;
    /**
     * Starting time of the instance when it gets created.
     */
    private int startTime;
    /**
     * Remaining execution time.
     */
    private int remExecutionTime;

    /**
     * Constructor.
     *
     * @param processName process name.
     * @param instanceNo instance number.
     * @param startTime starting time.
     * @param eTime total execution time.
     * @param absDeadline absolute deadline.
     * @param period period.
     */
    public ProcessInstance(String processName, int instanceNo, int startTime, int eTime, int absDeadline, int period) {
        this.processName = processName;
        this.instanceNo = instanceNo;
        this.absoluteDeadline = absDeadline;
        this.period = period;
        this.startTime = startTime;
        this.remExecutionTime = eTime;
    }

    /**
     * Returns the process name.
     *
     * @return process name.
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Returns the instance number.
     *
     * @return instance number.
     */
    public int getInstanceNo() {
        return instanceNo;
    }

    /**
     * Returns the absolute deadline.
     *
     * @return absolute deadline.
     */
    public int getAbsoluteDeadline() {
        return absoluteDeadline;
    }

    /**
     * Returns the period.
     *
     * @return period.
     */
    public int getPeriod() {
        return period;
    }

    /**
     * Returns the starting time.
     *
     * @return starting time.
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * Executes the instance.
     *
     * <p>
     *     This method performs the execution for one time instant
     *     by decrementing the {@link #remExecutionTime}. The
     *     {@link #remExecutionTime} ultimately reaches the value
     *     <code>0</code> and the execution gets finished.
     * </p>
     */
    public void execute() {
        if (!isFinished()) {
            remExecutionTime--;
            System.out.println("(Process : " + processName + " Instance No. : " + instanceNo + ") Executed");
        }
    }

    /**
     * Aborts the execution of the instance.
     */
    public void abort() {
        remExecutionTime = 0;
    }

    /**
     * Checks if the instance has finished execution.
     *
     * <p>
     *     It compares {@link #remExecutionTime} with <code>0</code>.
     * </p>
     *
     * @return <code>true</code>, if finished, <br/>
     *          <code>false</code>, otherwise.
     */
    public boolean isFinished() {
        return (remExecutionTime == 0);
    }

    /**
     * Checks if the deadline is missed by the instance.
     *
     * <p>
     *     Deadline is missed if the current time is greater
     *     than the absolute deadline.
     * </p>
     *
     * @param currentTime current time instant.
     * @return <code>true</code>, if deadline missed, <br/>
     *          <code>false</code>, otherwise.
     */
    public boolean isDeadlineMissed(int currentTime) {
        return currentTime > absoluteDeadline;
    }
}
