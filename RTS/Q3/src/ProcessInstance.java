import java.util.Vector;

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
 *         <li>Period</li>
 *         <li>Remaining execution time</li>
 *         <li>Original priority</li>
 *         <li>Current Priority</li>
 *     </ul>
 *     Priorities can change due to Priority Inheritance
 *     Protocol (PIP). It is executed for one time instant
 *     by calling the {@link #execute()} method. Execution
 *     is finished if the {@link #remExecutionTime} is equal
 *     to <code>0</code>.
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
     * Original priority.
     */
    private int originalPriority;
    /**
     * Current priority.
     */
    private int currentPriority;
    /**
     * Remaining execution time.
     */
    private int remExecutionTime;
    /**
     * Total execution time.
     */
    private int executionTime;
    /**
     * <code>Vector</code> of {@link ResourceRequirement}s representing
     * the requirements of resources by the process instance.
     */
    private Vector<ResourceRequirement> resourceRequirements;
    /**
     * <code>Vector</code> of {@link Resource}s held by the instance.
     */
    private Vector<Resource> heldResources;

    /**
     * Constructor.
     *
     * @param instanceNo instance number.
     * @param startTime starting time.
     * @param process {@link Process} whose instance it is.
     */
    public ProcessInstance(int instanceNo, int startTime, Process process) {
        this.processName = process.getName();
        this.instanceNo = instanceNo;
        this.startTime = startTime;
        this.absoluteDeadline = startTime + process.getDeadline();
        this.period = process.getPeriod();
        this.originalPriority = process.getPriority();
        this.currentPriority = originalPriority;
        this.remExecutionTime = process.getExecutionTime();
        this.executionTime = this.remExecutionTime;
        resourceRequirements = process.getResourceRequirements();
        heldResources = new Vector<>();
    }

    /**
     * Returns the current priority.
     *
     * @return current priority.
     */
    public int getCurrentPriority() {
        return currentPriority;
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
     * Changes the current priority.
     *
     * @param currentPriority the priority to set.
     */
    public void setCurrentPriority(int currentPriority) {
        this.currentPriority = currentPriority;
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
        releaseAllResources();
    }

    /**
     * Method to make the instance hold a resource.
     *
     * <p>
     *     It adds the resource to the list of resources held.
     *     It also sets itself as the user of the resource.
     * </p>
     * @param resource {@link Resource} to be held.
     */
    public void holdResource(Resource resource) {
        if (!heldResources.contains(resource))
            heldResources.addElement(resource);
        resource.setUser(this);
        System.out.println("(Process : " + processName + " Instance No. : " + instanceNo + ") Acquired resource : " + resource.getResNo());
    }

    /**
     * Method to make the instance release a resource.
     *
     * <p>
     *     It removes the resource from the list of resources held.
     *     It also sets <code>null</code> as the user of the resource.
     *     and reverts back to its original priority.
     * </p>
     * @param resource {@link Resource} to be released.
     */
    public void releaseResource(Resource resource) {
        heldResources.remove(resource);
        resource.setUser(null);
        currentPriority = originalPriority;
        System.out.println("(Process : " + processName + " Instance No. : " + instanceNo + ") Released resource : " + resource.getResNo());
    }

    /**
     * Method to make the instance release all the resources.
     *
     * <p>
     *     It removes all resources from the list of resources held.
     * </p>
     */
    public void releaseAllResources() {
        int end = heldResources.size();
        for (int i=0; i<end; ++i) {
            Resource res = heldResources.get(i);
            releaseResource(res);
        }
        currentPriority = originalPriority;
    }

    /**
     * Finds the resources to be acquired at the current time instant.
     *
     * @return <code>Vector</code> of {@link Resource}s required.
     */
    public Vector<Resource> currentResRequirement() {
        Vector<Resource> resources = new Vector<>();
        for (int i=0; i<resourceRequirements.size(); ++i) {
            ResourceRequirement req = resourceRequirements.get(i);
            int timeExecuted = executionTime - remExecutionTime;
//            System.out.println("Time executed : " + timeExecuted);
//            System.out.println("Offset : " + req.getOffset());
            if (timeExecuted == req.getOffset())
                if (req.getResource().getUser() != this || !req.getResource().isWaiting(this))
                    resources.addElement(req.getResource());
        }

        return resources;
    }

    /**
     * Finds the resources to be released at the current time instant.
     *
     * @return <code>Vector</code> of {@link Resource}s to be released.
     */
    public Vector<Resource> currentResRelease() {
        Vector<Resource> resources = new Vector<>();
        for (int i=0; i<resourceRequirements.size(); ++i) {
            ResourceRequirement req = resourceRequirements.get(i);
            int timeExecuted = executionTime - remExecutionTime;
            if (timeExecuted == (req.getOffset() + req.getDuration()))
                resources.addElement(req.getResource());
        }

        return resources;
    }


//    public int getInheritablePriority() {
//        if (heldResources.isEmpty())
//            return originalPriority;
//        int priority = heldResources.firstElement().getInheritablePriority();
//        for (int i=1; i<heldResources.size(); ++i) {
//            int p = heldResources.get(i).getInheritablePriority();
//            if (p > priority)
//                priority = p;
//        }
//        return priority;
//    }

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
