package data_generator;

import com.sun.istack.internal.NotNull;
import entities.Job;
import entities.Skill;

import java.util.Random;

/**
 * Provides a live stream of jobs as live data. We need to call the startStream method to start
 * the data stream. JobStreamCallback is used to return job whenever it is ready.
 *
 * We can update minimum and maximum delay between two jobs.
 * To stop the stream call stopStream method.
 */
public class FakeDataStream extends Thread implements DataStream {

    private JobStreamCallback callback;

    // minimum time delay between two job responses.
    private static final int minimumTimeDelayInMs = 500;

    // maximum time delay between two job responses.
    private static final int maximumTimeDelayInMs = 1000;

    // generates random data in a particular range.
    private Random randomNumberGenerator = new Random();

    public FakeDataStream() { }

    @Override
    public void run() {
        super.run();
        int rand;
        try {
            while (isAlive()) {
                rand = randomNumberGenerator.nextInt(
                        (maximumTimeDelayInMs - minimumTimeDelayInMs) + 1) + maximumTimeDelayInMs;
                callback.newJob(getJob());
                Thread.sleep(rand);
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    @Override
    public void startStream(@NotNull DataStream.JobStreamCallback callback) {
        this.callback = callback;
        start();
    }

    @Override
    public void stopStream() {
        super.interrupt();
    }

    /**
     * Creates a random Job object with a random skill type and
     * a random priority level
     * @return , a Job object.
     */
    private synchronized Job getJob() {

        Skill skill = Skill.values()[randomNumberGenerator.nextInt(5)];
        int priority = randomNumberGenerator.nextInt(101);
        return new Job(priority, skill);
    }
}


