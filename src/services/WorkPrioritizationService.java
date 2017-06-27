package services;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import data_generator.DataStream;
import entities.Job;
import entities.Skill;
import entities.Worker;

import java.util.*;


/**
 * The following class provides a service to assign Jobs from a live stream of
 * data to different workers based on the skill type required for received Job.
 *
 * Job assignment and Data stream reading happens in separate threads.
 * Use startService() call to start the service and the stopService() call to
 * stop it.
 */
public class WorkPrioritizationService {

    //  Stores a mapping between skills and jobs.
    private final Map<Skill, TreeSet<Job>> jobSkillMap = new HashMap<>(5);

    //  Stores a mapping between skills and workers.
    private final Map<Skill, List<Worker>> workerSkillMap = new HashMap<>(5);

    // Callback required to get jobs from live stream of data.
    private final DataStream.JobStreamCallback jobStreamCallback;

    // Provides stream of input jobs data.
    private DataStream dataStream;

    // Contains separate thread objects for assigning jobs. Each thread
    // assigns jobs of one particular skill type.
    private JobAssigner[] jobAssigners;

    @Nullable
    private static WorkPrioritizationService INSTANCE = null;

    // Creating a singleton object for the service.
    public static WorkPrioritizationService getInstance() {
        if (INSTANCE == null)
            INSTANCE = new WorkPrioritizationService();
        return INSTANCE;
    }


    /**
     * Setting up data structures. We add 5 fixed skill types as keys
     * for the jobs and workers map first.
     *
     * Then we initialize the jobsStream callback and initialize
     * thread objects for job assignment for every skill type.
     */
    private WorkPrioritizationService() {
        for (Skill skill: Skill.values()){
            jobSkillMap.put(skill, new TreeSet<>());
            workerSkillMap.put(skill, new LinkedList<>());
        }

        jobStreamCallback = initJobStreamCallback();
        jobAssigners = new JobAssigner[] {
                new JobAssigner(Skill.S1),  //  for Skill type S1
                new JobAssigner(Skill.S2),  //  for Skill type S2
                new JobAssigner(Skill.S3),  //  for Skill type S3
                new JobAssigner(Skill.S4),  //  for Skill type S4
                new JobAssigner(Skill.S5)   //  for Skill type S5
        };
    }

    /**
     * Call this method to start the Prioritization service.
     * @param workerList , list of all available workers
     * @param dataStream , stream of data for jobs
     */
    public void startService(@NotNull List<Worker> workerList,
                             @NotNull DataStream dataStream) {

        fillWorkerSkillMap(workerList);
        initDataStream(dataStream);
        startJobAssignment();
    }

    public void stopService() {
        if (dataStream != null)
            dataStream.stopStream();

        for (JobAssigner assigner : jobAssigners)
            assigner.interrupt();

    }

    /**
     * Returns all the workers with assigned values.
     * @return , list of all workers.
     */
    public List<Worker> getWorkers() {

        List<Worker> workers = new ArrayList<>();
        workerSkillMap.forEach((skill, workersList) ->
            workers.addAll(workersList)
        );

        return workers;
    }

    // Here we only start a job assignment thread if there  are any workers
    // assigned to it.
    private void startJobAssignment() {

        for (JobAssigner assigner: jobAssigners) {
            if (workerSkillMap.get(assigner.skill).size() > 0)
                assigner.start();
        }
    }

    // Here we call the startStream method of dataStream to start
    // receiving jobs.
    private void initDataStream(DataStream dataStream) {
        this.dataStream = dataStream;
        dataStream.startStream(jobStreamCallback);
    }

    // Add the received Job into its particular set based on skill type.
    private DataStream.JobStreamCallback initJobStreamCallback() {
        return job -> jobSkillMap.get(job.getSkill()).add(job);
    }

    /**
     * Takes the List of workers and adds them to our workersSkillMap
     * based on their skill type. Workers are sorted in a List with
     * worker having least number of assigned jobs in first position
     * @param workerList , list of available workers.
     */
    private void fillWorkerSkillMap(List<Worker> workerList) {
        workerList.forEach(worker ->
                worker.getWorkerSkillSet().forEach(skill ->
                        workerSkillMap.get(skill).add(worker))
        );
        workerSkillMap.forEach((skill, workers)->
                workers.sort(getWorkerComparator()));
    }

    /*
        Sorts two Worker object based on no. of assigned jobs in them. If
        number of assigned jobs is same, we separate them based on their
        arrival id i.e. workerID
     */
    private Comparator<Worker> getWorkerComparator() {
        return (w1, w2)->
                (Integer.compare(w1.getAssignedJobs().size(), w2.getAssignedJobs().size()) == 0) ?
                    Long.compare(w1.getWorkerID(), w2.getWorkerID()) :
                    Integer.compare(w1.getAssignedJobs().size(), w2.getAssignedJobs().size());

    }

    /**
     * Assigns a job from jobSkillMap for a particular skill to
     * a suitable Worker from jobWorkersMap.
     *
     * The Thread is alive throughout the lifetime of service.
     */
    private class JobAssigner extends Thread {

        //  Only look for jobs having this particular skill type
        private Skill skill;

        private JobAssigner(Skill skill) {
            this.skill = skill;
        }

        @Override
        public void run() {
            super.run();

            try {
                while (isAlive()) {

                    // if no job available for the particular skill or
                    // no worker present for particular skill, check back after 3 seconds
                    if (jobSkillMap.get(skill).size() == 0 ||
                            workerSkillMap.get(skill).size() == 0) {
                        Thread.sleep(3000);
                        continue;
                    }

                    // grab the first job. The one having highest priority
                    Job job = jobSkillMap.get(skill).first();

                    // grab the first Worker, having least no. of jobs and assign job to him
                    Worker worker = workerSkillMap.get(skill).iterator().next();
                    worker.assignJob(job);

                    // update the list order. The assigned worker will now go back.
                    workerSkillMap.get(skill).sort(getWorkerComparator());

                    // remove the assigned job from the list
                    jobSkillMap.get(skill).remove(job);

                    // wait for 0.1 second and repeat the process
                    Thread.sleep(100);
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
