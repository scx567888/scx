package cool.scx.beans;

import java.util.concurrent.ScheduledExecutorService;

/**
 * <p>ScxBeanFactoryOptions class.</p>
 *
 * @author scx567888
 * @version 2.0.9
 */
public class ScxBeanFactoryOptions {

    private boolean enableSchedulingWithAnnotation = true;
    private ScheduledExecutorService scheduler = null;
    private boolean allowCircularReferences = false;

    /**
     * <p>enableSchedulingWithAnnotation.</p>
     *
     * @param enableSchedulingWithAnnotation a boolean
     * @return a {@link cool.scx.beans.ScxBeanFactoryOptions} object
     */
    public ScxBeanFactoryOptions enableSchedulingWithAnnotation(boolean enableSchedulingWithAnnotation) {
        this.enableSchedulingWithAnnotation = enableSchedulingWithAnnotation;
        return this;
    }

    /**
     * <p>scheduler.</p>
     *
     * @param scheduler a {@link java.util.concurrent.ScheduledExecutorService} object
     * @return a {@link cool.scx.beans.ScxBeanFactoryOptions} object
     */
    public ScxBeanFactoryOptions scheduler(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    /**
     * <p>allowCircularReferences.</p>
     *
     * @param allowCircularReferences a boolean
     * @return a {@link cool.scx.beans.ScxBeanFactoryOptions} object
     */
    public ScxBeanFactoryOptions allowCircularReferences(boolean allowCircularReferences) {
        this.allowCircularReferences = allowCircularReferences;
        return this;
    }

    /**
     * <p>enableSchedulingWithAnnotation.</p>
     *
     * @return a boolean
     */
    public boolean enableSchedulingWithAnnotation() {
        return this.enableSchedulingWithAnnotation;
    }

    /**
     * <p>allowCircularReferences.</p>
     *
     * @return a boolean
     */
    public boolean allowCircularReferences() {
        return this.allowCircularReferences;
    }

    /**
     * <p>scheduler.</p>
     *
     * @return a {@link java.util.concurrent.ScheduledExecutorService} object
     */
    public ScheduledExecutorService scheduler() {
        return this.scheduler;
    }

}
