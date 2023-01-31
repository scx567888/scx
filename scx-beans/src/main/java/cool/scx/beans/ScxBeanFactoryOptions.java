package cool.scx.beans;

import java.util.concurrent.ScheduledExecutorService;

public class ScxBeanFactoryOptions {

    private boolean enableSchedulingWithAnnotation = true;
    private ScheduledExecutorService scheduler = null;
    private boolean allowCircularReferences = false;

    public ScxBeanFactoryOptions enableSchedulingWithAnnotation(boolean enableSchedulingWithAnnotation) {
        this.enableSchedulingWithAnnotation = enableSchedulingWithAnnotation;
        return this;
    }

    public ScxBeanFactoryOptions scheduler(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    public ScxBeanFactoryOptions allowCircularReferences(boolean allowCircularReferences) {
        this.allowCircularReferences = allowCircularReferences;
        return this;
    }

    public boolean enableSchedulingWithAnnotation() {
        return this.enableSchedulingWithAnnotation;
    }

    public boolean allowCircularReferences() {
        return this.allowCircularReferences;
    }

    public ScheduledExecutorService scheduler() {
        return this.scheduler;
    }

}
