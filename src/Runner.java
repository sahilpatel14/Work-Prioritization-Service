import data_generator.DataStream;
import data_generator.FakeDataStream;
import data_generator.FakeWorkersData;
import entities.Worker;
import services.WorkPrioritizationService;

import java.util.List;
import java.util.Scanner;

/**
 * Use this class to setup the whole service and run it.
 * Contains a main method that initializes fake workers data, fake job stream
 * and passes it to our WorkPrioritizationService class. From here
 * Jobs start to get assigned.
 */
public class Runner {

    private WorkPrioritizationService service;

    private void start() {
        service = WorkPrioritizationService.getInstance();
        DataStream dataStream = new FakeDataStream();
        service.startService(FakeWorkersData.generateWorkers(10),dataStream);

    }

    private void initiate() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Service started!");
        start();
        System.out.println("Type 'stop' to terminate the service.");

        String line = sc.nextLine();

        while (!line.equalsIgnoreCase("stop")) {
            System.out.println("Type 'stop' to terminate he service.");
            line = sc.nextLine();
        }
        System.out.println("Service stopped!");

        List<Worker> workers = service.getWorkers();

        workers.forEach(worker -> {
            System.out.print(worker.getWorkerID()+","+
                    worker.getWorkerSkillSet().iterator().next()+" : ");
            worker.getAssignedJobs().forEach(System.out::print);
            System.out.println();
        });
    }

    public static void main(String[] args) {
        new Runner().initiate();
    }
}
