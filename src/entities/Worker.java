package entities;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Worker implements Comparable{

    private static long globalWorkerID;

    private final long workerID;
    private final Set<Skill> skillSet = new LinkedHashSet<>(5);
    private ArrayList<Job> assignedJobs;


    public Worker(Skill skill) {
        skillSet.add(skill);
        this.workerID = globalWorkerID++;
        assignedJobs = new ArrayList<>();
    }

    public Worker(Set<Skill> skills) {
        skillSet.addAll(skills);
        this.workerID = globalWorkerID++;
        assignedJobs = new ArrayList<>();
    }

    public Skill getWorkerSkill(){
        return skillSet.iterator().next();
    }

    public Set<Skill> getWorkerSkillSet() {
        return skillSet;
    }

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
        if (obj instanceof Worker){
            return compareTo(obj) == 0;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hashCode = (int)workerID;
        hashCode += skillSet.hashCode();
        return (hashCode);
    }

    @Override
    public int compareTo(Object o) {


        if (!(o instanceof Worker))
            return -1;


        Worker worker = (Worker) o;
        if (this.assignedJobs.size() == worker.assignedJobs.size()) {
            return Long.compare(this.workerID, worker.workerID);
        }
        else {
            return Integer.compare(assignedJobs.size(), worker.assignedJobs.size());
        }
//
//        int r = Long.compare(this.workerID, worker.workerID);
//
//        if (r == 0)
//            return r;
//
//        r = Integer.compare(this.assignedJobs.size(), worker.assignedJobs.size());
//
//        if ( r == 0)
//            return -1;
//
//        return r;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder(workerID + ",[");
        assignedJobs.forEach(sb::append);
        sb.append("]");
        return sb.toString();
    }
}
