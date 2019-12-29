/**
 * Class representing a resource requirement of a process.
 *
 * <p>
 *     The different properties are  :
 *     <ul>
 *         <li>{@link Resource}r</li>
 *         <li>Offset</li>
 *         <li>Duration</li>
 *     </ul>
 * </p>
 */
public class ResourceRequirement {

    /**
     * The {@link Resource}.
     */
    private Resource resource;
    /**
     * Offset of the resource requirement.
     */
    private int offset;
    /**
     * Duration of the resource requirement.
     */
    private int duration;

    /**
     * Constructor.
     *
     * @param resource the {@link Resource}
     * @param offset the offset.
     * @param duration the duration.
     */
    public ResourceRequirement(Resource resource, int offset, int duration) {
        this.resource = resource;
        this.offset = offset;
        this.duration = duration;
    }

    /**
     * Returns the {@link Resource}.
     *
     * @return the {@link Resource}.
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Returns the offset.
     *
     * @return the offset.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Returns the duration.
     *
     * @return the duration.
     */
    public int getDuration() {
        return duration;
    }
}
