package data_generator;

import entities.Skill;
import entities.Worker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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


    private static Set<Skill> generateRandomSkills() {

        Set<Skill> skillSet = new HashSet<>(1);
        int maxSkills = 1;
        int i = 0;
        while (i < maxSkills){
            skillSet.add(Skill.values()[random.nextInt(5)]);
            i++;
        }
        return skillSet;
    }
}
