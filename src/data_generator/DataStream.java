package data_generator;

import com.sun.istack.internal.NotNull;
import entities.Job;

import java.util.ArrayList;

/**
 * A small behaviour object for every DataStream.
 * startStream should start the DataStream and stopStream should stop it.
 * Stream should be sent via JobStreamCallback
 */
public interface DataStream {

    void startStream(@NotNull JobStreamCallback callback);
    void stopStream();

    interface JobStreamCallback {
        void newJob(Job job);
    }
}
