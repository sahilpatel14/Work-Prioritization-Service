package entities;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Worker {

    // a global variable to provide unique incremental Ids to Workers
    private static long globalWorkerID;

    // Unique identifier for every worker
    private final long workerID;

    //  A set of all skills of a particular worker
    private final Set<Skill> skillSet = new LinkedHashSet<>(5);

    //  A List of All the Jobs that are assigned to Worker
    private ArrayList<Job> assignedJobs;


    public Worker(Set<Skill> skills) {
        skillSet.addAll(skills);
        this.workerID = globalWorkerID++;
        assignedJobs = new ArrayList<>();
    }


    public Set<Skill> getWorkerSkillSet() {
        return skillSet;
    }

    /**
     * Adds a particular job to list of assigned Jobs for
     * this worker
     * @param job , to be assigned
     */
    public void assignJob(Job job) {
        assignedJobs.add(job);
    }

    public long getWorkerID() {
        return workerID;
    }

    public ArrayList<Job> getAssignedJobs() {
        return assignedJobs;
    }


    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Worker ) &&
                ((Worker)obj).getWorkerID() == workerID;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder(workerID + ",[");
        assignedJobs.forEach(sb::append);
        sb.append("]");
        return sb.toString();
    }
}
