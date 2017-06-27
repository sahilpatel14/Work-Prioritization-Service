package entities;

public class Job implements Comparable<Job>{

    // a global variable to provide unique incremental Ids to Every Job
    private static long globalJobID;

    // a unique id for every job
    private final long jobID;

    // priority level for the job [0,100]
    private final int jobPriority;

    // required skill for completing the job
    private final Skill requiredSkill;

    public Job(int jobPriority, Skill skill) {
        this.jobPriority = jobPriority;
        this.requiredSkill = skill;
        jobID = globalJobID++;
    }

    public int getJobPriority() {
        return jobPriority;
    }

    public Skill getSkill() {
        return requiredSkill;
    }

    /**
     * Since we are using a TreeSet, Comparator interface must be implemented.
     * Here we compare two job types for sorting to take place.
     * @param other , the other job we are comparing with
     * @return , 0 if both are equal, -n if current job should come first, +n
     * if it should come afterwards.
     */
    @Override
    public int compareTo(Job other) {

        return other.jobPriority == this.jobPriority ?
                (int) (other.jobID - this.jobID) :
                other.jobPriority - this.jobPriority;
    }


    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Job) && ((Job)obj).jobID == this.jobID;
    }

    @Override
    public String toString() {
        return "("+ getJobPriority()+","+getSkill()+")";
    }
}
