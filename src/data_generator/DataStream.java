package data_generator;

import com.sun.istack.internal.NotNull;
import entities.Job;

import java.util.ArrayList;

public interface DataStream {

    void startStream(@NotNull JobStreamCallback callback);
    void stopStream();

    interface JobStreamCallback {
        void newJob(Job job);
    }
}
