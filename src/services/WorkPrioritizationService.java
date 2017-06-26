package services;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import data_generator.DataStream;
import entities.Job;
import entities.Skill;
import entities.Worker;

import java.util.*;

public class WorkPrioritizationService {

    private final Map<Skill, TreeSet<Job>> jobSkillMap = new HashMap<>(5);
    private final Map<Skill, List<Worker>> workerSkillMap = new HashMap<>(5);

    private final DataStream.JobStreamCallback jobStreamCallback;
    private DataStream dataStream;
    private JobAssigner[] jobAssigners;

    @Nullable
    private static WorkPrioritizationService INSTANCE = null;

    public static WorkPrioritizationService getInstance() {
        if (INSTANCE == null)
            INSTANCE = new WorkPrioritizationService();
        return INSTANCE;
    }


    private WorkPrioritizationService() {
        for (Skill skill: Skill.values()){
            jobSkillMap.put(skill, new TreeSet<>());
            workerSkillMap.put(skill, new LinkedList<>());
        }

        jobStreamCallback = setupJobStreamCallback();
        jobAssigners = new JobAssigner[] {
                new JobAssigner(Skill.S1), new JobAssigner(Skill.S2),
                new JobAssigner(Skill.S3), new JobAssigner(Skill.S4),
                new JobAssigner(Skill.S5)
        };
    }

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

    private void startJobAssignment() {
        for (JobAssigner assigner: jobAssigners) {
            assigner.start();
        }
    }

    private void initDataStream(DataStream dataStream) {
        this.dataStream = dataStream;
        dataStream.startStream(jobStreamCallback);
    }

    private DataStream.JobStreamCallback setupJobStreamCallback() {
        return job -> {
            jobSkillMap.get(job.getSkill()).add(job);
        };
    }

    private void fillWorkerSkillMap(List<Worker> workerList) {
        workerList.forEach(worker ->
                worker.getWorkerSkillSet().forEach(skill ->
                        workerSkillMap.get(skill).add(worker))
        );

        workerSkillMap.forEach(((skill, workers) -> {

        }));

    }

    private class JobAssigner extends Thread {

        private Skill skill;

        private JobAssigner(Skill skill) {
            this.skill = skill;
        }

        @Override
        public void run() {
            super.run();

            try {
                while (isAlive()) {

                    TreeSet<Job> jobTreeSet = jobSkillMap.get(skill);

                    if (jobTreeSet.size() == 0){
                        Thread.sleep(1000);
                        continue;
                    }

                    Job job = jobTreeSet.first();

                    List<Worker> workerTreeSet = workerSkillMap.get(skill);
                    if (workerTreeSet.size() == 0){
                        continue;
                    }
                    Worker worker = workerSkillMap.get(skill).iterator().next();
                    Worker worker1 = workerSkillMap.get(skill).iterator().next();
                    workerTreeSet.forEach(System.out::println);
                    worker.assignJob(job);
                    workerSkillMap.get(skill).remove(worker1);
//                    workerSkillMap.get(skill).add(worker);
                    jobSkillMap.get(skill).remove(job);
                    Thread.sleep(10000);
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
