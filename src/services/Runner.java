package services;

import data_generator.DataStream;
import data_generator.FakeDataStream;
import data_generator.FakeWorkersData;
import entities.Job;
import entities.Skill;
import entities.Worker;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Runner {

    public static void main(String[] args) {

        WorkPrioritizationService service = WorkPrioritizationService.getInstance();
        DataStream dataStream = new FakeDataStream();
        service.startService(FakeWorkersData.generateWorkers(10),dataStream);

//        Set<Worker> myWorkers = new TreeSet<>();
//
//        List<Worker> workers = FakeWorkersData.generateWorkers(10);
//
//        for (Worker worker : workers){
//            if (worker.getWorkerID() == 1){
//                worker.assignJob(new Job(31, Skill.S5));
//                worker.assignJob(new Job(33,Skill.S5));
//            }
//
//            if (worker.getWorkerID() == 7) {
//                worker.assignJob(new Job(3, Skill.S3));
//            }
//            boolean b = myWorkers.add(worker);
//            if (b){
//                System.out.println("added");
//            }
//        }
//
//        myWorkers.forEach(System.out::println);
//        boolean b = myWorkers.remove(workers.get(2));
//        System.out.print(b);
//        myWorkers.forEach(System.out::println);
//
//        System.out.print("fdf");
    }
}
