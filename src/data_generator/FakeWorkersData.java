package data_generator;

import entities.Skill;
import entities.Worker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * Generates fake workers data. We need to pass the maximum number of
 * fake worker objects required to perform the process.
 */
public class FakeWorkersData {

    private static Random random = new Random();

    public static ArrayList<Worker> generateWorkers(int workersRequired) {

        int i = 0;
        ArrayList<Worker> workers = new ArrayList<>(workersRequired);
        while (i < workersRequired) {
            workers.add(new Worker(generateRandomSkills()));
            i++;
        }
        return workers;
    }


    /**
     * Generates a random set of skills for a particular user.
     * @return , a set of skills.
     */
    private static Set<Skill> generateRandomSkills() {

        Set<Skill> skillSet = new HashSet<>(1);

        // we are putting a tab on maximum skills for a worker to one.
        int maxSkills = 1;
        int i = 0;
        while (i < maxSkills){
            skillSet.add(Skill.values()[random.nextInt(5)]);
            i++;
        }
        return skillSet;
    }
}
