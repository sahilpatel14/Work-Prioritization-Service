import data_generator.DataStream;
import data_generator.FakeDataStream;
import data_generator.FakeWorkersData;
import services.WorkPrioritizationService;

/**
 * Use this class to setup the whole service and run it.
 * Contains a main method that initializes fake workers data, fake job stream
 * and passes it to our WorkPrioritizationService class. From here
 * Jobs start to get assigned.
 */
public class Runner {


    public static void main(String[] args) {

        WorkPrioritizationService service = WorkPrioritizationService.getInstance();
        DataStream dataStream = new FakeDataStream();
        service.startService(FakeWorkersData.generateWorkers(10),dataStream);

    }
}
