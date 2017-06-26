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

        int maxSkills = random.nextInt(5);
        maxSkills = maxSkills == 0? 1 : maxSkills;
        Set<Skill> skillSet = new HashSet<>(1);
        skillSet.add(Skill.S1);
//        maxSkills = 1;
//        int i = 0;
//        while (i < maxSkills){
//            skillSet.add(Skill.values()[i]);
//            i++;
//        }
        return skillSet;
    }
}
