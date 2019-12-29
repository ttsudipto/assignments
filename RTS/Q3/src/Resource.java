import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Class representing a resource.
 *
 * <p>
 *     It contains the different properties of a resource, such as :
 *     <ul>
 *         <li>Resource number</li>
 *         <li>Current user</li>
 *         <li>Waiting Queue</li>
 *     </ul>
 *     Current user is the {@link ProcessInstance} that currently holds
 *     the resource. Waiting queue is a priority queue that stores the
 *     {@link ProcessInstance}s that are waiting for the resource. Its
 *     priority is same as the current priority of the {@link ProcessInstance}s.
 * </p>
 */
public class Resource {
    /**
     * Resource number.
     */
    private int resNo;
    /**
     * Current user.
     */
    private ProcessInstance user;
    /**
     * Waiting queue.
     */
    private PriorityQueue<ProcessInstance> waitingQueue;

    /**
     * Constructor.
     *
     * @param number resource number.
     */
    public Resource(int number) {
        resNo = number;
        waitingQueue = new PriorityQueue<>(new Comparator<ProcessInstance>() {
            @Override
            public int compare(ProcessInstance processInstance, ProcessInstance t1) {
                if (processInstance.getCurrentPriority() > t1.getCurrentPriority())
                    return -1;
                else if (processInstance.getCurrentPriority() == t1.getCurrentPriority())
                    return 0;
                else
                    return 1;
            }
        });
        user = null;
    }

    /**
     * Returns the resource number.
     *
     * @return resource number.
     */
    public int getResNo() {
        return resNo;
    }

    /**
     * Returns the {@link ProcessInstance} currently holding
     * the resource.
     *
     * @return {@link ProcessInstance} holding the resource.
     */
    public ProcessInstance getUser() {
        return user;
    }

    /**
     * Updates the current user of the resource.
     *
     * @param user user {@link ProcessInstance}.
     */
    public void setUser(ProcessInstance user) {
        this.user = user;
    }

//    public void removeUser() {
//        this.user = null;
//    }

    /**
     * Add {@link ProcessInstance} to the waiting queue.
     *
     * @param instance {@link ProcessInstance} to be added.
     */
    public void addWaitingInstance(ProcessInstance instance) {
        waitingQueue.add(instance);
    }

//    public ProcessInstance removeWaitingInstance() {
//        return waitingQueue.poll();
//    }

    /**
     * Returns the priority that can be inherited by
     * the current user in accordance to PIP.
     *
     * @return inheritable priority.
     */
    public int getInheritablePriority() {
        ProcessInstance instance = waitingQueue.peek();
        if (instance == null) {
            if (user != null)
                return user.getCurrentPriority();
            else
                return -1;
        }
        return instance.getCurrentPriority();
    }

    /**
     * Checks if the given {@link ProcessInstance} is waiting
     * for the process.
     *
     * @param instance the {@link ProcessInstance}.
     * @return <code>true</code>, if waiting, <br/>
     *          <code>false</code>, otherwise.
     */
    public boolean isWaiting(ProcessInstance instance) {
        return waitingQueue.contains(instance);
    }
}
